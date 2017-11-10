package com.example.bogdanbm.weapontool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BogdanBM on 11/5/2017.
 */

public class TasksPerWeapon extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_per_weapon);

        Intent intent = getIntent();
        String weaponType = intent.getStringExtra("weaponType");
        int weaponId = intent.getIntExtra("weaponId", 0);

        TextView weaponTypeTextView = (TextView) findViewById(R.id.textViewTasksForWeaponType);
        weaponTypeTextView.setText(weaponType + " TO-DOs ");

        Button buttonCreateTask = (Button) findViewById(R.id.buttonCreateNewTask);
        buttonCreateTask.setOnClickListener(new OnClickListenerCreateTask(this, weaponId, weaponType));

        countTasks(weaponId);
        readTasksPerWeapon(weaponId);
    }

    public void countTasks(int weaponId) {
        int tasksCount = new TableControllerTasks(this).count(weaponId);
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewNoOfTasks);
        textViewRecordCount.setText(tasksCount + " records found.");
    }

    public void readTasksPerWeapon(int weaponId) {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutTasks);
        linearLayoutRecords.removeAllViews();

        List<ObjectTask> tasks = new TableControllerTasks(this).read(weaponId);

        if (tasks.size() > 0) {

            for (ObjectTask obj : tasks) {

                int id = obj.id;
                String description = obj.description;

                String textViewContents = description;

                TextView textViewWeaponItem= new TextView(this);
                textViewWeaponItem.setPadding(0, 10, 0, 10);
                textViewWeaponItem.setText(textViewContents);
                textViewWeaponItem.setTag(Integer.toString(id));
                textViewWeaponItem.setOnLongClickListener(new OnLongClickListenerTask(this, weaponId));
                linearLayoutRecords.addView(textViewWeaponItem);
            }

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No tasks yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}