package org.androidtown.healthcareguide.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidtown.healthcareguide.Activity.Select_Activity;
import org.androidtown.healthcareguide.Dialog.DateTimeDialog2;
import org.androidtown.healthcareguide.Model.BloodPressureInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

/**
 * Created by yjhyj on 2017-11-24.
 */

public class BloodPressureFragment1 extends Fragment {

    private Button bt_dates;
    private TextView tv_dates;
    private EditText et_high;
    private EditText et_low;
    private RadioGroup rg_eats;
    private RadioButton rb_eatbefore;
    private RadioButton rb_eatafter;
    private Button bt_saves;
    public static String date;
    public static String time;
    private User caredUser;
    private User currentUser;
//private User caredUser;

    private BloodPressureFragment1 BloodPressureFragment1;

    public BloodPressureFragment1() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blood_pressure_frag1, container, false);
        setUser();
        initView(view);
        return view;
    }
    public void setUser(){
        currentUser = Select_Activity.currentUser;
        caredUser = Select_Activity.caredUser;
    }

    public void initView(View view) {
        bt_dates=view.findViewById(R.id.bt_dates);
        tv_dates=view.findViewById(R.id.tv_dates);
        et_high=view.findViewById(R.id.et_high);
        et_low=view.findViewById(R.id.et_low);
        bt_saves=view.findViewById(R.id.bt_saves);
        rg_eats=view.findViewById(R.id.rg_eats);
        rb_eatbefore=view.findViewById(R.id.rb_eatbefore);
        rb_eatafter=view.findViewById(R.id.rb_eatafter);

        bt_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeDialog2 dialog =new DateTimeDialog2(getContext(), tv_dates);
                dialog.show();
            }
        });
        bt_saves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bloodHigh =et_high.getText().toString();
                String bloodLow =et_low.getText().toString();
                String eats;
                if(rb_eatbefore.isChecked()){
                    eats = rb_eatbefore.getText().toString();
                }else{
                    eats = rb_eatafter.getText().toString();
                }

                BloodPressureInformation bi = new BloodPressureInformation();
                bi.setDate(date);
                bi.setTime(time);
                bi.setBloodHigh(bloodHigh);
                bi.setBloodLow(bloodLow);
                bi.setEat(eats);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dr = firebaseDatabase.getReference().child("blood_pressure").child(caredUser.getUid()).push();
                bi.setKey(dr.getKey());
                dr.setValue(bi)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "저장 성공", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public static void setD(String d) {
        date = d;
    }

    public static void setT(String t) {
        time = t;
    }
}

