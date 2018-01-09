package com.example.bogdanbm.finalweapontool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

/**
 * Created by BogdanBM on 1/9/2018.
 */

public class OnLongClickListenerTask implements View.OnLongClickListener {
    private static TasksPerWeapon tasksPerWeapon;
    private String weaponId;
    private String id;
    Context context;
    List<Task> tasksList;

    public OnLongClickListenerTask(TasksPerWeapon tasksPerWeapon, String weaponId, List<Task> tasksList) {
        this.tasksPerWeapon = tasksPerWeapon;
        this.weaponId = weaponId;
        this.tasksList = tasksList;
    }
    @Override
    public boolean onLongClick(final View view) {
        context = view.getContext();
        String content = view.getTag().toString();
        id = content;

        final CharSequence[] items = { "Edit", "Delete" };
        new AlertDialog.Builder(context).setTitle("This Task")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int item) {

                        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference tasks = root.child("tasks");
                        if (item == 0) {
                            editTask(id, tasks, view);
                        }
                        else if (item == 1) {

                            tasks.child(id).removeValue().addOnCompleteListener(tasksPerWeapon, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                    Toast.makeText(tasksPerWeapon, "deleteWeaponTask:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    if(!task.isSuccessful())
                                    {
                                        Toast.makeText(context, "Unable to delete weapon task.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Weapon task was deleted.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editTask(final String taskId, final DatabaseReference tasks, View view) {
        Task task = new Task();
        for(Task t: tasksList)
        {
            if(Objects.equals(t.id, taskId))
            {
                task = t;
            }
        }


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.task_input_form, null, false);

        final EditText editTextTaskDescription= (EditText) formElementsView.findViewById(R.id.editTextTaskDescription);

        editTextTaskDescription.setText(task.description);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Task")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Task newObjectTask = new Task();
                                newObjectTask.id = taskId;
                                newObjectTask.description = editTextTaskDescription.getText().toString();
                                newObjectTask.weaponId = weaponId;
                                newObjectTask.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                tasks.child(taskId).setValue(newObjectTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                        Toast.makeText(tasksPerWeapon, "updateWeaponTask:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                        if(!task.isSuccessful())
                                        {
                                            Toast.makeText(context, "Unable to update weapon task.", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Weapon task was updated.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog.cancel();
                            }

                        }).show();
    }
}
