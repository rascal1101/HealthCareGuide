package org.androidtown.healthcareguide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidtown.healthcareguide.Activity.Select_Activity;
import org.androidtown.healthcareguide.Model.User;
import org.androidtown.healthcareguide.R;

import java.util.List;

/**
 * Created by MSI on 2017-11-13.
 */

public class CarePeopleListAdapter extends BaseAdapter {
    Context context;
    List<User> list;
    LayoutInflater inflater;
    User currentUser;

    public CarePeopleListAdapter(Context context, List<User> list, User currentUser) {
        this.context = context;
        this.list = list;
        this.currentUser = currentUser;
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
            view = inflater.inflate(R.layout.adapter_care_people_list, parent, false);
        }else{
            view = convertView;
        }


        User user = list.get(position);
        String name = user.getName();
        String email = user.getEmail();

        TextView carePeopleName = view.findViewById(R.id.care_people_name);
        TextView carePeopleEmail = view.findViewById(R.id.care_people_email);

        carePeopleName.setText(name);
        carePeopleEmail.setText(email);

        RelativeLayout layout = view.findViewById(R.id.care_people_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callIntent(position);
            }
        });


        return view;
    }

    public void callIntent(int position){
        User selectedUser = list.get(position);

        Intent intent = new Intent(context, Select_Activity.class);
        intent.putExtra("name",selectedUser.getName());
        intent.putExtra("email",selectedUser.getEmail());
        intent.putExtra("uid",selectedUser.getUid());
        context.startActivity(intent);
    }
}
