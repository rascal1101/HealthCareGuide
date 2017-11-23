package org.androidtown.healthcareguide.Fragment;


import android.os.Bundle;
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
import org.androidtown.healthcareguide.Adapter.DiabetesAdapter;
import org.androidtown.healthcareguide.Model.DiabetesInformation;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultdiabetsFragment extends Fragment {

    private ListView diabets_list;
    private List<DiabetesInformation> list;
    private DiabetesInformation currentDiabetesInformation;
    private DiabetesAdapter adapter;
    private User caredUser;

    public ResultdiabetsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resultdiabets_fregment, container, false);

        setUser();
        initView(view);
        initAdapter();
        getListFromFirebase();
        return view;
    }

    public void initView(View view) {
        diabets_list=view.findViewById(R.id.diabets_list);
    }

    public void setUser(){
        caredUser = Select_Activity.caredUser;
    }



    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new DiabetesAdapter(getContext(),list);
        DiabetesInformation diabetesInformation = new DiabetesInformation();
        diabets_list.setAdapter(adapter);
    }

    public void getListFromFirebase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("diabetes").child(caredUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    DiabetesInformation diabetesInformation;
                    diabetesInformation = ds.getValue(DiabetesInformation.class);
                    list.add(diabetesInformation);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
