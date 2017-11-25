package org.androidtown.healthcareguide.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.androidtown.healthcareguide.Model.BloodPressureInformation;
import org.androidtown.healthcareguide.Model.DiabetesInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserStateGraphActivity extends AppCompatActivity {


    private GraphView beforeDiabetesGraph;
    private GraphView afterDiabetesGraph;
    private GraphView bloodPressureGraph;
    public User currentUser;
    private User caredUser;
    private TextView textView;
    private List<DiabetesInformation> beforeDiabetesList;
    private List<DiabetesInformation> afterDiabetesList;
    private List<BloodPressureInformation> bloodPressureList;
    private LineGraphSeries<DataPoint> beforeDiabetesSeries;
    private LineGraphSeries<DataPoint> afterDiabetesSeries;
    private PointsGraphSeries<DataPoint> highBloodPressureSeries;
    private PointsGraphSeries<DataPoint> lowBloodPressureSeries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state_graph);

        setUsers();
        initView();
        setGraphView();
        getDiabetesListFromFirebase();
        getBPListFromFirebase();
    }

    public void getBPListFromFirebase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("blood_pressure").child(caredUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bloodPressureList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    BloodPressureInformation bpi =ds.getValue(BloodPressureInformation.class);
                    bloodPressureList.add(bpi);
                }
                DataPoint[] dataPointsHigh = new DataPoint[bloodPressureList.size()];
                DataPoint[] dataPointsLow = new DataPoint[bloodPressureList.size()];


                Date minDate = new Date();
                Date maxDate = new Date();

                int i=0;
                for(BloodPressureInformation info : bloodPressureList){
                    String date = info.getDate();
                    int year = Integer.parseInt(date.substring(0,4));
                    int month=11;
                    int day=31;

                    for(int j=5;j<date.length();j++){
                        if(date.charAt(j)=='-'){
                            month = Integer.parseInt(date.substring(5,j)) -1;
                            day = Integer.parseInt(date.substring(j+1,date.length()));
                            break;
                        }
                    }

                    Date d = new Date(year,month,day);
                    if(i==0){
                        minDate = d;
                        maxDate = d;
                    }else{
                        if(d.getTime() < minDate.getTime()){
                            minDate = d;
                        }
                        if(d.getTime() > maxDate.getTime()){
                            maxDate = d;
                        }
                    }
                    dataPointsHigh[i] = new DataPoint(d,Integer.parseInt(info.getBloodHigh()));
                    dataPointsLow[i] = new DataPoint(d,Integer.parseInt(info.getBloodLow()));
                    i++;
                }

                bloodPressureGraph.getViewport().setMinX(minDate.getTime());
                bloodPressureGraph.getViewport().setMaxX(maxDate.getTime());
                bloodPressureGraph.getViewport().setXAxisBoundsManual(true);


                highBloodPressureSeries = new PointsGraphSeries<>(dataPointsHigh);
                lowBloodPressureSeries = new PointsGraphSeries<>(dataPointsLow);

                bloodPressureGraph.addSeries(highBloodPressureSeries);
                bloodPressureGraph.addSeries(lowBloodPressureSeries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDiabetesListFromFirebase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("diabetes").child(caredUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                beforeDiabetesList.clear();
                afterDiabetesList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    DiabetesInformation di =ds.getValue(DiabetesInformation.class);
                    if(di.getEat().equals("식전")){
                        beforeDiabetesList.add(di);
                    }else{
                        afterDiabetesList.add(di);
                    }
                }
                DataPoint[] dataPointsBefore = new DataPoint[beforeDiabetesList.size()];
                DataPoint[] dataPointsAfter = new DataPoint[afterDiabetesList.size()];

                Date minDate = new Date();
                Date maxDate = new Date();


                int i=0;
                for(DiabetesInformation info : beforeDiabetesList){
                    String date = info.getDate();
                    int year = Integer.parseInt(date.substring(0,4));
                    int month=11;
                    int day=31;

                    for(int j=5;j<date.length();j++){
                        if(date.charAt(j)=='-'){
                            month = Integer.parseInt(date.substring(5,j)) -1;
                            day = Integer.parseInt(date.substring(j+1,date.length()));
                            break;
                        }
                    }

                    Date d = new Date(year,month,day);
                    if(i==0){
                        minDate = d;
                        maxDate = d;
                    }else{
                        if(d.getTime() < minDate.getTime()){
                            minDate = d;
                        }
                        if(d.getTime() > maxDate.getTime()){
                            maxDate = d;
                        }
                    }
                    dataPointsBefore[i] = new DataPoint(d,Integer.parseInt(info.getDiabetesinfo()));
                    i++;
                }

                beforeDiabetesGraph.getViewport().setMinX(minDate.getTime());
                beforeDiabetesGraph.getViewport().setMinX(maxDate.getTime());
                beforeDiabetesGraph.getViewport().setXAxisBoundsManual(true);


                i=0;
                for(DiabetesInformation info : afterDiabetesList){
                    String date = info.getDate();
                    int year = Integer.parseInt(date.substring(0,4));
                    int month=11;
                    int day=31;


                    for(int j=5;j<date.length();j++){
                        if(date.charAt(j)=='-'){
                            month = Integer.parseInt(date.substring(5,j)) -1;
                            day = Integer.parseInt(date.substring(j+1,date.length()));
                            break;
                        }
                    }

                    Date d = new Date(year,month,day);
                    if(i==0){
                        minDate = d;
                        maxDate = d;
                    }else{
                        if(d.getTime() < minDate.getTime()){
                            minDate = d;
                        }
                        if(d.getTime() > maxDate.getTime()){
                            maxDate = d;
                        }
                    }
                    dataPointsAfter[i] = new DataPoint(d,Integer.parseInt(info.getDiabetesinfo()));
                    i++;
                }

                afterDiabetesGraph.getViewport().setMinX(minDate.getTime());
                afterDiabetesGraph.getViewport().setMinX(maxDate.getTime());
                afterDiabetesGraph.getViewport().setXAxisBoundsManual(true);

                beforeDiabetesSeries = new LineGraphSeries<>(dataPointsBefore);
                afterDiabetesSeries = new LineGraphSeries<>(dataPointsAfter);


                beforeDiabetesGraph.addSeries(beforeDiabetesSeries);
                afterDiabetesGraph.addSeries(afterDiabetesSeries);
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

    public void setGraphView(){
        beforeDiabetesGraph.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(UserStateGraphActivity.this, new SimpleDateFormat("MM/dd")));
        afterDiabetesGraph.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(UserStateGraphActivity.this, new SimpleDateFormat("MM/dd")));

        beforeDiabetesGraph.getViewport().setMinY(0);
        beforeDiabetesGraph.getViewport().setMaxY(400);
        beforeDiabetesGraph.getViewport().setYAxisBoundsManual(true);
        beforeDiabetesGraph.getGridLabelRenderer().setHumanRounding(false);

        afterDiabetesGraph.getViewport().setMinY(0);
        afterDiabetesGraph.getViewport().setMaxY(400);
        afterDiabetesGraph.getViewport().setYAxisBoundsManual(true);
        afterDiabetesGraph.getGridLabelRenderer().setHumanRounding(false);

        bloodPressureGraph.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(UserStateGraphActivity.this, new SimpleDateFormat("MM/dd")));


        bloodPressureGraph.getViewport().setMinY(0);
        bloodPressureGraph.getViewport().setMaxY(200);
        bloodPressureGraph.getViewport().setYAxisBoundsManual(true);
        bloodPressureGraph.getGridLabelRenderer().setHumanRounding(false);
    }

    public void initView(){
        beforeDiabetesGraph = findViewById(R.id.before_diabetes_graph);
        afterDiabetesGraph = findViewById(R.id.after_diabetes_graph);
        bloodPressureGraph = findViewById(R.id.blood_pressure_graph);
        textView = findViewById(R.id.cared_user_name);

        textView.setText(caredUser.getName() + "의 상태 그래프");
        beforeDiabetesList = new ArrayList<>();
        afterDiabetesList = new ArrayList<>();
        bloodPressureList = new ArrayList<>();
    }
}
