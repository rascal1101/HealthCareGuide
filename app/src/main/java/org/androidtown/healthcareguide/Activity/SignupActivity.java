package org.androidtown.healthcareguide.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

/**
 * Created by MSI on 2017-11-09.
 */

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabasereference;
    private Button signupButton2;
    EditText emailEditText;
    EditText passwordEditText;
    EditText nameEditText;
    EditText phoneEditText;
    RadioButton normalRadio;
    RadioButton doctorRadio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupButton2 = findViewById(R.id.sign_up_button2);
        emailEditText = findViewById(R.id.sign_up_email_edit_text);
        passwordEditText = findViewById(R.id.sign_up_password_edit_text);
        nameEditText = findViewById(R.id.sign_up_name_edit_text);
        phoneEditText = findViewById(R.id.sign_up_phone_edit_text);
        normalRadio = findViewById(R.id.normal_radio);
        doctorRadio = findViewById(R.id.doctor_radio);

        signupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
    }

    private void signUpUser(String email, String password){

        mDatabasereference = FirebaseDatabase.getInstance().getReference();


        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Rascal", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User userModel = new User();
                            userModel.setEmail(emailEditText.getText().toString());
                            userModel.setName(nameEditText.getText().toString());
                            userModel.setPhone(phoneEditText.getText().toString());
                            if(normalRadio.isChecked())
                                userModel.setMode("normal");
                            else
                                userModel.setMode("doctor");
                            User userModel2 = new User();
                            userModel2.setEmail(userModel.getEmail());
                            userModel2.setName(userModel.getName());
                            mDatabasereference.child("users").child(user.getUid()).setValue(userModel);
                            mDatabasereference.child("users").child(user.getUid()).child("cpgroup").child(user.getUid()).setValue(userModel2);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Rascal", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
    }
}
