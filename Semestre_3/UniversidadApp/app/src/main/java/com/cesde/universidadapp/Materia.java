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

public class Materia extends AppCompatActivity {
    EditText etcodigoMateria,etnombreMateria,etcredito,etprofesor;
    CheckBox cbactivo;
    Button btguardar,btcancelar,btactivar,btregresar;
    String codigoMateria,nombreMateria,credito,profesor,coleccion = "Materia", clave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);
        //Ocultar barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar los objetos Java con los objetos Xml
        etcodigoMateria = findViewById(R.id.etcodigomateria);
        etnombreMateria = findViewById(R.id.etnombreMateria);
        etcredito = findViewById(R.id.etcredito);
        etprofesor = findViewById(R.id.etprofesor);
        cbactivo=findViewById(R.id.cbactivo);
        btguardar = findViewById(R.id.btguardar);
        btcancelar = findViewById(R.id.btguardar);
        btactivar = findViewById(R.id.btactivar);
        btregresar = findViewById(R.id.btregresar);
        etcodigoMateria.requestFocus();
    }//fin metodo onCreate

    public void Consultar(View view){
        ConsultarDocumento();
    }//Fin metodo consultar

    public void Guardar(View view){
        //Validar que toda la informacion fue digita
        codigoMateria= etcodigoMateria.getText().toString();
        nombreMateria =etnombreMateria.getText().toString();
        credito = etcredito.getText().toString();
        profesor =etprofesor.getText().toString();

        if (!codigoMateria.isEmpty() && !nombreMateria.isEmpty() && !credito.isEmpty()
                && !profesor.isEmpty()){
            //Llenar el contenedor
            Map<String, Object> Area = new HashMap<>();
            Area.put("Codigo", codigoMateria);
            Area.put("Materia", nombreMateria);
            Area.put("Credito", credito);
            Area.put("Profesor", profesor);
            Area.put("Activo", "Si");

            // Add a new document with a generated ID
            db.collection(coleccion)
                    .add(Area)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(Materia.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Materia.this, "Error guardando registro", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Datos requeridos", Toast.LENGTH_SHORT).show();
            etcodigoMateria.requestFocus();
        }
    }//fin metodo Guardar

    public void Cancelar(View view){
        Map<String, Object> Area = new HashMap<>();
        Area.put("Activo", "NO");
        db.collection(coleccion).document(clave)
                .update(Area)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Materia.this,"Documento cancelado ...",Toast.LENGTH_SHORT).show();
                        LimpiarCampos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Materia.this,"Error cancelando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Activar(View view){
        Map<String, Object> Area = new HashMap<>();
        Area.put("Activo", "Si");

        db.collection(coleccion).document(clave)
                .update(Area)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Materia.this, "Documento activado ...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Materia.this, "Error activando documento...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Regresar(View view){
        Intent intIngreso = new Intent(this, Ingreso.class);
        startActivity(intIngreso);
    }

    private void ConsultarDocumento(){
        codigoMateria = etcodigoMateria.getText().toString();

        if (!codigoMateria.isEmpty()){
            db.collection(coleccion)
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
                                        etcodigoMateria.setText(document.getString("Codigo"));
                                        etcredito.setText(document.getString("Credito"));
                                        etprofesor.setText(document.getString("Profesor"));

                                        if(document.getString("Activo").equals("Si")){
                                            cbactivo.setChecked(true);
                                        }else{
                                            cbactivo.setChecked(false);
                                        }

                                        btguardar.setEnabled(true);
                                        btactivar.setEnabled(true);
                                        cbactivo.setEnabled(true);
                                    }
                                }else{
                                    //No encotro docuentos
                                    Toast.makeText(Materia.this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                                    btguardar.setEnabled(true);
                                }

                                etcodigoMateria.setEnabled(false);
                                etnombreMateria.setEnabled(true);
                                etcredito.setEnabled(true);
                                etprofesor.setEnabled(true);
                                etcodigoMateria.requestFocus();
                            }else{
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "El codigo es requerido", Toast.LENGTH_SHORT).show();
            etcodigoMateria.requestFocus();
        }
    }//Fin metodo ConsultarRegistro()

    private void LimpiarCampos(){
        etcodigoMateria.setText("");
        etnombreMateria.setText("");
        etcredito.setText("");
        etprofesor.setText("");
        cbactivo.setChecked(false);
        cbactivo.setEnabled(false);
        etcodigoMateria.setEnabled(true);
        etnombreMateria.setEnabled(false);
        etcredito.setEnabled(false);
        etprofesor.setEnabled(false);
        btguardar.setEnabled(false);
        btactivar.setEnabled(false);
    }//Fin metodo limpiar_campos()
}