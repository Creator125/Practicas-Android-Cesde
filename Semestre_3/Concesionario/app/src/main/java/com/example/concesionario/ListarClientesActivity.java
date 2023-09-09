package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListarClientesActivity extends AppCompatActivity {

    ListView lvClientes;
    ArrayList<ClsRegistroClientes> alclientes = new ArrayList<>();
    ArrayAdapter<ClsRegistroClientes> aadclientes;
    ClsOpenHelper admin = new ClsOpenHelper(this,"Concesionario.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);

        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();

        //Asociar objetos Java con objetos XML
        lvClientes = findViewById(R.id.lvclientes);

        //Abrir la conexion en modo lectura y lealizar la consulta general
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor regitro = db.rawQuery("SELECT * FROM TblClientes", null);

        //Llevar los registros del cursosr (registro) a un arrayList
        for (int i = 0; i < regitro.getCount(); i++){
            regitro.moveToNext();
            ClsRegistroClientes objresgistro = new ClsRegistroClientes(regitro.getString(0),regitro.getString(1),
                                                                        regitro.getString(2), regitro.getString(3),
                                                                        regitro.getString(4));

            alclientes.add(objresgistro);
        }//Terminar de mover registros

        aadclientes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alclientes);
        lvClientes.setAdapter(aadclientes);

        db.close();
    }

    //Metodo para el boton regresar
    public void Regresar(View view){
        Intent intClienteas = new Intent(this, ClientesActivity.class);

        startActivity(intClienteas);
    }
}