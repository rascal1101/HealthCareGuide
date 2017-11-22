package org.androidtown.healthcareguide.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserStateGraphActivity extends AppCompatActivity {

    private GraphView graph;
    public User currentUser;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state_graph);

        setCurrentUser();
        initView();
    }

    public void setCurrentUser(){
        currentUser = CarelistForDoctorActivity.currentUser;
    }

    public void initView(){
        graph = findViewById(R.id.graph);

        sdf = new SimpleDateFormat("MM/dd");
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{

                new DataPoint(new Date(2017,11,20), 120),
                new DataPoint(new Date(2017,11,21), 130),
                new DataPoint(new Date(2017,11,22), 125),
                new DataPoint(new Date(2017,11,23), 123),
                new DataPoint(new Date(2017,11,24), 124)
        });




        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(new Date(2017,11,20), 90),
                new DataPoint(new Date(2017,11,21), 100),
                new DataPoint(new Date(2017,11,22), 95),
                new DataPoint(new Date(2017,11,23), 92),
                new DataPoint(new Date(2017,11,24), 90)
        });
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
        graph.addSeries(series);
        graph.addSeries(series2);

    }
}
