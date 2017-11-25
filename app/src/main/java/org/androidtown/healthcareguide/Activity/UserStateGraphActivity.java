package org.androidtown.healthcareguide.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.androidtown.healthcareguide.Model.DiabetesInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserStateGraphActivity extends AppCompatActivity {


    private GraphView graph;
    public User currentUser;
    private SimpleDateFormat sdf;
    private User caredUser;
    private TextView textView;
    private List<DiabetesInformation> list;
    private LineGraphSeries<DataPoint> diabetesSeries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state_graph);

        setUsers();
        initView();
        getListFromFirebase();
    }

    public void getListFromFirebase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("diabetes").child(caredUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    list.add(ds.getValue(DiabetesInformation.class));
                }
                DataPoint[] dataPoints = new DataPoint[list.size()];

                int i=0;
                for(DiabetesInformation info : list){
                    String date = info.getDate();
                    int year = Integer.parseInt(date.substring(0,4));
                    int month=12;
                    int day=31;

                    for(int j=5;j<date.length();j++){
                        if(date.charAt(j)=='-'){
                            month = Integer.parseInt(date.substring(5,j));
                            day = Integer.parseInt(date.substring(j+1,date.length()));
                        }
                    }

                    Date d = new Date(year,month,day);
                    dataPoints[i] = new DataPoint(d,Integer.parseInt(info.getDiabetesinfo()));
                    i++;
                }

                diabetesSeries = new LineGraphSeries<>(dataPoints);
                graph.addSeries(diabetesSeries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUsers(){
        caredUser = Select_Activity.caredUser;
        currentUser = Select_Activity.currentUser;
    }

    public void initView(){
        graph = findViewById(R.id.graph);
        textView = findViewById(R.id.cared_user_name);
        textView.setText(caredUser.getName());
        list = new ArrayList<>();


        sdf = new SimpleDateFormat("MM/dd");
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long)value));
                }else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(400);
        graph.getViewport().setYAxisBoundsManual(true);


    }



}
