package com.example.bogdanbm.finalweapontool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class OnLongClickListenerWeaponAdmin implements View.OnLongClickListener, NumberPicker.OnValueChangeListener {
    private static AdminActivity adminActivity;
    Context context;
    String id;
    String weaponType;
    List<Weapon> weaponList;

    public OnLongClickListenerWeaponAdmin(AdminActivity adminActivity, List<Weapon> weaponList) {
        this.adminActivity = adminActivity;
        this.weaponList = weaponList;
    }

    @Override
    public boolean onLongClick(final View view) {
        context = view.getContext();
        String[]contents = view.getTag().toString().split(" ");
        id = contents[0];
        weaponType = contents[1];

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference weapons = root.child("weapons");

        final CharSequence[] items = { "Edit", "Delete" };
        new AlertDialog.Builder(context).setTitle("My Weapon "+weaponType)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {//=Edit
                            editWeapon(id, weapons, view);
                        }
                        else if (item == 1) {//=Delete

                            weapons.child(id).removeValue().addOnCompleteListener(adminActivity, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(adminActivity, "deleteWeapon:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    if(!task.isSuccessful())
                                    {
                                        Toast.makeText(context, "Unable to delete weapon record.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Weapon was deleted.", Toast.LENGTH_SHORT).show();
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
    public void editWeapon(final String weaponId, final DatabaseReference weapons, View view) {

        Weapon weapon = new Weapon();
        for(Weapon we: weaponList)
        {
            if(Objects.equals(we.id, weaponId))
            {
                weapon = we;
            }
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.weapon_inputform, null, false);

        final EditText editTextWeaponDescription= (EditText) formElementsView.findViewById(R.id.editTextWeaponDescription);
        final NumberPicker numberPickerAmmoType = (NumberPicker) formElementsView.findViewById(R.id.numberPickerAmmoType);
        numberPickerAmmoType.setMaxValue(10);
        numberPickerAmmoType.setMinValue(0);
        numberPickerAmmoType.setWrapSelectorWheel(false);
        numberPickerAmmoType.setOnValueChangedListener(this);
        final EditText editTextWeaponWeight = (EditText) formElementsView.findViewById(R.id.editTextWeaponWeight);
        final RadioGroup weaponTypeRadioGroup = (RadioGroup) formElementsView.findViewById(R.id.weaponTypeRadioGroup);
        final EditText editTextWeaponPrice = (EditText) formElementsView.findViewById(R.id.editTextWeaponPrice);

        editTextWeaponDescription.setText(weapon.description);
        numberPickerAmmoType.setValue(weapon.ammoType);
        editTextWeaponWeight.setText(weapon.weight+"");
        //check radio button based on object's value
        if(Objects.equals(weapon.type, "Auto")) {
            weaponTypeRadioGroup.check(R.id.autoTypeRadioButton);
        }
        else if(Objects.equals(weapon.type, "Semiauto")){
            weaponTypeRadioGroup.check(R.id.semiAutoTypeRadioButton);
        }
        editTextWeaponPrice.setText(weapon.price+"");

        final StringBuffer weaponType = new StringBuffer();
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
        weaponType.setLength(0);
        int checkedId = weaponTypeRadioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.autoTypeRadioButton) {
            weaponType.append("Auto");
        }
        else {
            weaponType.append("Semiauto");
        }

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Weapon")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final Weapon objectWeapon = new Weapon();
                                objectWeapon.id = weaponId;
                                objectWeapon.description = editTextWeaponDescription.getText().toString();
                                objectWeapon.ammoType = numberPickerAmmoType.getValue();
                                objectWeapon.weight = Integer.parseInt(editTextWeaponWeight.getText().toString());
                                objectWeapon.type = weaponType.toString();
                                objectWeapon.price = Integer.parseInt(editTextWeaponPrice.getText().toString());

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        weapons.child(weaponId).setValue(objectWeapon).addOnCompleteListener(adminActivity, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(adminActivity, "updateWeapon:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                if (!task.isSuccessful()){
                                                    Toast.makeText(adminActivity, "Unable to update weapon: " + task.getException(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(context, "Weapon was updated.", Toast.LENGTH_SHORT).show();
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
