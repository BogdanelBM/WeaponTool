package com.example.bogdanbm.weapontool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BogdanBM on 12/8/2017.
 */

public class WeaponsAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<ObjectWeapon> list;
    private Context context;
    private static MainActivity mainActivity;

    public WeaponsAdapter(ArrayList<ObjectWeapon> list, Context context, MainActivity mainActivity) {
        this.list = list;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    public void setList(ArrayList<ObjectWeapon> ll)
    {
        this.list = ll;
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
        return list.get(i).id;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.weapon_rowitem, null);
        }

        TextView itemLabel = (TextView) view.findViewById(R.id.rowItemDescTextView);
        itemLabel.setTag(Integer.toString(list.get(i).id) + " " + list.get(i).description);
        String text = list.get(i).description + " ~ " + list.get(i).type;
        itemLabel.setText(text);

        itemLabel.setOnLongClickListener(new OnLongClickListenerWeapon(mainActivity));

        Button weaponTask = (Button) view.findViewById(R.id.weaponTaskButton);

        weaponTask.setOnClickListener(new OnClickListenerTasksPerWeapon( mainActivity, new TasksPerWeapon(), list.get(i)));

        return view;
    }
}
