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
    clsProductos dbProductos = new clsProductos(this, "dbProductos", null, 1);

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
                if(!etRefencia.getText().toString().isEmpty() && !etDescripcion.getText().toString().isEmpty() &&
                        !etCosto.getText().toString().isEmpty() && !etExistencia.getText().toString().isEmpty()){
                    if(Integer.parseInt(String.valueOf(etCosto.getText())) > 20000){
                        if(Integer.parseInt(String.valueOf(etExistencia.getText())) >= 5 && Integer.parseInt(String.valueOf(etExistencia.getText())) <= 20){
                            String valorIva = etCosto.getText().toString();
                            double calculoIva = Double.parseDouble(valorIva) / 0.19;

                            guardarProducto(etRefencia.getText().toString(), etDescripcion.getText().toString(),
                                    etCosto.getText().toString(), etExistencia.getText().toString(), Double.toString(calculoIva));

                            tvValorIva.setText(Double.toString(Math.round(calculoIva)));
                        }else{
                            Toast.makeText(getApplicationContext(), "La existencia del producto debe estar entre 5 y 20", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "El costo del producto debe ser superior a 20 mil", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Todos los datos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Boton de buscar
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbr = dbProductos.getReadableDatabase();
                // Variable que cotiene la consulta
                String query = "SELECT referencia, descripcion, costo, existencia, valorIva WHERE referencia = '"+etRefencia.getText().toString()+"'";
                // Crear la tabla cursor para almacenar el registro basado en la consulta (query)
                Cursor cursorProductos = dbr.rawQuery(query, null);

                // Crear la tabla cursor para almacenar el registro basado en la consulta (query)
                if(cursorProductos.moveToFirst()){ // Si encuentra el ident del producto
                    etRefencia.setText(cursorProductos.getString(1));
                    etDescripcion.setText(cursorProductos.getString(2));
                    etCosto.setText(cursorProductos.getString(3));
                }
            }
        });
    }


    private void guardarProducto(String sReferencia, String sDescripcion, String sCosto, String sExistencia, String sValorIva){
        // Buscar la identificación para que no se repita
        SQLiteDatabase dbr = dbProductos.getReadableDatabase();
        String sql = "SELECT referencia FROM Productos WHERE referencia = '"+etRefencia.getText().toString()+"'";
        Cursor cProductos = dbr.rawQuery(sql, null);

        if(!cProductos.moveToFirst()){ //No encuentra la referencia
            // Instanciar objeto de la clase SQLiteDatabase
            SQLiteDatabase dbw = dbProductos.getWritableDatabase();
            ContentValues cProductoT = new ContentValues(); //Tabla temporal
            cProductoT.put("referencia", sReferencia);
            cProductoT.put("descripcion", sDescripcion);
            cProductoT.put("costo", sCosto);
            cProductoT.put("existencia", sExistencia);
            cProductoT.put("valorIva", sValorIva);
            dbw.insert("Productos", null, cProductoT);
            dbw.close();

            Toast.makeText(getApplicationContext(), "Porducto agregado corretamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "La referencia YA EXISTE", Toast.LENGTH_SHORT).show();
        }
    }
}