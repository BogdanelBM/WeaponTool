package com.example.bogdanbm.finalweapontool;

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
 * Created by BogdanBM on 1/8/2018.
 */

public class AdminWeaponsAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Weapon> list;
    private Context context;
    private static AdminActivity adminActivity;

    public AdminWeaponsAdapter(ArrayList<Weapon> list, Context context, AdminActivity adminActivity) {
        this.list = list;
        this.context = context;
        this.adminActivity = adminActivity;
    }

    public void setList(ArrayList<Weapon> ll)
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
        return 0;
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
        itemLabel.setTag(list.get(i).id + " " + list.get(i).description);
        String text = list.get(i).description + " ~ " + list.get(i).type;
        itemLabel.setText(text);

        itemLabel.setOnLongClickListener(new OnLongClickListenerWeaponAdmin(adminActivity, list));

        return view;
    }
}
