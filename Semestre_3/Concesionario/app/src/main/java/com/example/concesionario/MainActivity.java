package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Clientes(View view){ //Metodo de la actividad de Clientes
        Intent intClientes = new Intent(this, ClientesActivity.class);
        startActivity(intClientes);
    }

    public void Vehiculos(View view){ //Metodo de la actividad de Vehiculos
        Intent intVehiculos = new Intent(this, VehiculosActivity.class);
        startActivity(intVehiculos);
    }

    public void Facturas(View view){ //Metodo de la actividad de Facturas
        Intent intFacturas = new Intent(this, FacturasActivity.class);
        startActivity(intFacturas);
    }
}