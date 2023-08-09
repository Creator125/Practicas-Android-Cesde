package com.example.crudproductos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class clsProductos extends SQLiteOpenHelper {
    //Definiendo la tabla que tendrá la base de datos
    String tblProductos = "CREATE TABLE Productos(referencia INT PRIMARY KEY, descripcion TEXT, costo INT, existencia INT, valorIva INT)";
    public clsProductos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblProductos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Productos");
        db.execSQL(tblProductos);
    }
}
