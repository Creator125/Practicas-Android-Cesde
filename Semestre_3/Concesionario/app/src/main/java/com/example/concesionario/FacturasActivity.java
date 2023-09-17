package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacturasActivity extends AppCompatActivity {
    EditText etcodigo,etfecha,etidentificacion,etplaca;
    TextView tvnombre,tvtelefono,tvmarca,tvvalor;
    CheckBox cbactivo;
    Button btadicionar,btanular;
    String codigo, idenCliente, placa;
    ClsOpenHelper admin = new ClsOpenHelper(this,"Concesionario.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);

        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();

        //Asociar los objetos Java con los objetos Xml
        etcodigo = findViewById(R.id.etcodigo);
        etfecha = findViewById(R.id.etfecha);
        etidentificacion = findViewById(R.id.etidentificacion);
        etplaca = findViewById(R.id.etplaca);
        tvnombre = findViewById(R.id.tvnombre);
        tvtelefono = findViewById(R.id.tvtelefono);
        tvmarca = findViewById(R.id.tvmarca);
        tvvalor = findViewById(R.id.tvvalor);
        cbactivo = findViewById(R.id.cbactivo);
        btadicionar = findViewById(R.id.btadicionar);
        btanular = findViewById(R.id.btanular);
        etcodigo.requestFocus();
    }//Fin metodo onCreate

    public void Consultar(View view){
        codigo = etcodigo.getText().toString();

        if(!codigo.isEmpty()){
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor registro = db.rawQuery("SELECT Fecha, TblFacturas.IdCliente, " +
                    "NomCliente, TelCliente, TblDetalle_Factura.Placa, Marca, Valor, " +
                    "TblFacturas.Activo FROM TblClientes INNER JOIN TblFacturas ON " +
                    "TblClientes.IdCliente = TblFacturas.IdCliente INNER JOIN " +
                    "TblDetalle_Factura ON TblFacturas.CodFacturas =" +
                    "TblDetalle_Factura.CodFactura INNER JOIN TblVehiculos ON " +
                    "TblDetalle_Factura.Placa = TblVehiculos.Placa WHERE " +
                    "TblFacturas.CodFacturas = '"+codigo+"'", null);

            if (registro.moveToNext()){
                etfecha.setText(registro.getString(0));
                etidentificacion.setText(registro.getString(1));
                tvnombre.setText(registro.getString(2));
                tvvalor.setText(registro.getString(7));

                if (registro.getString(8).equals("Si")){
                    cbactivo.setChecked(true);
                }else{
                    cbactivo.setChecked(false);
                }
            }else{
                //Comienza el trabajo de ustedes
                Toast.makeText(this, "Factura no registrada", Toast.LENGTH_SHORT).show();
                etfecha.requestFocus();
            }

            db.close();
        }else{
            Toast.makeText(this, "Codigo requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    }//Fin metodo Consultar

    //Metodo de ConsultarCliente
    public void ConsultarCliente(View view) {
        idenCliente = etidentificacion.getText().toString();

        //Validar que los campos no estan vacios
        if (idenCliente.isEmpty()) {
            Toast.makeText(this, "La identificacion del cliente es requerida", Toast.LENGTH_SHORT).show();
            etidentificacion.requestFocus();
        } else {
            //abrir base de datos en modo lectura
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor registro = db.rawQuery("select * from TblClientes where IdCliente='" + idenCliente + "'", null);

            if (registro.moveToNext()) {
                //el registro existe
                tvnombre.setText(registro.getString(1));
                tvtelefono.setText(registro.getString(3));
            } else {
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
            }

            db.close();
        }//Fin metodo ConsultarCliente
    }

    //Metodo de consultar Vehiculos
    public void ConsultarVehiculo(View view) {
        placa = etplaca.getText().toString();

        //Validar que los campos no estan vacios
        if (placa.isEmpty()) {
            Toast.makeText(this, "La identificacion del cliente es requerida", Toast.LENGTH_SHORT).show();
            etplaca.requestFocus();
        } else {
            //abrir base de datos en modo lectura
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor registro = db.rawQuery("select * from TblVehiculos where Placa='" + placa + "'", null);

            if (registro.moveToNext()) {
                //el registro existe
                tvmarca.setText(registro.getString(2));
                tvvalor.setText(registro.getString(3));
            } else {
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
            }

            db.close();
        }//Fin metodo de ConsultarVehiculo
    }
}