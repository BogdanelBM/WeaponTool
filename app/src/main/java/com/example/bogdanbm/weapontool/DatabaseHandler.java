package com.example.bogdanbm.weapontool;

/**
 * Created by BogdanBM on 11/10/2017.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by BogdanBM on 10/31/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "WeaponToolDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE weapons " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT, " +
                "type TEXT, " +
                "ammoType INTEGER, " +
                "weight INTEGER, " +
                "price INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE tasks " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "description TEXT, " +
                "weaponID INTEGER, " +
                "FOREIGN KEY (weaponID) REFERENCES weapons(id) ON DELETE CASCADE )";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS tasks";
        db.execSQL(sql);

        sql = "DROP TABLE IF EXISTS weapons";
        db.execSQL(sql);

        onCreate(db);
    }

}
