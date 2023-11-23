package com.cesde.universidadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    EditText etcarnet,etnombre,etcarrera,etsemestre;
    CheckBox cbactivo;
    Button btadicionar,btmodificar,btanular,bteliminar;
    String carnet,nombre,carrera,semestre,coleccion = "Estudiante", clave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar los objetos Java con los objetos Xml
        etcarnet=findViewById(R.id.etcarnet);
        etnombre=findViewById(R.id.etnombre);
        etcarrera=findViewById(R.id.etcarrera);
        etsemestre=findViewById(R.id.etsemestre);
        cbactivo=findViewById(R.id.cbactivo);
        btadicionar=findViewById(R.id.btadicionar);
        btanular=findViewById(R.id.btanular);
        bteliminar=findViewById(R.id.bteliminar);
        btmodificar=findViewById(R.id.btmodificar);
        etcarnet.requestFocus();
    }//fin metodo onCreate

    public void Adicionar(View view){
        //Validar que toda la informacion fue digita
        carnet=etcarnet.getText().toString();
        nombre=etnombre.getText().toString();
        carrera=etcarrera.getText().toString();
        semestre=etsemestre.getText().toString();

        if (!carnet.isEmpty() && !nombre.isEmpty() && !carrera.isEmpty()
                && !semestre.isEmpty()){
            //Llenar el contenedor
            Map<String, Object> Alumno = new HashMap<>();
            Alumno.put("Carnet", carnet);
            Alumno.put("Nombre", nombre);
            Alumno.put("Carrera", carrera);
            Alumno.put("Semestre", semestre);
            Alumno.put("Activo", "Si");

            // Add a new document with a generated ID
            db.collection(coleccion)
                    .add(Alumno)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error guardando registro", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Datos requeridos", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//fin metodo adicionar

    public void Regresar(View view){
        Intent intIngreso = new Intent(this, Ingreso.class);
        startActivity(intIngreso);
    }

    public void Consultar(View view){
        ConsultarDocumento();
    }//Fin metodo consultar

    public void Limpiar(View view){
        LimpiarCampos();
    }

    public void Modificar(View view){
        //Validar que todos los campos están vacios
        nombre = etnombre.getText().toString();
        carrera = etcarrera.getText().toString();
        semestre = etnombre.getText().toString();

        if (!nombre.isEmpty() && !carrera.isEmpty() && !semestre.isEmpty()){
            //Llenar el contenedor
            Map<String, Object> Alumno = new HashMap<>();
            Alumno.put("Carnet", carnet);
            Alumno.put("Nombre", nombre);
            Alumno.put("Carrera", carrera);
            Alumno.put("Semestre", semestre);

            if (cbactivo.isChecked()){
                Alumno.put("Activo", "Si");
            }else{
                Alumno.put("Activo", "No");

                //Modificar la coleccion
                db.collection(coleccion).document(clave)
                        .set(Alumno)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Documento actualizado ...",Toast.LENGTH_SHORT).show();
                                LimpiarCampos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error actualizando documento...",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
        }
    }//Fin metodo Modificar

    public void Eliminar(View view){
        db.collection(coleccion).document(clave)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Documento eliminando ...",Toast.LENGTH_SHORT).show();
                        LimpiarCampos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error eliminando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }//Fin metodo Eliminar

    public void Anular(View view){
        Map<String, Object> Alumno = new HashMap<>();
        Alumno.put("Activo", "NO");
        db.collection(coleccion).document(clave)
                .update(Alumno)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Documento anulado ...",Toast.LENGTH_SHORT).show();
                        LimpiarCampos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error anulando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }//Fin metodo anular

    public void Listar(View view){
        Intent intlistar = new Intent(this, ListarEstudiantesActivity.class);
        startActivity(intlistar);
    }

    private void ConsultarDocumento(){
        carnet = etcarnet.getText().toString();

        if (!carnet.isEmpty()){
            db.collection(coleccion)
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
                                        etnombre.setText(document.getString("Nombre"));
                                        etcarrera.setText(document.getString("Carrera"));
                                        etsemestre.setText(document.getString("Semestre"));

                                        if(document.getString("Activo").equals("Si")){
                                            cbactivo.setChecked(true);
                                        }else{
                                            cbactivo.setChecked(false);
                                        }

                                        btmodificar.setEnabled(true);
                                        btanular.setEnabled(true);
                                        bteliminar.setEnabled(true);
                                        cbactivo.setEnabled(false);
                                    }
                                }else{
                                    //No encotro docuentos
                                    Toast.makeText(MainActivity.this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                                    btadicionar.setEnabled(true);
                                }

                                etcarnet.setEnabled(false);
                                etnombre.setEnabled(true);
                                etcarrera.setEnabled(true);
                                etsemestre.setEnabled(true);
                                etnombre.requestFocus();
                            }else{
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "El carnet es requerido", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//Fin metodo ConsultarRegistro()

    private void LimpiarCampos(){
        etcarnet.setText("");
        etnombre.setText("");
        etsemestre.setText("");
        etcarrera.setText("");
        cbactivo.setChecked(false);
        cbactivo.setEnabled(false);
        etcarnet.setEnabled(true);
        etnombre.setEnabled(false);
        etcarrera.setEnabled(false);
        etsemestre.setEnabled(false);
        btadicionar.setEnabled(false);
        btanular.setEnabled(false);
        bteliminar.setEnabled(false);
        btmodificar.setEnabled(false);
        etcarnet.requestFocus();
    }
}
