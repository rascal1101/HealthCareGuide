package org.androidtown.healthcareguide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Fragment.CareMeFragment;
import org.androidtown.healthcareguide.Fragment.CaredPeopleFragment;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

public class CarelistActivity extends AppCompatActivity {

    private Button caredPeopleButton;
    private Button careMeButton;
    private Fragment caredPeopleFragment;
    private Fragment careMeFragment;
    private FrameLayout container;
    public String uid;
    public String email;
    public String name;
    public User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carelist);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        email = intent.getStringExtra("email");
        currentUser = new User();

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        dr.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue(String.class);
                currentUser.setUid(uid);
                currentUser.setName(name);
                currentUser.setEmail(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        caredPeopleFragment = new CaredPeopleFragment();
        careMeFragment = new CareMeFragment();


        container = findViewById(R.id.carelist_container);
        caredPeopleButton = findViewById(R.id.cared_people_button);
        careMeButton = findViewById(R.id.care_me_button);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.carelist_container,caredPeopleFragment);
            ft.add(R.id.carelist_container,careMeFragment);
            ft.hide(careMeFragment);
            ft.show(caredPeopleFragment);
            ft.commit();
        }





            caredPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .hide(careMeFragment)
                        .show(caredPeopleFragment)
                        .commit();
            }
        });

        careMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .hide(caredPeopleFragment)
                        .show(careMeFragment)
                        .commit();
            }
        });

    }
}
