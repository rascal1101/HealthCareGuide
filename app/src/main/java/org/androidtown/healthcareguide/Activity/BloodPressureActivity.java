package org.androidtown.healthcareguide.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import org.androidtown.healthcareguide.Fragment.BloodPressureFragment1;
import org.androidtown.healthcareguide.Fragment.BloodPressureFragment2;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;


public class BloodPressureActivity extends AppCompatActivity {

    private BloodPressureFragment1 bloodPressureFragment1;
    private BloodPressureFragment2 bloodPressureFragment2;
    private FrameLayout container;
    private Button inputblood;
    private Button outputblood;
    private User caredUser;
    private User currentUser;
    //public static User caredUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
        setUser();
        initView(savedInstanceState);
    }

    public void setUser(){
        currentUser = Select_Activity.currentUser;
        caredUser = Select_Activity.caredUser;
    }



    public void initView(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        bloodPressureFragment1 = new BloodPressureFragment1();
        bloodPressureFragment2 =new BloodPressureFragment2();
        container= findViewById(R.id.blood_container);
        inputblood= findViewById(R.id.inputblood);
        outputblood= findViewById(R.id.outputblood);
        if(savedInstanceState==null)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.add(R.id.blood_container, bloodPressureFragment1);
            ft.add(R.id.blood_container, bloodPressureFragment2);
            ft.hide(bloodPressureFragment2);
            ft.show(bloodPressureFragment1);
            ft.commit();
        }

        inputblood.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                        .hide(bloodPressureFragment2)
                        .show(bloodPressureFragment1)
                        .commit();
            }
        });

        outputblood.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                        .hide(bloodPressureFragment1)
                        .show(bloodPressureFragment2)
                        .commit();
            }
        });
    }

}