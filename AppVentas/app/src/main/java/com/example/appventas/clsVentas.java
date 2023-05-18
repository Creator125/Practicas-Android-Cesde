package com.example.appventas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class clsVentas extends SQLiteOpenHelper {
    //Definir la tabla que tendrá la base de datos
    String tblVendedor = "CREATE TABLE Vendedor (identificacion text primary key, nombre text, email text, contraseña text)";
    String tblVentas = "CREATE TABLE Ventas (idVenta integer primary key autoincrement, indentificacion text, valorVenta integer)";

    public clsVentas(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(tblVendedor);
        db.execSQL(tblVentas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Vendedor");
        db.execSQL(tblVendedor);
        db.execSQL("DROP TABLE Ventas");
        db.execSQL(tblVentas);
    }
}
