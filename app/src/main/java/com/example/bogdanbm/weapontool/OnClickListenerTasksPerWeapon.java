package com.example.bogdanbm.weapontool;

import android.content.Intent;
import android.view.View;

/**
 * Created by BogdanBM on 11/5/2017.
 */

public class OnClickListenerTasksPerWeapon implements View.OnClickListener {
    private static MainActivity mainActivity;
    private static TasksPerWeapon tasksPerWeapon;
    ObjectWeapon objectWeapon;

    public OnClickListenerTasksPerWeapon(MainActivity mainActivity, TasksPerWeapon tasksPerWeapon, ObjectWeapon objectWeapon) {
        this.mainActivity = mainActivity;
        this.tasksPerWeapon = tasksPerWeapon;
        this.objectWeapon = objectWeapon;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mainActivity, tasksPerWeapon.getClass());
        intent.putExtra("weaponType", objectWeapon.type);
        intent.putExtra("weaponId", objectWeapon.id);
        mainActivity.startActivity(intent);
    }
}