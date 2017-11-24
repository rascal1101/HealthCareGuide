package org.androidtown.healthcareguide.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import org.androidtown.healthcareguide.Fragment.InputdiabetsFragment;
import org.androidtown.healthcareguide.Fragment.ResultdiabetsFragment;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;


/**
 * Created by yjhyj on 2017-11-17.
 */

public class Diabets_Activity extends AppCompatActivity {

    private InputdiabetsFragment inputdiabetsFragment;
    private ResultdiabetsFragment resultdiabetsFragment;
    private FrameLayout container;
    private Button inputbutton;
    private Button outputbutton;
    public static User caredUser;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diabets_fragment);
        setUser();
        initView(savedInstanceState);
    }

    public void setUser(){
        caredUser = Select_Activity.caredUser;
    }



    public void initView(Bundle savedInstanceState)
    {
        inputdiabetsFragment= new InputdiabetsFragment();
        resultdiabetsFragment=new ResultdiabetsFragment();
        container=findViewById(R.id.diabets_container);
        inputbutton=findViewById(R.id.inputbutton);
        outputbutton=findViewById(R.id.outputbutton);
        if(savedInstanceState==null)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.add(R.id.diabets_container,inputdiabetsFragment);
            ft.add(R.id.diabets_container,resultdiabetsFragment);
            ft.hide(resultdiabetsFragment);
            ft.show(inputdiabetsFragment);
            ft.commit();
        }

        inputbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                        .hide(resultdiabetsFragment)
                        .show(inputdiabetsFragment)
                        .commit();
            }
        });

        outputbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                        .hide(inputdiabetsFragment)
                        .show(resultdiabetsFragment)
                        .commit();
            }
        });
    }

}
