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

    //Instaciar la base de datos de bdVentas
    clsVentas dbVentas = new clsVentas(this, "dbVentas", null, 1)

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

        //Eventos
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etIdent.getText().toString().isEmpty() && !etFullnombre.getText().toString().isEmpty() &&
                    !etEmail.getText().toString().isEmpty() && !etContrasena.getText().toString().isEmpty()){
                    guardaVenta(etIdent.getText().toString(), etFullnombre.getText().toString(),
                                etEmail.getText().toString(), etFullnombre.getText().toString());
                }else{
                    tvMensaje.setTextColor(Color.RED);
                    tvMensaje.setText("Todos los datos Son obligatorios");
                }
            }
        });
    }

    private void guardaVenta(String sIdent, String seFullnombre, String sEmail, String sContrasena) {
        //Instanciar objeto de la clase SQLinteDatabase
        SQLiteDatabase dbw = dbVentas.getWritableDatabase();
        ContentValues cVentas = new ContentValues(); //Tabla temporal

        cVentas.put("ident", sIdent);
        cVentas.put("nombre", seFullnombre);
        cVentas.put("email", sEmail);
        cVentas.put("contraseña", sContrasena);
        dbw.insert("Vendedor")
    }
}