package com.rt.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Product {
    // Labels table name
    public static final String PRODUCT = "product";

    // Labels Table Columns names
    public static final String PID = "pid";
    public static final String PNAME = "pname";
    public static final String PTYPE = "ptype";
    public static final String PHOST = "phost";
    public static final String PUSER = "puser";
    public static final String PPASS = "ppass";
    public static final String PTCP = "ptcp";
    public static final String PTOM = "ptom";
    
    // property help us to keep data
    public int pid;
    public String pname;
    public String ptype;
    public String phost;
    public String puser;
    public String ppass;
    public String ptcp;
    public String ptom;
    
public static class ProductRepo {
    private DBHelper dbHelper;

    public ProductRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Product product) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Product.PTOM, product.ptom);
        values.put(Product.PTCP, product.ptcp);
        values.put(Product.PPASS, product.ppass);
        values.put(Product.PUSER, product.puser);
        values.put(Product.PHOST, product.phost);
        values.put(Product.PTYPE, product.ptype);
        values.put(Product.PNAME, product.pname);

        // Inserting Row
        long product_Id = db.insert(Product.PRODUCT, null, values);
        db.close(); // Closing database connection
        return (int) product_Id;
    }

    public void delete(int product_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Product.PRODUCT, Product.PID + "= ?", new String[] { String.valueOf(product_Id) });
        db.close(); // Closing database connection
    }

    public void update(Product product) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Product.PTOM, product.ptom);
        values.put(Product.PTCP, product.ptcp);
        values.put(Product.PPASS, product.ppass);
        values.put(Product.PUSER, product.puser);
        values.put(Product.PHOST, product.phost);
        values.put(Product.PTYPE, product.ptype);
        values.put(Product.PNAME, product.pname);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Product.PRODUCT, values, Product.PID + "= ?", new String[] { String.valueOf(product.pid) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getProductList(String ptype) {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery=null;
        
        if(ptype==null){selectQuery =  "SELECT  " +
                Product.PID + "," +
                Product.PNAME + "," +
                Product.PTYPE + "," +
                Product.PHOST + "," +
                Product.PUSER + "," +
                Product.PPASS + "," +
                Product.PTCP + "," +
                Product.PTOM +
                " FROM " + Product.PRODUCT +
                " ORDER BY "+Product.PTYPE;
        }
        else{selectQuery =  "SELECT  " +
                Product.PID + "," +
                Product.PNAME + "," +
                Product.PTYPE + "," +
                Product.PHOST + "," +
                Product.PUSER + "," +
                Product.PPASS + "," +
                Product.PTCP + "," +
                Product.PTOM +
                " FROM " + Product.PRODUCT +
                " WHERE "+Product.PTYPE+"='"+ptype+"'";
        }
        //Product product = new Product();
        ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> product = new HashMap<String, String>();
                product.put("id", cursor.getString(cursor.getColumnIndex(Product.PID)));
                product.put("name", cursor.getString(cursor.getColumnIndex(Product.PNAME)));
                productList.add(product);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;

    }

    public Product getProductById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Product.PID + "," +
                Product.PNAME + "," +
                Product.PTYPE + "," +
                Product.PHOST + "," +
                Product.PUSER + "," +
                Product.PPASS + "," +
                Product.PTCP + "," +
                Product.PTOM +" FROM " + Product.PRODUCT
                + " WHERE " +
                Product.PID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        //int iCount =0;
        Product product = new Product();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                product.pid =cursor.getInt(cursor.getColumnIndex(Product.PID));
                product.pname =cursor.getString(cursor.getColumnIndex(Product.PNAME));
                product.ptype =cursor.getString(cursor.getColumnIndex(Product.PTYPE));
                product.phost =cursor.getString(cursor.getColumnIndex(Product.PHOST));
                product.puser =cursor.getString(cursor.getColumnIndex(Product.PUSER));
                product.ppass =cursor.getString(cursor.getColumnIndex(Product.PPASS));
                product.ptcp =cursor.getString(cursor.getColumnIndex(Product.PTCP));
                product.ptom =cursor.getString(cursor.getColumnIndex(Product.PTOM));
                
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return product;
    }
}



}