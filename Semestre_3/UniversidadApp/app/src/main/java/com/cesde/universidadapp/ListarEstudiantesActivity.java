package com.cesde.universidadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListarEstudiantesActivity extends AppCompatActivity {
    TextView tvaplicacion;
    RecyclerView rvestudiantes;
    ArrayList<clsEstudiantes> listaEstudiantes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String coleccion = "Estudiante";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_estudiantes);
        getSupportActionBar().hide();

        //Asociar objetos Java con los objetos XML
        tvaplicacion = findViewById(R.id.tvaplicacion);
        rvestudiantes = findViewById(R.id.rvestudiantes);
        //Inicializar el ArrayList
        listaEstudiantes = new ArrayList<>();
        //Administrador del recycleview
        rvestudiantes.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rvestudiantes.setHasFixedSize(true);
        cargar_datos();

        //Mostar los resultados
        cargar_datos();
    }

    public void Regresar(View view){
        Intent intmain = new Intent(this, MainActivity.class);
        startActivity(intmain);
    }//Metodo Cegresar

    private void cargar_datos(){
        db.collection(coleccion)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Encontró al menos un documento
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Llenar el array
                                clsEstudiantes objestudiantes = new clsEstudiantes();
                                objestudiantes.setCarnet(document.getString("Carnet"));
                                objestudiantes.setNombre(document.getString("Nombre"));
                                objestudiantes.setCarrera(document.getString("Carrera"));
                                objestudiantes.setSemestre(document.getString("Semestre"));
                                objestudiantes.setActivo(document.getString("Activo"));
                                listaEstudiantes.add(objestudiantes);
                            }
                            clsEstudintesAdapter adestudiantes = new clsEstudintesAdapter(listaEstudiantes);
                            //Llenar el RecyclerView con los adaptados
                            rvestudiantes.setAdapter(adestudiantes);
                        }
                    }
                });
    }//Fin metodo cargar_datos

}