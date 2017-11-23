package org.androidtown.healthcareguide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Adapter.CareListForDoctorAdapter;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.ArrayList;
import java.util.List;

public class CarelistForDoctorActivity extends AppCompatActivity {

    public static User currentUser;
    private ListView listView;
    private List<User> list;
    private CareListForDoctorAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carelist_for_doctor);

        setCurrentUser();
        initView();
        initAdapter();
        getListFromFirebase();

    }

    public void getListFromFirebase(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        dr.child("users").child(currentUser.getUid()).child("cpgroup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String email = ds.child("email").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.getKey();
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setUid(uid);
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new CareListForDoctorAdapter(getApplicationContext(),list, currentUser);
        listView.setAdapter(adapter);
    }

    public void initView(){
        listView = findViewById(R.id.d_cared_people_list);
        button = findViewById(R.id.sign_out);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CarelistForDoctorActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setCurrentUser(){
        Intent intent = getIntent();
        currentUser = new User();
        currentUser.setUid(intent.getStringExtra("uid"));
        currentUser.setName(intent.getStringExtra("name"));
        currentUser.setEmail(intent.getStringExtra("email"));
    }
}
