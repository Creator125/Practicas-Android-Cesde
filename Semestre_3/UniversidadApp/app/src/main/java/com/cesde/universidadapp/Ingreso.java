package com.cesde.universidadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Ingreso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);
    }

    public void Estudiante(View view){ //Metodo para ir a la actividad de  Estudiantes
        Intent intEstudiante = new Intent(this, MainActivity.class);
        startActivity(intEstudiante);
    }

    public void Materia(View view){ //Metodo para ir a la actividad de  Materias
        Intent intMateria = new Intent(this, Materia.class);
        startActivity(intMateria);
    }

    public void Matricula(View view){ //Metodo para ir a la actividad de  Matricula
        Intent intMatricula = new Intent(this, MatriculaActivity.class);
        startActivity(intMatricula);
    }
}