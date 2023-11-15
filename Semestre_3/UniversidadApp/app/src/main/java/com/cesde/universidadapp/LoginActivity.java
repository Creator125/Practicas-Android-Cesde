package com.cesde.universidadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText etusuario, etcontrasena;
    Button btingreso;
    String usuario,contrasena, coleccion = "Estudiante";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Asociar los objetos Java con los objetos Xml
        etusuario = findViewById(R.id.etusuario);
        etcontrasena = findViewById(R.id.etcontrasena);
    }
}