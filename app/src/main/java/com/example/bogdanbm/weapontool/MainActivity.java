package com.example.bogdanbm.weapontool;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonCreateWeapon = (Button) findViewById(R.id.buttonCreateWeapon);
        buttonCreateWeapon.setOnClickListener(new OnClickListenerCreateWeapon(this));
        countWeapons();
        final ListView wList = (ListView) findViewById(R.id.wListView);

        List<ObjectWeapon> weapoons= new TableControllerWeapon(this).read();
        final ArrayList<ObjectWeapon> array = new ArrayList<>(weapoons);

        WeaponsAdapter adapter = new WeaponsAdapter(array, this, this);
        wList.setAdapter(adapter);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);
        ArrayList<Entry>entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int index=0;
        for(ObjectWeapon weapon: weapoons)
        {
            int taskCount = new TableControllerTasks(this).count(weapon.id);
            entries.add(new Entry((float)taskCount, index++));
            labels.add(weapon.description);
        }

        LineDataSet dataset = new LineDataSet(entries, "# of tasks per weapon");
        LineData data = new LineData(labels, dataset);
                lineChart.setData(data);

        lineChart.setDescription("WeaponTool");
        dataset.setDrawCubic(true);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateChart();
    }

    public void updateChart()
    {
        List<ObjectWeapon> weapoons= new TableControllerWeapon(this).read();

        LineChart lineChart = (LineChart) findViewById(R.id.chart);
        ArrayList<Entry>entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int index=0;
        for(ObjectWeapon weapon: weapoons)
        {
            int taskCount = new TableControllerTasks(this).count(weapon.id);
            entries.add(new Entry((float)taskCount, index++));
            labels.add(weapon.description);
        }

        LineDataSet dataset = new LineDataSet(entries, "# of tasks per weapon");
        LineData data = new LineData(labels, dataset);
        lineChart.setData(data);

        lineChart.setDescription("WeaponTool");
        dataset.setDrawCubic(true);
    }

    public void countWeapons() {
        int weaponsCount = new TableControllerWeapon(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewWeaponCount);
        textViewRecordCount.setText(weaponsCount + " weapons found.");
    }

    public void readWeapons() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);

        List<ObjectWeapon> pets = new TableControllerWeapon(this).read();

        if (pets.size() > 0) {
            final ListView petsList = (ListView) findViewById(R.id.wListView);
            final ArrayList<ObjectWeapon> array = new ArrayList<>(pets);
            ((WeaponsAdapter) petsList.getAdapter()).setList(array);
            ((BaseAdapter) petsList.getAdapter()).notifyDataSetChanged();

            updateChart();
        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("NO WEAPONS YET.");

            linearLayoutRecords.addView(locationItem);
        }


    }
}
