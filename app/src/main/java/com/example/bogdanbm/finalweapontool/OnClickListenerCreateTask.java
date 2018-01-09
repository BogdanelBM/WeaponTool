package com.example.bogdanbm.finalweapontool;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by BogdanBM on 1/9/2018.
 */

public class OnClickListenerCreateTask implements View.OnClickListener{
    private static TasksPerWeapon tasksPerWeapon;
    private String weaponId;
    private String weaponType;

    public OnClickListenerCreateTask(TasksPerWeapon tasksPerWeapon, String weaponId, String weaponType) {
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

                                Task task = new Task();
                                task.description = description;
                                task.weaponId = weaponId;
                                task.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference tasks = root.child("tasks");

                                String id = tasks.push().getKey();
                                task.id = id;

                                tasks.child(id).setValue(task).addOnCompleteListener(tasksPerWeapon, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                        Toast.makeText(tasksPerWeapon, "createTask:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                        if(!task.isSuccessful())
                                        {
                                            Toast.makeText(context, "Unable to create a new task.", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                                            intent.setType("text/plain");
                                            intent.setData(Uri.parse("mailto:"+ FirebaseAuth.getInstance().getCurrentUser().getEmail()+"?cc="+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"&subject=New Task Added For Weapon "+ weaponType +" -weapon&body="+
                                                    task.toString() ));
                                            try {
                                                context.startActivity(intent);
                                            }
                                            catch(ActivityNotFoundException ex)
                                            {
                                                Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                                            }

                                            Toast.makeText(context, "New task created successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }).show();
    }
}
