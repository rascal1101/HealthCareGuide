package org.androidtown.healthcareguide.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
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

        careMeName.setText(name);
        careMeEmail.setText(email);

        RelativeLayout layout = view.findViewById(R.id.care_me_layout);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog dialog = createDialogBox(position);
                dialog.show();
                return false;
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

    private AlertDialog createDialogBox(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //builder.setTitle("내용 삭제");
        builder.setMessage("내용을 삭제하시겠습니까?");


        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int whichButton){
                deleteList(position);
                dialog.dismiss();
            }

        });


        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int whichButton){
                dialog.dismiss();
            }

        });



        AlertDialog dialog = builder.create();

        return dialog;
    }

}
