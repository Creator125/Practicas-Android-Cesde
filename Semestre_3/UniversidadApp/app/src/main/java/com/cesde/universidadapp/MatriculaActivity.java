package com.cesde.universidadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MatriculaActivity extends AppCompatActivity {
    EditText etmatricula, etfechasistema, etcarnet, etmateria;
    TextView tvnombre, tvcarrera, tvmateria, tvcredito;
    CheckBox cbactivo;
    Button btbuscarmatricula, btbuscarcarnet, btbuscarmateria, btadicionar, btanular, btlimpiar, btregresar;
    String matricula, fechaMatricula, carnet, nombre, carrera, codigoMateria, materia, credito, coleccion = "Matricula", clave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matricula);

        //Asociar los objetos Java con los objetos Xml
        etmatricula = findViewById(R.id.etmatricula);
        etfechasistema = findViewById(R.id.etfechasistema);
        etcarnet = findViewById(R.id.etcarnet);
        etmateria = findViewById(R.id.etmateria);
        tvnombre = findViewById(R.id.tvnombre);
        tvcarrera = findViewById(R.id.tvcarrera);
        tvmateria = findViewById(R.id.tvmateria);
        tvcredito = findViewById(R.id.tvcredito);
        cbactivo = findViewById(R.id.cbactivo);
        btbuscarmatricula = findViewById(R.id.btbuscarmatricula);
        btbuscarcarnet = findViewById(R.id.btbuscarcarnet);
        btbuscarmateria = findViewById(R.id.btbuscarmateria);
        btadicionar = findViewById(R.id.btadicionar);
        btanular = findViewById(R.id.btanular);
        btlimpiar = findViewById(R.id.btlimpiar);
        btregresar = findViewById(R.id.btregresar);
    }//fin metodo onCreate

    public void Consultar(View view){
        ConsultarDocumento();
    }//Fin metodo consultar

    public void ConsultarCarnet(View view){
        carnet = etcarnet.getText().toString();

        if (!carnet.isEmpty()){
            db.collection("Estudiante")
                    .whereEqualTo("Carnet", carnet)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0){
                                    //Encontró al menos un documento
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        clave = document.getId();
                                        tvnombre.setText(document.getString("Nombre"));
                                        tvcarrera.setText(document.getString("Carrera"));
                                    }
                                }else{
                                    //No encotro docuentos
                                    Toast.makeText(MatriculaActivity.this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "El carnet es requerido", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }

    public void ConsultarMateria(View view){
        codigoMateria = etmateria.getText().toString();

        if (!codigoMateria.isEmpty()){
            db.collection("Materia")
                    .whereEqualTo("Codigo", codigoMateria)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0){
                                    //Encontró al menos un documento
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        clave = document.getId();
                                        tvmateria.setText(document.getString("Materia"));
                                        tvcredito.setText(document.getString("Credito"));
                                    }
                                }else{
                                    //No encotro docuentos
                                    Toast.makeText(MatriculaActivity.this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "El codigo de la materia es requerido", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }

    public void Adicionar(View view){
        //Validar que toda la informacion fue digita
        carnet=etcarnet.getText().toString();
        nombre= tvnombre.getText().toString();
        carrera=tvcarrera.getText().toString();

        if (!carnet.isEmpty() && !nombre.isEmpty() && !carrera.isEmpty()){
            //Llenar el contenedor
            Map<String, Object> Matricula = new HashMap<>();
            Matricula.put("Codigo_matricula", matricula);
            Matricula.put("Fecha", fechaMatricula);
            Matricula.put("Carnet", carnet);
            Matricula.put("Nombre", nombre);
            Matricula.put("Carrera", carrera);
            Matricula.put("Codigo_materia", codigoMateria);
            Matricula.put("Materia", materia);
            Matricula.put("Credito", credito);
            Matricula.put("Activo", "Si");

            // Add a new document with a generated ID
            db.collection(coleccion)
                    .add(Matricula)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MatriculaActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MatriculaActivity.this, "Error guardando registro", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Datos requeridos", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//fin metodo adicionar

    private void ConsultarDocumento(){
        matricula = etmatricula.getText().toString();

        if (!matricula.isEmpty()){
            db.collection(coleccion)
                    .whereEqualTo("Codigo_matricula", matricula)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0){
                                    //Encontró al menos un documento
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        clave = document.getId();
                                        etmatricula.setText(document.getString("Codigo_matricula"));
                                        etfechasistema.setText(document.getString("Fecha"));
                                        etcarnet.setText(document.getString("Carnet"));
                                        tvnombre.setText(document.getString("Nombre"));
                                        tvcarrera.setText(document.getString("Carrera"));
                                        etmateria.setText(document.getString("Codigo_materia"));
                                        tvmateria.setText(document.getString("Materia"));
                                        tvcredito.setText(document.getString("Credito"));

                                        if(document.getString("Activo").equals("Si")){
                                            cbactivo.setChecked(true);
                                        }else{
                                            cbactivo.setChecked(false);
                                        }

                                        btadicionar.setEnabled(true);
                                        btanular.setEnabled(true);
                                        cbactivo.setEnabled(false);
                                    }
                                }else{
                                    //No encotro docuentos
                                    Toast.makeText(MatriculaActivity.this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                                    btadicionar.setEnabled(true);
                                }

                                etmatricula.setEnabled(false);
                                etfechasistema.setEnabled(true);
                                etcarnet.setEnabled(true);
                                etmateria.setEnabled(true);
                                etfechasistema.requestFocus();
                            }else{
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "El codigo de la matricula es requerido", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//Fin metodo ConsultarRegistro()
}