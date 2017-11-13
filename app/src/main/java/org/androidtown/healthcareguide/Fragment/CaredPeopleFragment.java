package org.androidtown.healthcareguide.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Activity.CarelistActivity;
import org.androidtown.healthcareguide.Adapter.CarePeopleListAdapter;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.ArrayList;
import java.util.List;

public class CaredPeopleFragment extends Fragment {

    private static ListView caredPeopleListView;
    private static List<User> list;
    private static CarePeopleListAdapter adapter;
    private String uid;
    private String email;
    private String name;
    private User currentUser;

    public CaredPeopleFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caredpeople, container, false);

        caredPeopleListView = view.findViewById(R.id.cared_people_list);
        uid = ((CarelistActivity)getActivity()).uid;

        getCurrentUser();
        initAdapter();
        getListFromFirebase();


        Toast.makeText(getContext(), "uid : " + uid, Toast.LENGTH_SHORT).show();
        return view;
    }

    public void getCurrentUser(){
        currentUser = new User();
        uid = ((CarelistActivity)getActivity()).uid;
        email = ((CarelistActivity)getActivity()).email;
        name =  ((CarelistActivity)getActivity()).name;
        currentUser = ((CarelistActivity)getActivity()).currentUser;
    }

    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new CarePeopleListAdapter(getContext());
        caredPeopleListView.setAdapter(adapter);
    }

    public void getListFromFirebase(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        dr.child("users").child(uid).child("cpgroup").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Toast.makeText(getContext(),"cpgroup email : " + ds.child("email").getValue(String.class) , Toast.LENGTH_SHORT).show();
                    String email = ds.child("email").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.getKey();
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setUid(uid);
                    list.add(user);
                    caredPeopleListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static List<User> getList() {
        return list;
    }

}
