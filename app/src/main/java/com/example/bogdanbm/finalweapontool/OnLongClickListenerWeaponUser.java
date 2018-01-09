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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

/**
 * Created by BogdanBM on 1/9/2018.
 */

public class OnLongClickListenerWeaponUser implements View.OnLongClickListener {
    private static UserActivity userActivity;
    Context context;
    String id;
    String weaponType;
    List<Weapon> weaponList;

    public OnLongClickListenerWeaponUser(UserActivity userActivity, List<Weapon> weaponList) {
        this.userActivity = userActivity;
        this.weaponList = weaponList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onLongClick(final View view) {
        context = view.getContext();
        String[]contents = view.getTag().toString().split(" ");
        id = contents[0];
        weaponType = contents[1];

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference weapons = root.child("weapons");

        Weapon weapon = new Weapon();
        for(Weapon we: weaponList)
        {
            if(Objects.equals(we.id, id))
            {
                weapon = we;
            }
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.weapon_view, null, false);

        TextView weaponDescription = (TextView) formElementsView.findViewById(R.id.viewWeaponDescription);
        TextView weaponAmmoType = (TextView) formElementsView.findViewById(R.id.viewWeaponAmmoType);
        TextView weaponType = (TextView) formElementsView.findViewById(R.id.viewWeaponType);
        TextView weaponWeight = (TextView) formElementsView.findViewById(R.id.viewWeaponWeight);
        TextView weaponPrice = (TextView) formElementsView.findViewById(R.id.viewWeaponPrice);

        weaponDescription.setText(weapon.description);
        weaponAmmoType.setText(weapon.ammoType+"");
        weaponType.setText(weapon.type);
        weaponWeight.setText(weapon.weight+"");
        weaponPrice.setText(weapon.price);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("View Weapon")
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }

                        }).show();

        return false;
    }
}
