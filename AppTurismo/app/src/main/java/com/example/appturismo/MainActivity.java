package com.example.appturismo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    //Generar un array para llenar los destinos
    String[] targets ={"Guatape", "Manizales","Medellin"};
    //Crear variable que contenga el destino seleccionado
    String selecTarget;

    //Instaciar los elementos que tiene id
    EditText name, email, phone, personsnumber;
    TextView totalplan;
    Spinner target;
    Button calculate, clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referenciar los objetos instaciados
        name = findViewById(R.id.etname);
        email = findViewById(R.id.etemail);
        phone = findViewById(R.id.etphone);
        personsnumber = findViewById(R.id.edpersonalnumber);
        target = findViewById(R.id.sptarget);
        totalplan = findViewById(R.id.tvtotalplan);
        calculate = findViewById(R.id.btncalculate);
        clean = findViewById(R.id.btnclear);

        //Crear el ArrayAdapte (Adaptador) para recibir la infomacion del array target
        ArrayAdapter adpTarget = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, targets);
        //Pasar los datos del adaptador adpTarget al Spiner Target
        target.setAdapter(adpTarget);

        //Generar el evento para cuando se seleccione un destino
        target.setOnItemSelectedListener(this);

        //Generar el evento del boton calculate
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar si todos los datos se haya diligenciados
                String mname = name.getText().toString(); //Asignar el contenido de name a la variable
                String memail = email.getText().toString();
                String mphone = phone.getText().toString();
                String mpersonsnumber  = personsnumber.getText().toString();

                if (!mname.isEmpty() && !memail.isEmpty() && !mphone.isEmpty() && !mpersonsnumber.isEmpty()){
                    //Checquear el destino
                    double targteValue = 0;

                    switch (selecTarget){
                        case "Guatape":
                            targteValue = 350000;
                            break;
                        case "Manizales":
                            targteValue = 250000;
                            break;
                        case "Medellin":
                            targteValue = 150000;
                            break;
                    }

                    //Chequear el numeros de personas para el decuento (si es mayor o igual a 5)
                    double discout = 0;

                    if(Integer.valueOf(mpersonsnumber) >= 5){
                        discout = (targteValue * Integer.valueOf(mpersonsnumber)) * 0.1;
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selecTarget = targets[position]; //Asigna el contenido del arreglo en esa posicion

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}