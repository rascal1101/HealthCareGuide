package org.androidtown.healthcareguide.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import org.androidtown.healthcareguide.Activity.Select_Activity;
import org.androidtown.healthcareguide.Model.BloodPressureInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.List;

/**
 * Created by yjhyj on 2017-11-24.
 */

public class BloodPressure_Adapter extends BaseAdapter {

    Context context;
    List<BloodPressureInformation> list;
    LayoutInflater inflater;
    User caredUser;


    public BloodPressure_Adapter(Context context, List<BloodPressureInformation> list) {
        this.context = context;
        this.list = list;
        caredUser = Select_Activity.caredUser;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            view = inflater.inflate(R.layout.adapter_blood_pressure, parent, false);
        }else{
            view = convertView;
        }

        BloodPressureInformation bloodPressureInformation = list.get(position);

        String date = bloodPressureInformation.getDate();
        String time = bloodPressureInformation.getTime();
        String bloodhigh = bloodPressureInformation.getBloodHigh();
        String bloodlow=bloodPressureInformation.getBloodLow();
        final String key = bloodPressureInformation.getKey();

        TextView itemDateTime2 = view.findViewById(R.id.item_datetime2);
        TextView itemBloodhigh = view.findViewById(R.id.item_high);
        TextView itemBloodlow = view.findViewById(R.id.item_low);

        itemDateTime2.setText(date + " " + time);
        itemBloodhigh.setText(bloodhigh+"/");
        itemBloodlow.setText(bloodlow);

        LinearLayout layout = view.findViewById(R.id.blood_pressure_layout);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog dialog = createDialogBox(key);
                dialog.show();
                return false;
            }
        });

        return view;
    }

    private AlertDialog createDialogBox(final String key){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //builder.setTitle("내용 삭제");
        builder.setMessage("내용을 삭제하시겠습니까?");


        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int whichButton){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("blood_pressure").child(caredUser.getUid()).child(key).removeValue();
                dialog.dismiss();
            }

        });


        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int whichButton){
                dialog.dismiss();
            }

        });



        AlertDialog dialog = builder.create();

        return dialog;
    }
}
