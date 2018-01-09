package com.example.bogdanbm.finalweapontool;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class TasksPerWeapon extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_per_weapon);

        Intent intent = getIntent();
        String weaponType = intent.getStringExtra("weaponType");
        String weaponId = intent.getStringExtra("weaponId");

        TextView weaponTypeTextView = (TextView) findViewById(R.id.textViewTasksForWeaponType);
        weaponTypeTextView.setText(weaponType + ": DO NOT FORGET!");

        Button buttonCreateTask = (Button) findViewById(R.id.buttonCreateNewTask);
        buttonCreateTask.setOnClickListener(new OnClickListenerCreateTask(this, weaponId, weaponType));

        readTasksPerWeaponPerUser(weaponId);
    }

    public void readTasksPerWeaponPerUser(final String weaponId) {

        DatabaseReference root  = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasks = root.child("tasks");

        final ArrayList<Task> tasksList = new ArrayList<>();
        final int count[] = {0};

        tasks.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasksList.clear();
                count[0] = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Task val = ds.getValue(Task.class);
                    if(Objects.equals(val.weaponId, weaponId) && Objects.equals(val.userId, FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        count[0]++;
                        tasksList.add(val);
                        ((UserTasksAdapter) (((ListView)findViewById(R.id.tasksListView)).getAdapter())).notifyDataSetChanged();
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(count[0]);
                stringBuilder.append(" tasks found.");
                ((TextView) findViewById(R.id.textViewNoOfTasks)).setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.tasksListView)).setAdapter(new UserTasksAdapter(tasksList, this, this));
    }
}
