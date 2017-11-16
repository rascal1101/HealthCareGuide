package org.androidtown.healthcareguide.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Activity.CarelistActivity;
import org.androidtown.healthcareguide.Adapter.CareMeListAdapter;
import org.androidtown.healthcareguide.Dialog.AddCaringMeDialog;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CareMeFragment extends Fragment {

    private Button addCaringMeButton;
    private ListView careMeListView;
    private List<User> list;
    private User currentUser;
    private CareMeListAdapter adapter;

    public CareMeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_careme, container, false);

        setCurrentUser();
        initView(view);
        initAdapter();
        getListFromFirebase();

        return view;
    }

    public void initView(View view){
        addCaringMeButton = view.findViewById(R.id.add_caring_me_button);
        careMeListView = view.findViewById(R.id.care_me_list);
        addCaringMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCaringMeDialog dialog = new AddCaringMeDialog(getContext(), currentUser, list);
                dialog.show();
            }
        });
    }

    public void setCurrentUser(){
        currentUser = new User();
        currentUser = ((CarelistActivity)getActivity()).currentUser;
    }

    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new CareMeListAdapter(getContext(),list);
        careMeListView.setAdapter(adapter);
    }

    public void getListFromFirebase(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        dr.child("users").child(currentUser.getUid()).child("cmgroup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String email = ds.child("email").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
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
