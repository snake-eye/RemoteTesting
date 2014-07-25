package com.rt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "rtdb";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + Product.PRODUCT  + "("
                + Product.PID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Product.PNAME + " TEXT, "
                + Product.PTYPE + " TEXT, "
                + Product.PHOST + " TEXT, "
                + Product.PUSER + " TEXT, "
                + Product.PPASS + " TEXT, "
                + Product.PTCP + " TEXT, "
                + Product.PTOM + " TEXT)";

        db.execSQL(CREATE_TABLE_STUDENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Product.PRODUCT);

        // Create tables again
        onCreate(db);

    } 

}