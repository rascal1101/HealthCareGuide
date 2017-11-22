package org.androidtown.healthcareguide.Activity;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.androidtown.healthcareguide.Adapter.SymptomListAdapter;
import org.androidtown.healthcareguide.Model.Symptom;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserStateGraphActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 10;
    private GraphView graph;
    public User currentUser;
    private SimpleDateFormat sdf;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imagePath;
    private User caredUser;
    private Symptom symptom;
    private ListView listView;
    private SymptomListAdapter adapter;
    private List<Symptom> list;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state_graph);

        setUsers();
        initView();
        initAdapter();
        getListFromFirebase();
    }

    public void getListFromFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("symptom").child(caredUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Symptom symptom;
                    symptom = ds.getValue(Symptom.class);
                    list.add(symptom);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    public void setUsers(){
        Intent intent = getIntent();
        caredUser = new User();
        caredUser.setEmail(intent.getStringExtra("caredUserEmail"));
        caredUser.setName(intent.getStringExtra("caredUserName"));
        caredUser.setUid(intent.getStringExtra("caredUserUid"));
        currentUser = CarelistForDoctorActivity.currentUser;
    }

    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new SymptomListAdapter(getApplicationContext(), list, caredUser);
        listView.setAdapter(adapter);
    }

    public void initView(){
        graph = findViewById(R.id.graph);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        listView = findViewById(R.id.symptom_list);
        textView = findViewById(R.id.cared_user_name);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == GALLERY_CODE){
            if(data!=null) {
                imagePath = getPath(data.getData());
                upload(imagePath);
            }
        }
    }

    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj, null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    public void upload(String uri){

        Uri file = Uri.fromFile(new File(uri));
        symptom = new Symptom();
        symptom.setContent("sympton test");
        symptom.setUid(caredUser.getUid());
        StorageReference riversRef = storageRef.child("images/symptom/"+caredUser.getUid()+"/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                symptom.setImageUrl(downloadUrl.toString());

                firebaseDatabase.getReference().child("symptom").child(caredUser.getUid())
                        .push().setValue(symptom);

            }
        });
    }

}
