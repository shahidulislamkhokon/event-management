package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText,passwordEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private FirebaseAuth mAuth;
    private ProgressBar signUpProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up activity");
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailEditText=findViewById(R.id.signUpEmailEditTextId);
        passwordEditText=findViewById(R.id.signUpPasswordEditTextId);
        signUpButton=findViewById(R.id.signUpButtonId);
        signInTextView=findViewById(R.id.signInTextId);
        signUpProgressbar=findViewById(R.id.ProgressBarId);

        signUpButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.signUpButtonId:
                userRegister();
                break;
            case R.id.signInTextId:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userRegister() {

        String emailForSignUp=emailEditText.getText().toString().trim();
        String passwordForSignUp=passwordEditText.getText().toString().trim();

        //checking email
        if(emailForSignUp.isEmpty()){
            emailEditText.setError("Enter an email address");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailForSignUp).matches()){
            emailEditText.setError("Please enter a valid email adddress");
            emailEditText.requestFocus();
            return;
        }

        //checking password
        if(passwordForSignUp.isEmpty()){
            passwordEditText.setError("Enter your password");
            emailEditText.requestFocus();
            return;
        }

        if(passwordForSignUp.length()<6){
            emailEditText.setError("You should at least 6 character!!!");
            emailEditText.requestFocus();
            return;
        }

        signUpProgressbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailForSignUp,passwordForSignUp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signUpProgressbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        finish();
                        Toast.makeText(getApplicationContext(),"You are successfully registered",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),EventActivity.class);
                        startActivity(intent);

                } else {
                   if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"User is already registered",Toast.LENGTH_SHORT).show();
                        }

                   else{
                       Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });

    }

}

