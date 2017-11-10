package com.example.bogdanbm.weapontool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonCreateWeapon = (Button) findViewById(R.id.buttonCreateWeapon);
        buttonCreateWeapon.setOnClickListener(new OnClickListenerCreateWeapon(this));
        countWeapons();
        readWeapons();
    }

    public void countWeapons() {
        int weaponsCount = new TableControllerWeapon(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewWeaponCount);
        textViewRecordCount.setText(weaponsCount + " records found.");
    }

    public void readWeapons() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<ObjectWeapon> weapons = new TableControllerWeapon(this).read();

        if (weapons.size() > 0) {

            for (ObjectWeapon obj : weapons) {

                int id = obj.id;
                String weaponDescription = obj.description;
                String weaponType = obj.type;
                int weaponAmmoType = obj.ammoType;
                int weaponWeight = obj.weight;
                int weaponPrice = obj.price;

                String textViewContents = weaponDescription + " - " + weaponType + ", " + weaponWeight;

                TextView textViewWeaponItem= new TextView(this);
                textViewWeaponItem.setPadding(0, 10, 0, 10);
                textViewWeaponItem.setText(textViewContents);
                textViewWeaponItem.setTag(Integer.toString(id) + " " + weaponType);
                textViewWeaponItem.setOnLongClickListener(new OnLongClickListenerWeapon(this));
                Button tasksButton = new Button(this);
                tasksButton.setOnClickListener(new OnClickListenerTasksPerWeapon(this, new TasksPerWeapon(), obj));
                tasksButton.setText("TASKS for " + weaponType);
                linearLayoutRecords.addView(textViewWeaponItem);
                linearLayoutRecords.addView(tasksButton);
            }

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
