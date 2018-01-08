package com.example.bogdanbm.finalweapontool;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class AdminActivity extends AppCompatActivity {

    TextView weaponCount;
    ListView wList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button buttonCreateWeapon = (Button) findViewById(R.id.buttonCreateWeapon);
        buttonCreateWeapon.setOnClickListener(new OnClickListenerCreateWeapon(this));
        weaponCount = (TextView) findViewById(R.id.textViewWeaponCount);
        wList = (ListView) findViewById(R.id.wListView);

        readWeapons();
    }

    public void readWeapons() {
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference weapons = root.child("weapons");
        final ArrayList<Weapon> weaponsList= new ArrayList<>();
        final int[] count = {0};

        weapons.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                weaponsList.clear();
                count[0] = 0;
                while(iterator.hasNext())
                {
                    Weapon value = iterator.next().getValue(Weapon.class);
                    weaponsList.add(value);
                    count[0]++;
                    ((AdminWeaponsAdapter) (wList.getAdapter())).notifyDataSetChanged();
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(count[0]);
                stringBuilder.append(" weapons found.");
                weaponCount.setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        wList.setAdapter(new AdminWeaponsAdapter(weaponsList, this, this));
    }
}
