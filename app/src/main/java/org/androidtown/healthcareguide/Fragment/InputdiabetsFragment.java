package org.androidtown.healthcareguide.Fragment;


import android.os.Bundle;
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

import com.google.firebase.database.FirebaseDatabase;

import org.androidtown.healthcareguide.Activity.Select_Activity;
import org.androidtown.healthcareguide.Dialog.DateTimeDialog;
import org.androidtown.healthcareguide.Model.DiabetesInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputdiabetsFragment extends Fragment {


    private Button bt_date;
    private TextView tv_date;
    private EditText et_diabets;
    private Button bt_save;
    private RadioGroup rg_eat;
    private RadioButton rb_before;
    private RadioButton rb_after;
    private RadioButton rb_eat;
    private User caredUser;

    private ResultdiabetsFragment resultdiabetsFragment;

    public InputdiabetsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.inputdiabets_fregment,container,false);
        setUser();
        initView(view);
        return view;
    }

    public void setUser(){
        caredUser = Select_Activity.caredUser;
    }


    public void initView(View view){
        bt_date=view.findViewById(R.id.bt_date);
        tv_date=view.findViewById(R.id.tv_date);
        et_diabets=view.findViewById(R.id.et_inputdiabet);
        bt_save=view.findViewById(R.id.bt_save);
        rg_eat=view.findViewById(R.id.rg_eat);
        rb_before=view.findViewById(R.id.rb_before);
        rb_after=view.findViewById(R.id.rb_after);



        bt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeDialog dialog =new DateTimeDialog(getContext(), tv_date);
                dialog.show();
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String datetime =tv_date.getText().toString();
                String diabetesinfo =et_diabets.getText().toString();
                String eat;
                if(rb_before.isChecked()){
                    eat = rb_before.getText().toString();
                }else{
                    eat = rb_after.getText().toString();
                }
                //리스트에 들어갈 날짜와시간,식전식후여부,혈당을 저장버튼을 누를시 데이터베이스에 넣고싶어요..ㅠㅠ

                DiabetesInformation di = new DiabetesInformation();
                di.setDatetime(datetime);
                di.setDiabetesinfo(diabetesinfo);
                di.setEat(eat);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("diabetes").child(caredUser.getUid()).push().setValue(di);
                Toast.makeText(getContext(), "저장 성공", Toast.LENGTH_SHORT).show();
            }
        });


    }




}
