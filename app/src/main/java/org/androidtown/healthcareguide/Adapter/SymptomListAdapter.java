package org.androidtown.healthcareguide.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.androidtown.healthcareguide.Model.Symptom;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.List;

/**
 * Created by MSI on 2017-11-13.
 */

public class SymptomListAdapter extends BaseAdapter {
    Context context;
    List<Symptom> list;
    LayoutInflater inflater;
    User caredUser;

    public SymptomListAdapter(Context context, List<Symptom> list, User caredUser) {
        this.context = context;
        this.list = list;
        this.caredUser = caredUser;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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
            view = inflater.inflate(R.layout.adapter_symptom_list, parent, false);
        }else{
            view = convertView;
        }


        Symptom symptom = list.get(position);
        String content = symptom.getContent();
        String imageUrl = symptom.getImageUrl();
        final String key = symptom.getKey();
        final String imageName = symptom.getImageName();

        ImageView imageView = view.findViewById(R.id.symptom_image);
        TextView textView = view.findViewById(R.id.symptom_content);

        if(content==null){
            Glide.with(context).load(imageUrl).into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }else{
            textView.setText(content);
            textView.setVisibility(View.VISIBLE);
        }

        RelativeLayout layout = view.findViewById(R.id.symptom_layout);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog dialog = createDialogBox(key, imageName);
                dialog.show();
                return false;
            }
        });

        return view;
    }

    private AlertDialog createDialogBox(final String key,final String imageName){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //builder.setTitle("내용 삭제");
        builder.setMessage("내용을 삭제하시겠습니까?");


        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int whichButton){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("symptom").child(caredUser.getUid()).child(key).removeValue();
                if(imageName!=null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    storage.getReference().child("images").child("symptom").child(caredUser.getUid()).child(imageName).delete();
                }
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
