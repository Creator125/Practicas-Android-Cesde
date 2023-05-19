package com.example.appventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etIdent.getText().toString().isEmpty() && !etFullnombre.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty() && !etContrasena.getText().toString().isEmpty()){
                    guardaVendedor(etIdent.getText().toString(), etFullnombre.getText().toString(), etEmail.getText().toString(), etContrasena.getText().toString());
                }else{
                    tvMensaje.setTextColor(Color.RED);
                    tvMensaje.setText("Todos los datos Son obligatorios");
                }
            }
        });
    }

    private void guardaVendedor(String sIdent, String sFullnombre, String sEmail, String sContrasena) {
        //Instanciar objeto de la clase SQLinteDatabase
        SQLiteDatabase dbw = dbVentas.getWritableDatabase();
        ContentValues cVendedor = new ContentValues(); //Tabla temporal

        cVendedor.put("ident", sIdent);
        cVendedor.put("nombre", sFullnombre);
        cVendedor.put("email", sEmail);
        cVendedor.put("contraseña", sContrasena);
        dbw.insert("Vendedor", null, cVendedor);
        dbw.close();
        tvMensaje.setTextColor(Color.GREEN);
        tvMensaje.setText("Vendedor agregado correctamente");
    }
}