package com.example.bogdanbm.weapontool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by BogdanBM on 11/6/2017.
 */

public class OnLongClickListenerTask implements View.OnLongClickListener {
    private static TasksPerWeapon tasksPerWeapon;
    private int weaponId;
    private int id;
    Context context;

    public OnLongClickListenerTask(TasksPerWeapon tasksPerWeapon, int weaponId) {
        this.tasksPerWeapon = tasksPerWeapon;
        this.weaponId = weaponId;
    }
    @Override
    public boolean onLongClick(View view) {
        context = view.getContext();
        String content = view.getTag().toString();
        id = Integer.parseInt(content);

        final CharSequence[] items = { "Edit", "Delete" };
        new AlertDialog.Builder(context).setTitle("This Task")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            editTask(id);
                        }
                        else if (item == 1) {

                            boolean deleteSuccessful = new TableControllerTasks(context).delete(id);

                            if (deleteSuccessful){
                                Toast.makeText(context, "Weapon task was deleted.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to delete weapon task.", Toast.LENGTH_SHORT).show();
                            }

                            tasksPerWeapon.countTasks(weaponId);
                            tasksPerWeapon.readTasksPerWeapon(weaponId);

                        }
                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    public void editTask(final int taskId) {
        final TableControllerTasks tableControllerTasks = new TableControllerTasks(context);
        final ObjectTask objectTask = tableControllerTasks.readByID(id);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.task_input_form, null, false);

        final EditText editTextTaskDescription= (EditText) formElementsView.findViewById(R.id.editTextTaskDescription);

        editTextTaskDescription.setText(objectTask.description);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Task")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ObjectTask newObjectTask = new ObjectTask();
                                newObjectTask.id = taskId;
                                newObjectTask.description = editTextTaskDescription.getText().toString();
                                newObjectTask.weaponId = weaponId;

                                boolean updateSuccessful = tableControllerTasks.update(newObjectTask);

                                if(updateSuccessful){
                                    Toast.makeText(context, "Weapon task was updated.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Unable to update weapon task.", Toast.LENGTH_SHORT).show();
                                }

                                tasksPerWeapon.countTasks(weaponId);
                                tasksPerWeapon.readTasksPerWeapon(weaponId);

                                dialog.cancel();
                            }

                        }).show();
    }
}
