package org.androidtown.healthcareguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.androidtown.healthcareguide.Model.DiabetesInformation;
import org.androidtown.healthcareguide.R;

import java.util.List;

/**
 * Created by yjhyj on 2017-11-21.
 */

public class DiabetesAdapter extends BaseAdapter {
    Context context;
    List<DiabetesInformation> list;
    LayoutInflater inflater;

    public DiabetesAdapter(Context context, List<DiabetesInformation> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            view = inflater.inflate(R.layout.adapter_diabetes, parent, false);
        }else{
            view = convertView;
        }

        DiabetesInformation diabetesInformation = list.get(position);

        String date = diabetesInformation.getDate();
        String time = diabetesInformation.getTime();
        String eat = diabetesInformation.getEat();
        String diabetesinfo = diabetesInformation.getDiabetesinfo();

        TextView itemDateTime = view.findViewById(R.id.item_datetime);
        TextView itemEat = view.findViewById(R.id.item_eat);
        TextView itemDiabetesinfo = view.findViewById(R.id.item_diabetesinfo);

        itemDateTime.setText(date + " " + time);
        itemEat.setText(eat);
        itemDiabetesinfo.setText(diabetesinfo);

        return view;
    }
}
