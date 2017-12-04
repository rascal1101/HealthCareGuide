package org.androidtown.healthcareguide.Activity;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.androidtown.healthcareguide.Adapter.SymptomListAdapter;
import org.androidtown.healthcareguide.Model.Symptom;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SymptomActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView;
    private User caredUser;
    private List<Symptom> list;
    private SymptomListAdapter adapter;
    private Symptom symptom;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String imagePath;
    private static final int GALLERY_CODE = 10;
    private Button photo;
    private Button send;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        setUser();
        initView();
        initAdapter();
        getListFromFirebase();
    }

    public void setUser(){
        caredUser = Select_Activity.caredUser;
    }

    public void initView(){
        getSupportActionBar().hide();
        listView = findViewById(R.id.symptom_list);
        textView = findViewById(R.id.symptom_user_name);
        photo = findViewById(R.id.photo);
        send = findViewById(R.id.send);
        content = findViewById(R.id.content);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        textView.setText(caredUser.getName() + "의 증상기록");


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = content.getText().toString();
                if(s.equals(""))
                    return;
                symptom = new Symptom();
                symptom.setContent(s);
                content.setText("");
                DatabaseReference dr = Select_Activity.drSymptom.child(caredUser.getUid()).push();
                symptom.setKey(dr.getKey());
                dr.setValue(symptom);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });




    }
    public void initAdapter(){
        list = new ArrayList<>();
        adapter = new SymptomListAdapter(SymptomActivity.this, list, caredUser);
        listView.setAdapter(adapter);
    }

    public void getListFromFirebase(){
        Select_Activity.drSymptom.child(caredUser.getUid())
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
        StorageReference riversRef = storageRef.child("images/symptom/"+caredUser.getUid()+"/"+file.getLastPathSegment());
        symptom.setImageName(file.getLastPathSegment());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap src = BitmapFactory.decodeFile(imagePath, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageByte = stream.toByteArray();

        UploadTask uploadTask = riversRef.putBytes(imageByte);
        //UploadTask uploadTask = riversRef.putFile(file);

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

                DatabaseReference dr = firebaseDatabase.getReference().child("symptom").child(caredUser.getUid()).push();
                symptom.setKey(dr.getKey());
                dr.setValue(symptom);
            }
        });
    }

    public void getImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }
}
