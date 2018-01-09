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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
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

public class UserActivity extends AppCompatActivity {
    ListView wList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        wList = (ListView) findViewById(R.id.wuListView);

        readWeapons();
    }

    public void updateChart(final ArrayList<Weapon> weaponsList, DatabaseReference root) {
        DatabaseReference tasks = root.child("tasks");

        tasks.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LineChart lineChart = (LineChart) findViewById(R.id.chart);
                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                int index = -1;

                for (Weapon weapon : weaponsList) {
                    index++;
                    int tasks = 0;
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        Task task = ds.getValue(Task.class);
                        if(Objects.equals(task.weaponId, weapon.id) && Objects.equals(task.userId, FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            tasks++;
                        }
                    }
                    entries.add(new Entry((float)tasks, index));
                    labels.add(weapon.description);
                }

                LineDataSet dataset = new LineDataSet(entries, "# of tasks per weapon");
                LineData data = new LineData(labels, dataset);
                lineChart.setData(data);

                lineChart.setDescription("WeaponTool");
                dataset.setDrawCubic(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void readWeapons() {
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference weapons = root.child("weapons");
        final ArrayList<Weapon> weaponsList= new ArrayList<>();

        weapons.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                weaponsList.clear();

                while(iterator.hasNext())
                {
                    Weapon value = iterator.next().getValue(Weapon.class);
                    weaponsList.add(value);

                    ((UserWeaponsAdapter) (wList.getAdapter())).notifyDataSetChanged();
                }

                updateChart(weaponsList, root);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        wList.setAdapter(new UserWeaponsAdapter(weaponsList, this, this));
    }
}
