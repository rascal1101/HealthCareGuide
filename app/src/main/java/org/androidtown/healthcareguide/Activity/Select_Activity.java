package org.androidtown.healthcareguide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Model.BloodPressureInformation;
import org.androidtown.healthcareguide.Model.DiabetesInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjhyj on 2017-11-19.
 */

public class Select_Activity extends AppCompatActivity {

    public static User caredUser;
    public static User currentUser;
    public static DatabaseReference drDiabetes;
    public static DatabaseReference drBloodPressure;
    public static DatabaseReference drSymptom;
    public static List<DiabetesInformation> dbList;
    public static List<DiabetesInformation> daList;
    public static List<BloodPressureInformation> bpList;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);

        setUser();
        initView();
        getListFromFirebase();
    }

    private void getListFromFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("blood_pressure").child(caredUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bpList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    BloodPressureInformation bpi = ds.getValue(BloodPressureInformation.class);
                    bpList.add(bpi);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseDatabase.getReference().child("diabetes").child(caredUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbList.clear();
                daList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    DiabetesInformation di = ds.getValue(DiabetesInformation.class);
                    if(di.getEat().equals("식전")){
                        dbList.add(di);
                    }else{
                        daList.add(di);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUser(){
        caredUser = new User();
        Intent intent = getIntent();
        caredUser.setEmail(intent.getStringExtra("email"));
        caredUser.setName(intent.getStringExtra("name"));
        caredUser.setUid(intent.getStringExtra("uid"));
        currentUser = CarelistActivity.currentUser;
    }

    public void initView(){
        drBloodPressure = FirebaseDatabase.getInstance().getReference().child("blood_pressure");
        drDiabetes = FirebaseDatabase.getInstance().getReference().child("diabetes");
        drSymptom = FirebaseDatabase.getInstance().getReference().child("symptom");

        dbList = new ArrayList<>();
        daList = new ArrayList<>();
        bpList = new ArrayList<>();

        textView = findViewById(R.id.note_name);
        textView.setText(caredUser.getName());

        findViewById(R.id.bt_diabetes).setOnClickListener(ocn);
        findViewById(R.id.bt_graph).setOnClickListener(ocn);
        findViewById(R.id.bt_blood).setOnClickListener(ocn);
        findViewById(R.id.bt_symptom).setOnClickListener(ocn);



    }

    Button.OnClickListener ocn = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.bt_diabetes:
                    Intent intent1 = new Intent(Select_Activity.this,Diabets_Activity.class);
                    startActivity(intent1);
                    break;

                case R.id.bt_blood:
                    Intent intent2 = new Intent(Select_Activity.this, BloodPressureActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.bt_symptom:
                    Intent intent3 = new Intent(Select_Activity.this, SymptomActivity.class);
                    startActivity(intent3);
                    break;

                case R.id.bt_graph:
                    Intent intent4 = new Intent(Select_Activity.this, UserStateGraphActivity.class);
                    startActivity(intent4);
                    break;


            }
        }
    };
}
