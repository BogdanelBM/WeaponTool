package com.example.bogdanbm.weapontool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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
        weaponTypeTextView.setText(weaponType + ": DO NOT FORGET!");

        Button buttonCreateTask = (Button) findViewById(R.id.buttonCreateNewTask);
        buttonCreateTask.setOnClickListener(new OnClickListenerCreateTask(this, weaponId, weaponType));

        countTasks(weaponId);
        final ListView taskList = (ListView) findViewById(R.id.tasksListView);

        List<ObjectTask> tasks = new TableControllerTasks(this).read(weaponId);
        final ArrayList<ObjectTask> array = new ArrayList<>(tasks);

        TasksAdapter adapter = new TasksAdapter(array, this, this);
        taskList.setAdapter(adapter);
    }

    public void countTasks(int weaponId) {
        int tasksCount = new TableControllerTasks(this).count(weaponId);
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewNoOfTasks);
        textViewRecordCount.setText(tasksCount + " records found.");
    }

    public void readTasksPerWeapon(int weaponId) {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutTasks);

        List<ObjectTask> tasks = new TableControllerTasks(this).read(weaponId);

        if (tasks.size() > 0) {

            final ListView taskList = (ListView) findViewById(R.id.tasksListView);

            final ArrayList<ObjectTask> array = new ArrayList<>(tasks);

            TasksAdapter adapter = new TasksAdapter(array, this, this);
            taskList.setAdapter(adapter);

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No tasks yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
