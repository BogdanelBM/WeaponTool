package com.example.bogdanbm.weapontool;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by BogdanBM on 10/30/2017.
 */

public class OnClickListenerCreateWeapon implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    private static MainActivity mainActivity;
    public OnClickListenerCreateWeapon(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }
    @Override
    public void onClick(View view) {
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.weapon_input_form, null, false);
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

                                ObjectWeapon newWeapon = new ObjectWeapon();
                                newWeapon.description = weaponDescription;
                                newWeapon.weight= Integer.parseInt(weaponWeight);
                                newWeapon.price = Integer.parseInt(weaponPrice);
                                newWeapon.type = weaponType.toString();
                                newWeapon.ammoType= Integer.parseInt(weaponAmmoType);

                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setType("text/plain");
                                intent.setData(Uri.parse("mailto:santejudean.bogdan@gmail.com?cc=santejudean.bogdan@gmail.com&subject=New Weapon Added-WEAPONTOOL&body="+
                                        newWeapon.toString() ));
                                try {
                                    context.startActivity(intent);
                                }
                                catch(ActivityNotFoundException ex)
                                {
                                    Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                                }

                                boolean createSuccessful = new TableControllerWeapon(context).create(newWeapon);
                                if(createSuccessful) {
                                    Toast.makeText(context, "Weapon created successfully.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Unable to save weapon.", Toast.LENGTH_SHORT).show();
                                }
                                mainActivity.countWeapons();
                                mainActivity.readWeapons();
                                dialog.cancel();
                            }

                        }).show();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}
