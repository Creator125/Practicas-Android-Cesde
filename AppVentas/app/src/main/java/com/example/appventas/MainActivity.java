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
    String oldClient;

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
                                tvMensaje.setTextColor(Color.GREEN);
                                tvMensaje.setText("Vendedor eliminado correctamente");
                            }
                        });
                        adbConfirm.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                    tvMensaje.setTextColor(Color.RED);

                }
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbw = dbVentas.getWritableDatabase();
                if(oldClient.equals(etIdent.getText().toString())){

                    dbw.execSQL("UPDATE Vededor SET fullnombe='"+etFullnombre.getText().toString()+"' email="+etEmail.getText().toString()+"' contraseña= '"+etContrasena.getText().toString()+"' WHERE ident ='"+etIdent.getText().toString());
                }else{
                    SQLiteDatabase dbr = dbVentas.getReadableDatabase();
                    String sql = "SELECT ident FROM Vendedor WHERE ident = '"+etIdent.getText().toString()+"'";
                    Cursor cVendedor = dbr.rawQuery(sql, null);

                }
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
                    oldClient = etIdent.getText().toString();
                }else{
                    tvMensaje.setTextColor(Color.RED);
                    tvMensaje.setText("La indetificacion del cliente no existe. Intentelo con otro");
                }
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etIdent.getText().toString().isEmpty() && !etFullnombre.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty() && !etContrasena.getText().toString().isEmpty()){
                    guardaVendedor(etIdent.getText().toString(), etFullnombre.getText().toString(), etEmail.getText().toString(), etContrasena.getText().toString());
                }else{
                    tvMensaje.setTextColor(Color.RED);
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
            tvMensaje.setTextColor(Color.GREEN);
            tvMensaje.setText("Vendedor agregado correctamente");
        }else{
            tvMensaje.setText("La identificacion del vendedor YA EXISTE. Intentelo con otra");
            tvMensaje.setTextColor(Color.RED);
        }
    }
}