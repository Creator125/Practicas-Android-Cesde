package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class VehiculosActivity extends AppCompatActivity {
    EditText jetplaca, jetmodelo, jetmarca, jetvalor;
    CheckBox jcbactivo;
    Button jbtguardar,jbtanular;
    String placa, modelo, marca, valor;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);
    long respuesta;
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();

        //Asociar objetos Java con los objetos Xml
        jetplaca = findViewById(R.id.etplaca);
        jetmodelo = findViewById(R.id.etmodelo);
        jetmarca = findViewById(R.id.etmarca);
        jetvalor = findViewById(R.id.etvalor);
        jbtguardar = findViewById(R.id.btguardar);
        jbtanular = findViewById(R.id.btanular);
        jcbactivo = findViewById(R.id.cbactivo);
        sw = false;
        jetplaca.requestFocus();

        Toast.makeText(this,"Digite la placa del vehiculo y click en buscar",Toast.LENGTH_LONG).show();
    }//Fin del metodo onCreate


    public void Consultar(View view){
        placa = jetplaca.getText().toString();

        //Validar que los campos no estan vacios
        if(placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            //abrir base de datos en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor registro=db.rawQuery("select * from TblVehiculos where Placa='"+placa+"'",null);
            if (registro.moveToNext()){
                sw = true;
                //el registro existe
                jbtanular.setEnabled(true);
                jetmodelo.setText(registro.getString(1));
                jetmarca.setText(registro.getString(2));
                jetvalor.setText(registro.getString(3));

                if (registro.getString(4).equals("Si"))
                    jcbactivo.setChecked(true);
                else
                    jcbactivo.setChecked(false);
            }else{
                jcbactivo.setChecked(true);
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
            }
            //Activar objetos
            jetplaca.setEnabled(true);
            jetmodelo.setEnabled(true);
            jetmarca.setEnabled(true);
            jetvalor.setEnabled(true);
            jbtguardar.setEnabled(true);
            jetmodelo.requestFocus();
            jetplaca.setEnabled(false);
            db.close();
        }
    }//fin metodo consultar


    public void Guardar(View view){
        placa = jetplaca.getText().toString();
        modelo = jetmodelo.getText().toString();
        marca = jetmarca.getText().toString();
        valor = jetvalor.getText().toString();

        //Validar que los campos no estan vacios
        if (!modelo.isEmpty() && !marca.isEmpty() && !valor.isEmpty()){
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            //Llenar el contendor
            fila.put("Placa",placa);
            fila.put("Modelo",modelo);
            fila.put("Marca",marca);
            fila.put("Valor",valor);
            if(sw == false){
                respuesta=db.insert("TblVehiculos",null,fila);
            }else{
                sw = false;
                respuesta = db.update("TblVehiculos", fila, "Placa = '"+placa+"'", null);
            }

            if (respuesta > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetmodelo.requestFocus();
        }
    }//fin metodo Guardar


    //Metodo anular
    public void Anular(View view){
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues fila = new ContentValues();
        fila.put("Activo", "No");
        respuesta = db.update("TblVehiculos", fila, "Placa = '"+placa+"'", null);
        if (respuesta > 0){
            Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
            Limpiar_campos();
        }else {
            Toast.makeText(this, "Error anulado", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }//fin metodo Cancelar

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    private void Limpiar_campos(){
        sw = false;
        jetplaca.setEnabled(true);
        jetmodelo.setEnabled(false);
        jetmarca.setEnabled(false);
        jetvalor.setEnabled(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        jetplaca.setText("");
        jetmodelo.setText("");
        jetmarca.setText("");
        jetvalor.setText("");
        jcbactivo.setChecked(false);
        jetplaca.requestFocus();
    }
}