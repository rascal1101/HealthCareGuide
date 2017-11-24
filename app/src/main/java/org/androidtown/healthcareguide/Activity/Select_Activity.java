package org.androidtown.healthcareguide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

/**
 * Created by yjhyj on 2017-11-19.
 */

public class Select_Activity extends AppCompatActivity {

    public static User caredUser;
    public static User currentUser;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);

        setUser();
        initView();
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
