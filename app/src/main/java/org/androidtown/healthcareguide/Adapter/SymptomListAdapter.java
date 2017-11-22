package org.androidtown.healthcareguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

        ImageView imageView = view.findViewById(R.id.symptom_image);
        TextView textView = view.findViewById(R.id.symptom_content);

        Glide.with(context).load(imageUrl).into(imageView);
        textView.setText(content);


        return view;
    }
}
