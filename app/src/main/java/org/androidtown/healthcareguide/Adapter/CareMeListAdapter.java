package org.androidtown.healthcareguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.List;

/**
 * Created by MSI on 2017-11-13.
 */

public class CareMeListAdapter extends BaseAdapter {
    Context context;
    List<User> list;
    LayoutInflater inflater;
    FirebaseDatabase firebaseDatabase;
    User currentUser;


    public CareMeListAdapter(Context context, List<User> list, User currentUser) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        this.currentUser = currentUser;
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
            view = inflater.inflate(R.layout.adapter_care_me_list, parent, false);
        }else{
            view = convertView;
        }
        User user = list.get(position);
        String name = user.getName();
        String email = user.getEmail();

        TextView careMeName = view.findViewById(R.id.care_me_name);
        TextView careMeEmail = view.findViewById(R.id.care_me_email);
        Button button = view.findViewById(R.id.care_me_delete);

        careMeName.setText(name);
        careMeEmail.setText(email);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteList(position);
            }
        });

        return view;
    }
    public void deleteList(final int position){
        firebaseDatabase.getReference().child("users").child(currentUser.getUid()).child("cmgroup").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String email = ds.child("email").getValue(String.class);
                    if(list.get(position).getEmail().equals(email)){
                        String uid2 = ds.getKey();
                        FirebaseDatabase fd = FirebaseDatabase.getInstance();
                        fd.getReference().child("users").child(currentUser.getUid()).child("cmgroup").child(uid2).removeValue();
                        fd.getReference().child("users").child(uid2).child("cpgroup").child(currentUser.getUid()).removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
