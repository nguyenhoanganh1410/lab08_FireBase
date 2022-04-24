package com.example.lab08_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {
    private TextView txtRegister;
    private TextView ggSignIn;
    private EditText etGmail;
    private EditText etPass;
    private Button btnSignIn;
    FirebaseAuth mAuth;


    public static final String TAG = "GoogleSignIn";
    public static final int RC_SIGN_IN = 321;
    private SignInButton btnSignInWithGoogle;
    private GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        txtRegister = findViewById(R.id.txtRegister);
        btnSignIn = findViewById(R.id.buttonSignIn);
        etGmail = findViewById(R.id.editTextTextPersonName );
        etPass = findViewById(R.id.editTextTextPersonName2);


        //ggSignIn = findViewById(R.id.textView2);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //sign in with email/pass
        mAuth = FirebaseAuth.getInstance();
        btnSignIn.setOnClickListener(v ->{
            loggin();
        });


        //sign in with google
        btnSignInWithGoogle = findViewById(R.id.textView2);

        mAuth = FirebaseAuth.getInstance();
        requestGoogleSignIn();

        btnSignInWithGoogle.setOnClickListener(view -> {
            signIn();
        });
    }

    //sign in with google/pass
    private void loggin() {
        String email = etGmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            etGmail.setError("Email cannot br empty");
            etGmail.requestFocus();
        }else if(TextUtils.isEmpty(pass)){
            etPass.setError("Password cannot br empty");
            etPass.requestFocus();
        }else {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful() ){
                        Toast.makeText(SignInActivity.this, "user signin sucessfilly", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    }
                    else{
                        Toast.makeText(SignInActivity.this, "registration erro :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

/////////////////////////////////////////////////////
    //sign in with google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void requestGoogleSignIn(){
        // Configure sign-in to request the user’s basic profile like name and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating user with firebase using received token id
                firebaseAuthWithGoogle(account.getIdToken());

                //assigning user information to variables
//                String userName = account.getDisplayName();
//                String userEmail = account.getEmail();
//                String userPhoto = account.getPhotoUrl().toString();
//                userPhoto = userPhoto+"?type=large";
//
//                //create sharedPreference to store user data when user signs in successfully
//                SharedPreferences.Editor editor = getApplicationContext()
//                        .getSharedPreferences("MyPrefs",MODE_PRIVATE)
//                        .edit();
//                editor.putString("username", userName);
//                editor.putString("useremail", userEmail);
//                editor.putString("userPhoto", userPhoto);
//                editor.apply();

                Log.i(TAG, "onActivityResult: Success");

            } catch (ApiException e) {
                Log.e(TAG, "onActivityResult: " + e.getMessage());
            }
        }
    }



    private void firebaseAuthWithGoogle(String idToken) {

        //getting user credentials with the help of AuthCredential method and also passing user Token Id.
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        //trying to sign in user using signInWithCredential and passing above credentials of user.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");

                            // Sign in success, navigate user to Profile Activity
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "User authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
