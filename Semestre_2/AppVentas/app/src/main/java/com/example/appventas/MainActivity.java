package com.example.appventas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etIdent, etFullnombre, etEmail, etContrasena;
    Button btnGuardar, btnBuscar, btnActualizar, btnEliminar;
    TextView tvMensaje;

    //Instaciar la base de datos de clsVentas
    clsVentas dbVentas = new clsVentas(this, "dbVentas", null, 1);
    String oldIdent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIdent = findViewById(R.id.etident);
        etFullnombre = findViewById(R.id.etfullNombre);
        etEmail = findViewById(R.id.etemail);
        etContrasena = findViewById(R.id.etcontrasena);
        btnGuardar = findViewById(R.id.btnguardar);
        btnBuscar = findViewById(R.id.btnbuscar);
        btnActualizar = findViewById(R.id.btnactualizar);
        btnEliminar = findViewById(R.id.btneliminar);
        tvMensaje = findViewById(R.id.tvmensaje);

        //Eventos
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar que no esté vacia la identificacion
                if(!etIdent.getText().toString().isEmpty()){
                    SQLiteDatabase dbr = dbVentas.getReadableDatabase();
                    String consulta = "SELECT ident, fullnombre, email FROM Vendedor WHERE ident = '"+etIdent.getText().toString()+"'";
                    //Crear la tabla cursor para crear la tabla cursor para almacenar el regisro en la consulta
                    Cursor cursorVendedor = dbr.rawQuery(consulta,null);
                    //Si encuetra el resgistro de indent especifico
                    if(cursorVendedor.moveToFirst()){
                        //Comfimar el borrado de vendedor
                        AlertDialog.Builder adbConfirm = new AlertDialog.Builder(MainActivity.this);
                        adbConfirm.setMessage("Eliminacion del vendedor");
                        adbConfirm.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase dbeliminar = dbVentas.getWritableDatabase();
                                dbeliminar.execSQL("DELETE FROM Vendedor WHERE ident = '"+etIdent.getText().toString()+"'");
                                tvMensaje.setTextColor(Color.rgb(8, 88, 32));
                                tvMensaje.setText("Vendedor eliminado correctamente");
                            }
                        });
                        adbConfirm.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = adbConfirm.create();
                        alertDialog.show();
                    }else{
                        tvMensaje.setTextColor(Color.rgb(133, 8, 13));
                        tvMensaje.setText("Identificación NO EXISTE. Inténtelo con otra...");
                    }

                }
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbw = dbVentas.getWritableDatabase();
                if(oldIdent.equals(etIdent.getText().toString())){
                    dbw.execSQL("UPDATE Vendedor SET fullnombre='"+etFullnombre.getText().toString()+"', email='"+etEmail.getText().toString()+"', contraseña='"+etContrasena.getText().toString()+"' WHERE ident ='"+oldIdent+"'");
                    tvMensaje.setText("Vendedor actualizado correctamente");
                    tvMensaje.setTextColor(Color.rgb(8, 88, 32));
                }else{
                    SQLiteDatabase dbr = dbVentas.getReadableDatabase();
                    String sql = "SELECT ident FROM Vendedor WHERE ident = '"+etIdent.getText().toString()+"'";
                    Cursor cVendedor = dbr.rawQuery(sql, null);

                    if(!cVendedor.moveToFirst()){
                        dbw.execSQL("UPDATE Vendedor SET ident= '"+etIdent.getText().toString()+"', fullnombre='"+etFullnombre.getText().toString()+"', email='"+etEmail.getText().toString()+"', contraseña='"+etContrasena.getText().toString()+"' WHERE ident ='"+oldIdent+"'");
                        tvMensaje.setText("Vendedor actualizado correctamente");
                        tvMensaje.setTextColor(Color.rgb(8, 88, 32));
                    }else{
                        tvMensaje.setText("La identificación del vendedor YA EXISTE. Inténtelo con otra ...");
                        tvMensaje.setTextColor(Color.rgb(133, 8, 13));
                    }
                    dbr.close();
                }
                dbw.close();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbr = dbVentas.getReadableDatabase();
                String consulta = "SELECT ident, fullnombre, email FROM Vendedor WHERE ident = '"+etIdent.getText().toString()+"'";
                //Crear la tabla cursor para crear la tabla cursor para almacenar el regisro en la consulta
                Cursor cursorVendedor = dbr.rawQuery(consulta,null);
                //Si encuetra el resgistro de indent especifico
                if(cursorVendedor.moveToFirst()){
                    etFullnombre.setText(cursorVendedor.getString(1));
                    etEmail.setText(cursorVendedor.getString(2));
                    tvMensaje.setText("");
                    oldIdent = etIdent.getText().toString();
                }else{
                    tvMensaje.setTextColor(Color.rgb(133, 8, 13));
                    tvMensaje.setText("La indetificacion del cliente NO Existe. Intentelo con otro");
                }
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etIdent.getText().toString().isEmpty() && !etFullnombre.getText().toString().isEmpty() &&
                        !etEmail.getText().toString().isEmpty() && !etContrasena.getText().toString().isEmpty()){
                    guardaVendedor(etIdent.getText().toString(), etFullnombre.getText().toString(), etEmail.getText().toString(), etContrasena.getText().toString());
                }else{
                    tvMensaje.setTextColor(Color.rgb(133, 8, 13));
                    tvMensaje.setText("Todos los datos son obligatorios");
                }
            }
        });
    }

    private void guardaVendedor(String sIdent, String sFullnombre, String sEmail, String sContrasena) {
        //Buscar la identificacion para que no se repita
        SQLiteDatabase  dbr = dbVentas.getReadableDatabase();
        String sql = "SELECT ident FROM Vendedor WHERE ident = '"+etIdent.getText().toString()+"'";
        Cursor sVendedor = dbr.rawQuery(sql, null);

        if(!sVendedor.moveToFirst()) {
            //Instanciar objeto de la clase SQLinteDatabase
            SQLiteDatabase dbw = dbVentas.getWritableDatabase();
            ContentValues cVendedor = new ContentValues(); //Tabla temporal

            cVendedor.put("ident", sIdent);
            cVendedor.put("fullnombre", sFullnombre);
            cVendedor.put("email", sEmail);
            cVendedor.put("contraseña", sContrasena);
            dbw.insert("Vendedor", null, cVendedor);
            dbw.close();
            tvMensaje.setTextColor(Color.rgb(8, 88, 32));
            tvMensaje.setText("Vendedor agregado correctamente");
        }else{
            tvMensaje.setText("La identificacion del vendedor YA EXISTE. Intentelo con otra");
            tvMensaje.setTextColor(Color.rgb(133, 8, 13));
        }
    }
}