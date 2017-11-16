package org.androidtown.healthcareguide.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.List;

/**
 * Created by MSI on 2017-11-10.
 */

public class AddCaringMeDialog extends Dialog {

    private TextView caringMeOK;
    private Context context;
    private EditText caringMeEmail;
    private EditText caringMeName;
    private DatabaseReference databaseReference;
    private String uid;
    private String uid2;
    private User currentUser;
    private List<User> list;
    private ArrayAdapter<String> adapter;

    public AddCaringMeDialog(@NonNull Context context, User user, List<User> list) {
        super(context);
        this.context = context;
        this.currentUser = user;
        this.uid = user.getUid();
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_caring_me);


        caringMeEmail = findViewById(R.id.caring_me_email);
        caringMeName = findViewById(R.id.caring_me_name);

        caringMeOK = findViewById(R.id.caring_me_ok);
        caringMeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = caringMeName.getText().toString();
                final String email = caringMeEmail.getText().toString();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isSuccessed = false;
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            if (ds.child("email").getValue().equals(email)){
                                uid2 = ds.getKey();
                                DatabaseReference cmgroupRef = databaseReference.child(uid).child("cmgroup").child(uid2);
                                User cmu = new User();
                                cmu.setEmail(email);
                                cmu.setName(name);
                                cmgroupRef.setValue(cmu);

                                DatabaseReference cpgroupRef = databaseReference.child(uid2).child("cpgroup").child(uid);
                                User user1 = new User();
                                user1.setName(currentUser.getName());
                                user1.setEmail(currentUser.getEmail());
                                cpgroupRef.setValue(user1);
                                isSuccessed = true;
                                break;
                            }
                        }
                        if(!isSuccessed) {
                            Toast.makeText(context, email + "가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dismiss();
            }
        });
    }
}
