package org.androidtown.healthcareguide.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    private ListView caredPeopleListView;
    private List<User> list;
    private CarePeopleListAdapter adapter;
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

        setCurrentUser();
        initView(view);
        initAdapter();
        getListFromFirebase();

        return view;
    }

    public void setCurrentUser(){
        currentUser = ((CarelistActivity)getActivity()).currentUser;
    }

    public void initView(View view){
        caredPeopleListView = view.findViewById(R.id.cared_people_list);
    }

    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new CarePeopleListAdapter(getContext(),list, currentUser);
        caredPeopleListView.setAdapter(adapter);
    }

    public void getListFromFirebase(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        dr.child("users").child(currentUser.getUid()).child("cpgroup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String email = ds.child("email").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.getKey();
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setUid(uid);
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
