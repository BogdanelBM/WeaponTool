package com.example.bogdanbm.weapontool;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by BogdanBM on 11/6/2017.
 */

public class OnClickListenerCreateTask implements View.OnClickListener {

    private static TasksPerWeapon tasksPerWeapon;
    private int weaponId;
    private String weaponType;

    public OnClickListenerCreateTask(TasksPerWeapon tasksPerWeapon, int weaponId, String weaponType) {
        this.tasksPerWeapon = tasksPerWeapon;
        this.weaponId = weaponId;
        this.weaponType = weaponType;
    }
    @Override
    public void onClick(View view) {
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.task_input_form, null, false);
        final EditText editTextTaskDescription = (EditText) formElementsView.findViewById(R.id.editTextTaskDescription);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Create New Task for " + weaponType)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String description = editTextTaskDescription.getText().toString();

                                ObjectTask task = new ObjectTask();
                                task.description = description;
                                task.weaponId = weaponId;

                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setType("text/plain");
                                intent.setData(Uri.parse("mailto:santejudean.bogdan@gmail.com?cc=santejudean.bogdan@gmail.com&subject=New Task Added For Weapon "+ weaponType +" -PETCAVE&body="+
                                        task.toString() ));
                                try {
                                    context.startActivity(intent);
                                }
                                catch(ActivityNotFoundException ex)
                                {
                                    Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                                }

                                boolean createSuccessful = new TableControllerTasks(context).create(task);
                                if(createSuccessful) {
                                    Toast.makeText(context, "New task created successfully.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Unable to create a new task.", Toast.LENGTH_SHORT).show();
                                }

                                tasksPerWeapon.countTasks(weaponId);
                                tasksPerWeapon.readTasksPerWeapon(weaponId);

                                dialogInterface.cancel();
                            }
                        }).show();
    }
}

