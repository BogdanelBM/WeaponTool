package com.example.bogdanbm.weapontool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by BogdanBM on 11/5/2017.
 */

public class OnLongClickListenerWeapon implements View.OnLongClickListener {
    private static MainActivity mainActivity;
    Context context;
    String id;
    String weaponType;

    public OnLongClickListenerWeapon(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onLongClick(View view) {
        context = view.getContext();
        String[]contents = view.getTag().toString().split(" ");
        id = contents[0];
        weaponType = contents[1];

        final CharSequence[] items = { "Edit", "Delete" };
        new AlertDialog.Builder(context).setTitle("My Weapon "+weaponType)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            editWeapon(Integer.parseInt(id));
                        }
                        else if (item == 1) {

                            boolean deleteSuccessful = new TableControllerWeapon(context).delete(Integer.parseInt(id));

                            if (deleteSuccessful){
                                Toast.makeText(context, "Weapon record was deleted.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to delete weapon record.", Toast.LENGTH_SHORT).show();
                            }

                            mainActivity.countWeapons();
                            mainActivity.readWeapons();

                        }
                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    public void editWeapon(final int weaponId) {
        final TableControllerWeapon tableControllerWeapon = new TableControllerWeapon(context);
        ObjectWeapon objectWeapon = tableControllerWeapon.readSingleRecord(weaponId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.weapon_input_form, null, false);

        final EditText editTextWeaponDescription= (EditText) formElementsView.findViewById(R.id.editTextWeaponDescription);
        final EditText editTextWeaponAmmoType = (EditText) formElementsView.findViewById(R.id.editTextWeaponAmmoType);
        final EditText editTextWeaponWeight = (EditText) formElementsView.findViewById(R.id.editTextWeaponWeight);
        final RadioGroup weaponTypeRadioGroup = (RadioGroup) formElementsView.findViewById(R.id.weaponTypeRadioGroup);
        final EditText editTextWeaponPrice = (EditText) formElementsView.findViewById(R.id.editTextWeaponPrice);

        editTextWeaponDescription.setText(objectWeapon.description);
        editTextWeaponAmmoType.setText(objectWeapon.ammoType);
        editTextWeaponWeight.setText(objectWeapon.weight+"");
        if(Objects.equals(objectWeapon.type, "Auto")) {
            weaponTypeRadioGroup.check(R.id.autoTypeRadioButton);
        }
        else if(Objects.equals(objectWeapon.type, "Semiauto")){
            weaponTypeRadioGroup.check(R.id.semiAutoTypeRadioButton);
        }
        editTextWeaponPrice.setText(objectWeapon.price);

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

                                ObjectWeapon objectWeapon = new ObjectWeapon();
                                objectWeapon.id = weaponId;
                                objectWeapon.description = editTextWeaponDescription.getText().toString();
                                objectWeapon.ammoType = Integer.parseInt(editTextWeaponAmmoType.getText().toString());
                                objectWeapon.weight = Integer.parseInt(editTextWeaponWeight.getText().toString());
                                objectWeapon.type = weaponType.toString();
                                objectWeapon.price = Integer.parseInt(editTextWeaponPrice.getText().toString());

                                boolean updateSuccessful = tableControllerWeapon.update(objectWeapon);

                                if(updateSuccessful){
                                    Toast.makeText(context, "Weapon record was updated.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Unable to update weapon record.", Toast.LENGTH_SHORT).show();
                                }

                                mainActivity.countWeapons();
                                mainActivity.readWeapons();

                                dialog.cancel();
                            }

                        }).show();
    }
}