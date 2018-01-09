package com.example.bogdanbm.finalweapontool;

import android.content.Intent;
import android.view.View;

/**
 * Created by BogdanBM on 1/9/2018.
 */

public class OnClickListenerTasksPerWeapon implements View.OnClickListener {
    private static UserActivity userActivity;
    private static TasksPerWeapon tasksPerWeapon;
    Weapon weapon;

    public OnClickListenerTasksPerWeapon(UserActivity adminActivity, TasksPerWeapon tasksPerWeapon, Weapon objectWeapon) {
        this.userActivity = adminActivity;
        this.tasksPerWeapon = tasksPerWeapon;
        this.weapon = objectWeapon;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(userActivity, tasksPerWeapon.getClass());
        intent.putExtra("weaponType", weapon.type);
        intent.putExtra("weaponId", weapon.id);
        userActivity.startActivity(intent);
    }
}
