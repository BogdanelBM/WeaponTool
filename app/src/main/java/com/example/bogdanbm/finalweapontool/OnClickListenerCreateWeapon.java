package com.example.bogdanbm.finalweapontool;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class OnClickListenerCreateWeapon implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    private static AdminActivity adminActivity;
    public OnClickListenerCreateWeapon(AdminActivity adminActivity)
    {
        this.adminActivity = adminActivity;
    }
    @Override
    public void onClick(View view) {
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.weapon_inputform, null, false);
        final EditText editTextWeaponDescription= (EditText) formElementsView.findViewById(R.id.editTextWeaponDescription);
        final EditText editTextWeaponWeight= (EditText) formElementsView.findViewById(R.id.editTextWeaponWeight);
        final NumberPicker numberPickerAmmoType = (NumberPicker) formElementsView.findViewById(R.id.numberPickerAmmoType);
        numberPickerAmmoType.setMaxValue(10);
        numberPickerAmmoType.setMinValue(0);
        numberPickerAmmoType.setWrapSelectorWheel(false);
        numberPickerAmmoType.setOnValueChangedListener(this);
        final RadioGroup weaponTypeRadioGroup = (RadioGroup) formElementsView.findViewById(R.id.weaponTypeRadioGroup);
        final EditText editTextWeaponPrice= (EditText) formElementsView.findViewById(R.id.editTextWeaponPrice);
        final StringBuffer weaponType = new StringBuffer();
        weaponType.setLength(0);
        int checkedId = weaponTypeRadioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.autoTypeRadioButton) {
            weaponType.append("Auto");
        }
        else {
            weaponType.append("Semiauto");
        }
        weaponTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.autoTypeRadioButton) {
                    weaponType.setLength(0);
                    weaponType.append("Auto");
                }
                else {
                    weaponType.setLength(0);
                    weaponType.append("Semiauto");
                }
            }
        });

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Create Weapon")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog, int id) {
                                String weaponDescription= editTextWeaponDescription.getText().toString();
                                String weaponAmmoType= numberPickerAmmoType.getValue()+"";
                                String weaponWeight = editTextWeaponWeight.getText().toString();
                                String weaponPrice = editTextWeaponPrice.getText().toString();

                                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                                final DatabaseReference weapons = root.child("weapons");

                                final String weaponId = weapons.push().getKey();
                                final Weapon newWeapon = new Weapon(weaponId, weaponDescription, weaponType.toString(), Integer.parseInt(weaponAmmoType), Integer.parseInt(weaponWeight), Integer.parseInt(weaponPrice));

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        weapons.child(weaponId).setValue(newWeapon).addOnCompleteListener(adminActivity, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(adminActivity, "createNewWeapon:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                if (!task.isSuccessful()){
                                                    Toast.makeText(adminActivity, "Unable to create new weapon: " + task.getException(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                                    intent.setType("text/plain");
                                                    intent.setData(Uri.parse("mailto:"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"?cc="+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"&subject=WEAPONTOOL-New Weapon Added&body="+
                                                            newWeapon.toString() ));
                                                    try {
                                                        context.startActivity(intent);
                                                    }
                                                    catch(ActivityNotFoundException ex)
                                                    {
                                                        Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });

                                dialog.cancel();
                            }

                        }).show();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}