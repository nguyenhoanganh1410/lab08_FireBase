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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextView txtSignIn;
    private EditText etGmail;
    private EditText etPass;
    private Button btnRegis;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        txtSignIn = findViewById(R.id.txtSignin);
        btnRegis = findViewById(R.id.button4);
        etGmail = findViewById(R.id.emailRegister );
        etPass = findViewById(R.id.passworldRegister);

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        btnRegis.setOnClickListener(v ->{
            createUser();
        });



    }

    private void createUser() {
        String email = etGmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            etGmail.setError("Email cannot br empty");
            etGmail.requestFocus();
        }else if(TextUtils.isEmpty(pass)){
            etPass.setError("Password cannot br empty");
            etPass.requestFocus();
        }else {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful() ){
                        Toast.makeText(RegisterActivity.this, "user registered sucessfilly", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "registration erro :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
