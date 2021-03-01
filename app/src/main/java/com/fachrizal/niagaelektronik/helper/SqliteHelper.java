package com.fachrizal.niagaelektronik.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "niagaelektronik";
    private static final int DATABASE_VERSION = 5;
    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE akun (id_akun INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "nama TEXT," +
                        "email TEXT, password TEXT, telepon TEXT, alamat TEXT );");
        db.execSQL(
                "CREATE TABLE pesanan (id_pesanan INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "invoice TEXT, id_akun TEXT, nama_pembeli TEXT, nama_produk TEXT," +
                        "harga TEXT, jumlah TEXT, catatan TEXT, alamat TEXT, status TEXT, tanggal TEXT, " +
                        "gmbBuktiBayar BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS akun");
        db.execSQL("DROP TABLE IF EXISTS pesanan");
        onCreate(db);
    }

}
