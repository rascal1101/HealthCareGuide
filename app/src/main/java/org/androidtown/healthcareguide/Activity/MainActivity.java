package org.androidtown.healthcareguide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

public class MainActivity extends AppCompatActivity {

    private TextView emailtv;
    private Button logoutButton;
    private FirebaseAuth mAuth;
    private User user;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String email = intent.getStringExtra("email");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        user = new User();
        user.setUid(uid);
        user.setEmail(email);

        logoutButton = findViewById(R.id.log_out_button);

        emailtv = findViewById(R.id.email);
        emailtv.setText("uid : " + uid + "\nemail : " + email);


        readUser();


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void readUser(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    try{
                        String key = snapshot.getKey();
                        if(user.getUid().equals(key)){
                            User tempUser = snapshot.getValue(User.class);
                            user.setUser(tempUser);
                            emailtv.setText("uid : " + user.getUid() + "\nemail : " + user.getEmail() +
                            "\nname : " + user.getName() + "\nphone : " + user.getPhone());

                            break;
                        }
                    }catch(Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
