package com.example.bogdanbm.finalweapontool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BogdanBM on 1/9/2018.
 */

public class UserTasksAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Task> list;
    private Context context;
    private static TasksPerWeapon tasksPerWeapon;

    public UserTasksAdapter(ArrayList<Task> list, Context context, TasksPerWeapon tasksPerWeapon) {
        this.list = list;
        this.context = context;
        this.tasksPerWeapon = tasksPerWeapon;
    }

    public void setList(ArrayList<Task> ll)
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
            view = inflater.inflate(R.layout.task_rowitem, null);
        }

        TextView itemLabel = (TextView) view.findViewById(R.id.taskDescTextView);
        itemLabel.setTag(list.get(i).id);
        String text = list.get(i).description;
        itemLabel.setText(text);

        itemLabel.setOnLongClickListener(new OnLongClickListenerTask(tasksPerWeapon, list.get(i).weaponId, list));

        return view;
    }
}
