package org.androidtown.healthcareguide.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Activity.Select_Activity;
import org.androidtown.healthcareguide.Adapter.BloodPressure_Adapter;
import org.androidtown.healthcareguide.Model.BloodPressureInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjhyj on 2017-11-24.
 */

public class BloodPressureFragment2 extends Fragment {

    private ListView blood_list;
    private List<BloodPressureInformation> list;
    private BloodPressure_Adapter adapter;
    private User currentUser;
    private User caredUser;


    public BloodPressureFragment2(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blood_pressure_frag2, container, false);

        setUser();
        initView(view);
        initAdapter();
        getListFromFirebase();
        return view;
    }

    public void setUser(){
        currentUser = Select_Activity.currentUser;
        caredUser = Select_Activity.caredUser;
    }

    public void initView(View view) {
        blood_list=view.findViewById(R.id.blood_list);
    }

    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new BloodPressure_Adapter(getContext(),list);
        blood_list.setAdapter(adapter);
    }

   public void getListFromFirebase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("blood_pressure").child(caredUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    BloodPressureInformation bloodPressureInformation;
                    bloodPressureInformation = ds.getValue(BloodPressureInformation.class);
                    list.add(bloodPressureInformation);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
