package org.androidtown.healthcareguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

    public CareMeListAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
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

        return view;
    }
}
