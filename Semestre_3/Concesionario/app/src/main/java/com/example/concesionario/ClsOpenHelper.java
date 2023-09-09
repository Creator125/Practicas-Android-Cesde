package com.example.concesionario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClsOpenHelper extends SQLiteOpenHelper {
    public ClsOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tabla de clientes
        db.execSQL("CREATE TABLE TblClietes(IdCliente TEXT PRIMARY KEY, " +
                "NomCliente TEXT NOT NULL, DireCliente TEXT NOT NULL ," +
                "TelCliente TEXT NOT NULL, Activo TEXT DEFAULT 'Si')");
        //Tabla de Vehiculos
        db.execSQL("CREATE TABLE TblVehiculos(Placa TEXT PRIMARY KEY," +
                " Modelo TEXT NOT NULL, Marca TEXT NOT NULL, Valor INTEGER NOT NULL, Activo TEXT DEFAULT 'Si')");
        //Tabla de Facturas
        db.execSQL("CREATE TABLE TblFacturas(CodFacturas TEXT PRIMARY KEY," +
                " Fecha TEXT NOT NULL, IdCliente TEXT NOT NULL, Activo TEXT DEFAULT 'Si'," +
                " CONSTRAINT pk_factura FOREIGN KEY (IdCliente) REFERENCES TblClietes(IdCliente))");
        //Tabla de Detalle_Factura
        db.execSQL("CREATE TABLE TblDetalle_Factura(CodFactura TEXT NOT NULL," +
                " Placa TEXT NOT NULL, ValorRenta INTEGER NOT NULL," +
                " CONSTRAINT pk_detalle PRIMARY KEY (CodFactura, Placa)," +
                " FOREIGN KEY (CodFactura) REFERENCES TblFacturas(CodFacturas), " +
                "FOREIGN KEY (Placa) REFERENCES TblVehiculos(Placa))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE TblDetalle_Factura");
        db.execSQL("DROP TABLE TblFacturas");
        db.execSQL("DROP TABLE TblClietes");
        db.execSQL("DROP TABLE TblVehiculos");{
        onCreate(db);
    }

}

    }
