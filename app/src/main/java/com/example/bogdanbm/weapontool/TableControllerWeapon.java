package com.example.bogdanbm.weapontool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BogdanBM on 10/31/2017.
 */

public class TableControllerWeapon extends DatabaseHandler {

    public TableControllerWeapon(Context context) {
        super(context);
    }

    public boolean create(ObjectWeapon weapon) {
        ContentValues values = new ContentValues();

        values.put("description", weapon.description);
        values.put("ammoType", weapon.ammoType);
        values.put("weight", weapon.weight);
        values.put("price", weapon.price);
        values.put("type", weapon.type);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("weapons", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM weapons";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public List<ObjectWeapon> read() {

        List<ObjectWeapon> recordsList = new ArrayList<ObjectWeapon>();

        String sql = "SELECT * FROM weapons ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String weaponDescription = cursor.getString(cursor.getColumnIndex("description"));
                String weaponType = cursor.getString(cursor.getColumnIndex("type"));
                String weaponAmmoType = cursor.getString(cursor.getColumnIndex("ammoType"));
                String weaponWeight = cursor.getString(cursor.getColumnIndex("weight"));
                String weaponPrice = cursor.getString(cursor.getColumnIndex("price"));

                ObjectWeapon objectWeapon = new ObjectWeapon();
                objectWeapon.id = id;
                objectWeapon.description = weaponDescription;
                objectWeapon.type= weaponType;
                objectWeapon.ammoType = Integer.parseInt(weaponAmmoType);
                objectWeapon.weight = Integer.parseInt(weaponWeight);
                objectWeapon.price = Integer.parseInt(weaponPrice);


                recordsList.add(objectWeapon);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public ObjectWeapon readSingleRecord(int weaponId) {

        ObjectWeapon objectWeapon = null;

        String sql = "SELECT * FROM weapons WHERE id = " + weaponId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            int ammoType = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ammoType")));
            int weight = Integer.parseInt(cursor.getString(cursor.getColumnIndex("weight")));
            int price = Integer.parseInt(cursor.getString(cursor.getColumnIndex("price")));

            objectWeapon = new ObjectWeapon();
            objectWeapon.id = id;
            objectWeapon.description = description;
            objectWeapon.type = type;
            objectWeapon.ammoType = ammoType;
            objectWeapon.weight = weight;
            objectWeapon.price = price;

        }

        cursor.close();
        db.close();

        return objectWeapon;

    }

    public boolean update(ObjectWeapon objectWeapon) {

        ContentValues values = new ContentValues();

        values.put("description", objectWeapon.description);
        values.put("ammoType", objectWeapon.ammoType);
        values.put("weight", objectWeapon.weight);
        values.put("price", objectWeapon.price);
        values.put("type", objectWeapon.type);

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectWeapon.id) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("weapons", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("weapons", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}