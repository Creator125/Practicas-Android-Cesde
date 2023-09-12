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

public class ClientesActivity extends AppCompatActivity {

    EditText jetidentificacion,jetnombre,jetdireccion,jettelefono;
    CheckBox jcbactivo;
    Button jbtguardar,jbtanular;
    String identificacion,nombre,direccion,telefono;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);
    long respuesta;
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar objetos Java con los objetos Xml
        jetidentificacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetdireccion=findViewById(R.id.etdireccion);
        jettelefono=findViewById(R.id.ettelefono);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        jcbactivo=findViewById(R.id.cbactivo);
        sw = false;
        jetidentificacion.requestFocus();
        Toast.makeText(this,"Digite identificacion y click en buscar",Toast.LENGTH_LONG).show();
    }//fin Metodo onCreate

    public void Consultar(View view){
        identificacion=jetidentificacion.getText().toString();
        //Validar que los campos no estan vacios
        if(identificacion.isEmpty()){
            Toast.makeText(this, "La identificacion es requerida", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }else{
            //abrir base de datos en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor registro=db.rawQuery("select * from TblClientes where IdCliente='"+identificacion+"'",null);
            if (registro.moveToNext()){
                sw = true;
                //el registro existe
                jbtanular.setEnabled(true);
                jetnombre.setText(registro.getString(1));
                jetdireccion.setText(registro.getString(2));
                jettelefono.setText(registro.getString(3));
                if (registro.getString(4).equals("Si"))
                    jcbactivo.setChecked(true);
                else
                    jcbactivo.setChecked(false);
            }else{
                jcbactivo.setChecked(true);
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
            }
            //Activar objetos
            jetnombre.setEnabled(true);
            jetdireccion.setEnabled(true);
            jettelefono.setEnabled(true);
            jbtguardar.setEnabled(true);
            jetnombre.requestFocus();
            jetidentificacion.setEnabled(false);
            db.close();
        }
    }//fin metodo consultar

    public void Guardar(View view){
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        direccion=jetdireccion.getText().toString();
        telefono=jettelefono.getText().toString();
        //Validar que los campos no estan vacios
        if (!nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()){
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            //Llenar el contendor
            fila.put("IdCliente",identificacion);
            fila.put("NomCliente",nombre);
            fila.put("DireCliente",direccion);
            fila.put("TelCliente",telefono);
            if(sw == false){
                respuesta=db.insert("TblClientes",null,fila);
            }else{
                sw = false;
                respuesta = db.update("TblClientes", fila, "IdCliente = '"+identificacion+"'", null);
            }

            if (respuesta > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetnombre.requestFocus();
        }
    }//fin metodo Guardar

    //Metodo anular
    public void Anular(View view){
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues fila = new ContentValues();
        fila.put("Activo", "No");
        respuesta = db.update("TblCliente", fila, "IdCliente = '"+identificacion+"'", null);
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

    public void  Listar(View view){
        Intent intListar = new Intent(this, ListarClientesActivity.class);
        startActivity(intListar);
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    private void Limpiar_campos(){
        sw = false;
        jetidentificacion.setEnabled(true);
        jetnombre.setEnabled(false);
        jetdireccion.setEnabled(false);
        jettelefono.setEnabled(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        jetidentificacion.setText("");
        jetnombre.setText("");
        jetdireccion.setText("");
        jettelefono.setText("");
        jcbactivo.setChecked(false);
        jetidentificacion.requestFocus();
    }
}
