package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email,password;
    private Button signInButton;
    private TextView signUpTextView;
    private ProgressBar signInProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign In activity");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email=findViewById(R.id.signInEmailEditTextId);
        password=findViewById(R.id.signInPasswordEditTextId);
        signInButton=findViewById(R.id.signInButtonId);
        signUpTextView=findViewById(R.id.signUpTextId);
        signInProgressBar=findViewById(R.id.ProgressBarId);

        signInButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.signInButtonId:
                userLogIn();
                break;
            case R.id.signUpTextId:
                Intent intent=new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                break;
            }
        }

    private void userLogIn() {
        String emailForSignUp=email.getText().toString().trim();
        String passwordForSignUp=password.getText().toString().trim();

        //checking email
        if(emailForSignUp.isEmpty()){
            email.setError("Enter an email address");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailForSignUp).matches()){
            email.setError("Please enter a valid email adddress");
            email.requestFocus();
            return;
        }

        //checking password
        if(passwordForSignUp.isEmpty()){
            password.setError("Enter your password");
            password.requestFocus();
            return;
        }

        if(passwordForSignUp.length()<6){
            password.setError("You should at least 6 character!!!");
            password.requestFocus();
            return;
        }

        signInProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailForSignUp,passwordForSignUp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signInProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {


                    Intent intent=new Intent(getApplicationContext(),EventActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                        Toast.makeText(getApplicationContext(),"Invalid Email or Password",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}

