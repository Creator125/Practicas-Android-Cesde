package com.example.crudproductos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Referenciando los id
    EditText etRefencia, etDescripcion, etCosto, etExistencia;
    TextView tvValorIva;
    Button btnGuardar, btnBuscar, btnActualizar, btnEliminar;
    // Instanciar la base de datos de la clase clsVentas
    String oldReferencia;
    clsProductos dbProductos = new clsProductos(this, "dbProductos", null, 1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etRefencia = findViewById(R.id.etRefencia);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCosto = findViewById(R.id.etCosto);
        etExistencia = findViewById(R.id.etExistencia);
        tvValorIva = findViewById(R.id.tvValorIva);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);



        //Boton de guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etRefencia.getText().toString().isEmpty() && etDescripcion.getText().toString().isEmpty() &&
                        etCosto.getText().toString().isEmpty() && etExistencia.getText().toString().isEmpty()){

                }
            }
        });
    }

    private void guardarProducto(String sReferencia, String sDescripcion, String sCosto, String sExistencia){
        // Buscar la identificación para que no se repita
        SQLiteDatabase dbr = dbProductos.getReadableDatabase();
        String sql = "SELECT referencia FROM Productos WHERE referencia = '"+etRefencia.getText().toString()+"'";
        Cursor cProductos = dbr.rawQuery(sql, null);

        if(cProductos.moveToFirst()){ //No encuentra la referencia
            // Instanciar objeto de la clase SQLiteDatabase
            SQLiteDatabase dbw = dbProductos.getWritableDatabase();
            ContentValues cProductoT = new ContentValues(); //Tabla temporal
            cProductoT.put("referencia", sReferencia);
            cProductoT.put("descripcion", sDescripcion);
            cProductoT.put("costo", sCosto);
            cProductoT.put("existencia", sExistencia);
            dbw.close();

            Toast.makeText(getApplicationContext(), "Porducto agregado corretamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "La referencia YA EXISTE", Toast.LENGTH_SHORT).show();
        }
    }
}