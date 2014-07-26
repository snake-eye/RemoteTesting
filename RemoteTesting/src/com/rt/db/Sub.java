package com.rt.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Sub {
    // Labels table name
    public static final String SUB = "sub";

    // Labels Table Columns names
    public static final String SID = "sid";
    public static final String SNAME = "sname";
    public static final String PID = "pid";
    
    // property help us to keep data
    public int sid;
    public int pid;
    public String sname;
    
public static class SubRepo {
    private DBHelper dbHelper;

    public SubRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Sub sub) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Sub.PID, sub.pid);
        values.put(Sub.SNAME, sub.sname);

        // Inserting Row
        long s_Id = db.insert(Sub.SUB, null, values);
        db.close(); // Closing database connection
        return (int) s_Id;
    }

    public void delete(int s_Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Sub.SUB, Sub.SID + "= ?", new String[] { String.valueOf(s_Id) });
        db.close(); // Closing database connection
    }

    public void update(Sub sub) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Sub.PID, sub.pid);
        values.put(Sub.SNAME, sub.sname);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Sub.SUB, values, Sub.SID + "= ?", new String[] { String.valueOf(sub.sid) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getSubList(int pid) {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery="SELECT  " +
                Sub.SID + "," +
                Sub.SNAME + "," +
                Sub.PID + 
                " FROM " + Sub.SUB +
                " WHERE "+Sub.PID+"="+pid+
                " ORDER BY "+Sub.SNAME;
 
        ArrayList<HashMap<String, String>> subList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> sub = new HashMap<String, String>();
                sub.put("id", cursor.getString(cursor.getColumnIndex(Sub.SID)));
                sub.put("name", cursor.getString(cursor.getColumnIndex(Sub.SNAME)));
                subList.add(sub);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return subList;

    }

    public Sub getProductById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Sub.SID + "," +
                Sub.SNAME + "," +
                Sub.PID + 
                " FROM " + Sub.SUB
                + " WHERE " +
                Sub.SID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        //int iCount =0;
        Sub sub = new Sub();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                sub.sid =cursor.getInt(cursor.getColumnIndex(Sub.SID));
                sub.sname =cursor.getString(cursor.getColumnIndex(Sub.SNAME));
                sub.pid =cursor.getInt(cursor.getColumnIndex(Sub.PID));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sub;
    }
}
}