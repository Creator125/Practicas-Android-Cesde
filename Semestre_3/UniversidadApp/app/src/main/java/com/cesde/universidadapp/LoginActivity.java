package com.cesde.universidadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    EditText etusuario, etcontrasena;
    Button btingreso;
    String usuario,contrasena, coleccion = "Estudiante", clave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Asociar los objetos Java con los objetos Xml
        etusuario = findViewById(R.id.etusuario);
        etcontrasena = findViewById(R.id.etcontrasena);
        btingreso = findViewById(R.id.btingreso);
        etusuario.requestFocus();
    }//fin metodo onCreate

    public void Ingresar(View view){
        ConsultarDocumento();
    }

    private void ConsultarDocumento(){
        usuario = etusuario.getText().toString();

        if (!usuario.isEmpty()){
            db.collection(coleccion)
                    .whereEqualTo("usuario", usuario)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        clave = document.getId();
                                        Intent intIngreso = new Intent(LoginActivity.this, Ingreso.class);
                                        startActivity(intIngreso);
                                    }
                                }else{
                                    //No encotro docuentos
                                    Toast.makeText(LoginActivity.this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "El usuario es requerido", Toast.LENGTH_SHORT).show();
            etusuario.requestFocus();
        }
    }//Fin metodo ConsultarRegistro()

}