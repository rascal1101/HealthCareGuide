package org.androidtown.healthcareguide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.androidtown.healthcareguide.R;

/**
 * Created by MSI on 2017-11-09.
 */

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private DatabaseReference databaseReference;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.log_in_button);
        signupButton = findViewById(R.id.sign_up_button);
        emailEditText = findViewById(R.id.login_email_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser !=null) {
                    uid = firebaseUser.getUid();
                    email = firebaseUser.getEmail();
                }
/*
                if(firebaseUser !=null){ //유저가 있는 경우
                    Intent intent = new Intent(LoginActivity.this, CarelistActivity.class);
                    intent.putExtra("uid", firebaseUser.getUid());
                    intent.putExtra("email", firebaseUser.getEmail());
                    startActivity(intent);
                    finish();
                }else{

                }
                */

            }
        };


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
               final String email = emailEditText.getText().toString();
               String password = passwordEditText.getText().toString();


               mAuth.signInWithEmailAndPassword(email,password)
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   Log.d("rascal","Sign In Success");
                                   Intent intent = new Intent(LoginActivity.this, CarelistActivity.class);
                                   intent.putExtra("uid", uid);
                                   intent.putExtra("email", email);
                                   startActivity(intent);
                                   finish();
                               } else {
                                   Log.d("rascal","Sign In Fail");
                                   Toast.makeText(LoginActivity.this, "Sign In Fail", Toast.LENGTH_SHORT).show();
                               }
                           }
                       } );
           }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(LoginActivity.this, org.androidtown.healthcareguide.Activity.SignupActivity.class);
               startActivity(intent);
           }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}