package com.example.crudproductos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String oldReferencia;
    //Referenciando los id
    EditText etRefencia, etDescripcion, etCosto, etExistencia;
    TextView tvValorIva;
    Button btnGuardar, btnBuscar, btnActualizar, btnEliminar;
    // Instanciar la base de datos de la clase clsVentas
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



        //Evento del boton de Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etRefencia.getText().toString().isEmpty() && !etDescripcion.getText().toString().isEmpty() &&
                        !etCosto.getText().toString().isEmpty() && !etExistencia.getText().toString().isEmpty()){
                    if(Integer.parseInt(String.valueOf(etCosto.getText())) > 20000){
                        String valorIva = etCosto.getText().toString();
                        double calculoIva = Double.parseDouble(valorIva) / 0.19;

                        guardarProducto(etRefencia.getText().toString(), etDescripcion.getText().toString(),
                                etCosto.getText().toString(), etExistencia.getText().toString(), Double.toString(calculoIva));

                        tvValorIva.setText(Double.toString(Math.round(calculoIva)));

                        if(!(Integer.parseInt(String.valueOf(etExistencia.getText())) >= 5 && Integer.parseInt(String.valueOf(etExistencia.getText())) <= 20)){
                            Toast.makeText(getApplicationContext(), "Es recomendable que la existencia del producto esté entre 5 y 20", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "El costo del producto debe ser superior a 20 mil", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Todos los datos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Evento del boton Buscar
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbr = dbProductos.getReadableDatabase();
                // Variable que cotiene la consulta
                String query = "SELECT referencia, descripcion, costo, existencia, valorIva FROM Productos WHERE referencia = '"+etRefencia.getText().toString()+"'";
                // Crear la tabla cursor para almacenar el registro basado en la consulta (query)
                Cursor cursorProductos = dbr.rawQuery(query, null);

                // Crear la tabla cursor para almacenar el registro basado en la consulta (query)
                if(cursorProductos.moveToFirst()){ // Si encuentra el ident del producto
                    etDescripcion.setText(cursorProductos.getString(1));
                    etCosto.setText(cursorProductos.getString(2));
                    etExistencia.setText(cursorProductos.getString(3));
                    tvValorIva.setText(cursorProductos.getString(4));
                    oldReferencia = etRefencia.getText().toString();
                }else{
                    Toast.makeText(getApplicationContext(), "La referencia NO Existe. Inténtelo con otra...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Evento del boton Actualizar
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbw = dbProductos.getWritableDatabase();

                if(oldReferencia.equals(etRefencia.getText().toString())){
                    dbw.execSQL("UPDATE Productos SET descripcion ='"+etDescripcion.getText().toString()+ "', costo ='"+etCosto.getText().toString()+
                            "', existencia ='"+etExistencia.getText().toString()+"' WHERE referencia = '"+oldReferencia+"'" );

                    //Insetando el valor del costo del iva
                    String valorIva = etCosto.getText().toString();
                    double calculoIva = Double.parseDouble(valorIva) / 0.19;
                    tvValorIva.setText(Double.toString(Math.round(calculoIva)));

                    dbw.execSQL("UPDATE Productos SET valorIva = '"+tvValorIva.getText().toString()+"' WHERE '"+oldReferencia+"'");

                    Toast.makeText(getApplicationContext(), "Producto acutualizado correctamente", Toast.LENGTH_SHORT).show();
                }else{ //No son iguales
                    SQLiteDatabase dbr = dbProductos.getReadableDatabase();
                    String sql = "SELECT referencia FROM Productos WHERE referencia = '"+etRefencia.getText().toString()+"'";
                    Cursor cProductos = dbr.rawQuery(sql, null);

                    if(!cProductos.moveToFirst()){
                        dbw.execSQL("UPDATE Productos SET referencia ='"+etRefencia.getText().toString()+"', descripcion ='"+etDescripcion.getText().toString()+
                                "', costo ='"+etCosto.getText().toString()+"', existencia ='"+etExistencia.getText().toString()+"' WHERE referencia = '"+oldReferencia+"'" );

                        //Insetando el valor del costo del iva
                        String valorIva = etCosto.getText().toString();
                        double calculoIva = Double.parseDouble(valorIva) / 0.19;
                        tvValorIva.setText(Double.toString(Math.round(calculoIva)));

                        dbw.execSQL("UPDATE Productos SET valorIva = '"+tvValorIva.getText().toString()+"' WHERE '"+oldReferencia+"'");

                        Toast.makeText(getApplicationContext(), "Producto acutualizado correctamente", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "La referencia del producto ya existe Intente con otra...", Toast.LENGTH_SHORT).show();
                    }
                    dbr.close();
                }
                dbw.close();
            }
        });

        //Evento del boton Eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(String.valueOf(etExistencia.getText())) == 0){
                    // Verificar que no esté vacía la referencia
                    if(!etRefencia.getText().toString().isEmpty()){
                        //Buscar la referencia
                        SQLiteDatabase dbr = dbProductos.getReadableDatabase();
                        //Variable que contiene la consulta
                        String query = "SELECT referencia FROM Productos WHERE referencia ='"+etRefencia.getText().toString()+"'";
                        // Crear la tabla cursor para almacenar el registro basado en la consulta (query)
                        Cursor cursorProductos = dbr.rawQuery(query, null);

                        // Si encuentra el registro con el ident específico
                        if(cursorProductos.moveToFirst()){
                            Toast.makeText(getApplicationContext(), "Entra...", Toast.LENGTH_SHORT).show();

                            // encuentra la identificacion
                            // Confirmar el borrado del vendedor
                            AlertDialog.Builder adbConfirm = new AlertDialog.Builder(MainActivity.this);
                            adbConfirm.setMessage("Eliminacion del producto");
                            adbConfirm.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase dbElimninar = dbProductos.getWritableDatabase();
                                    dbElimninar.execSQL("DELETE FROM Productos WHERE referencia ='"+etRefencia.getText().toString()+"'");

                                    Toast.makeText(getApplicationContext(), "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });

                            adbConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog alertDialog = adbConfirm.create();
                            alertDialog.show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "La referencia NO EXISTE. Intentelo con otra", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "La existencia debe ser 0", Toast.LENGTH_SHORT).show();
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