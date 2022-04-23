package com.example.lab08_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        txtRegister = findViewById(R.id.txtRegister);
        btnSignIn = findViewById(R.id.buttonSignIn);
        etGmail = findViewById(R.id.editTextTextPersonName );
        etPass = findViewById(R.id.editTextTextPersonName2);


        ggSignIn = findViewById(R.id.textView2);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        btnSignIn.setOnClickListener(v ->{
            loggin();
        });




    }

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

}
