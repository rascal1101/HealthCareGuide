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
    private static ListView careMeListView;
    private static List<User> list;
    private String uid;
    private String email;
    private String name;
    private User currentUser;
    private static CareMeListAdapter adapter;

    public CareMeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_careme, container, false);

        addCaringMeButton = view.findViewById(R.id.add_caring_me_button);
        careMeListView = view.findViewById(R.id.care_me_list);

        getCurrentUser();
        initAdapter();
        getListFromFirebase();

        addCaringMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCaringMeDialog dialog = new AddCaringMeDialog(getContext(), currentUser, list);
                dialog.show();
            }
        });


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
        adapter = new CareMeListAdapter(getContext());
        careMeListView.setAdapter(adapter);
    }

    public void getListFromFirebase(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        dr.child("users").child(uid).child("cmgroup").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Toast.makeText(getContext(),ds.child("email").getValue(String.class) , Toast.LENGTH_SHORT).show();
                    String email = ds.child("email").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    list.add(user);
                    careMeListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static ListView getCareMeListView() {
        return careMeListView;
    }

    public static CareMeListAdapter getCareMeListAdapter(){
        return adapter;
    }

    public static List<User> getList(){
        return list;
    }
}
