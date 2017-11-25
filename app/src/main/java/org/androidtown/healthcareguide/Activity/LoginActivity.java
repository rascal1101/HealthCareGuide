package org.androidtown.healthcareguide.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.R;

/**
 * Created by MSI on 2017-11-09.
 */

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private FirebaseDatabase database;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;
    private String email;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }


        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.log_in_button);
        signupButton = findViewById(R.id.sign_up_button);
        emailEditText = findViewById(R.id.login_email_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("처리중입니다");
        progressDialog.setCanceledOnTouchOutside(false);

        SharedPreferences auto = getSharedPreferences("auto_login", Context.MODE_PRIVATE);

        emailEditText.setText(auto.getString("email",null));
        passwordEditText.setText(auto.getString("password",null));

        database = FirebaseDatabase.getInstance();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null) {
                    uid = user.getUid();
                    email = user.getEmail();
                }
            }
        };


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
               final String email = emailEditText.getText().toString();
               final String password = passwordEditText.getText().toString();

               progressDialog.show();

               mAuth.signInWithEmailAndPassword(email,password)
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   Log.d("rascal","Sign In Success");
                                   SharedPreferences sharedPreferences =
                                           LoginActivity.this.getSharedPreferences("auto_login", Context.MODE_PRIVATE);
                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString("email",email);
                                   editor.putString("password",password);

                                   editor.commit();



                                   database.getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           progressDialog.dismiss();
                                           String name = dataSnapshot.child("name").getValue(String.class);
                                           //String mode = dataSnapshot.child("mode").getValue(String.class);
                                           Intent intent;
                                           //if(mode.equals("normal")){
                                           intent = new Intent(LoginActivity.this, CarelistActivity.class);
//                                           }else{
//                                               intent = new Intent(LoginActivity.this, CarelistForDoctorActivity.class);
//                                           }
                                           intent.putExtra("uid", uid);
                                           intent.putExtra("email", email);
                                           intent.putExtra("name",name);
                                           startActivity(intent);
                                           finish();
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {
                                           progressDialog.dismiss();
                                       }
                                   });

                               } else {
                                   Log.d("rascal","Sign In Fail");
                                   progressDialog.dismiss();
                                   Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                               }
                           }
                       } );
           }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
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
