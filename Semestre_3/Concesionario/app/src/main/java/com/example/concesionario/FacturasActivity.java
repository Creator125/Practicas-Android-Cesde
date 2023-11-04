package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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
    String codigo, idenCliente, placa, fecha, codigoFactura, vehiculoActivo, valorRenta;
    ClsOpenHelper admin = new ClsOpenHelper(this,"Concesionario.db",null,1);
    long respuesta1, respuesta2, respuesta;
    boolean sw;

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
        sw = false;
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
        }
    }//Fin metodo de ConsultarVehiculo

    //Metodo de adicionar
    public void Adicionar(View view){
        codigoFactura = etcodigo.getText().toString();
        fecha = etfecha.getText().toString();
        idenCliente = etidentificacion.getText().toString();
        placa = etplaca.getText().toString();
        valorRenta = tvvalor.getText().toString();

        //Consultar la tabla de vehiculos

        SQLiteDatabase DB = admin.getReadableDatabase();
        /*Cursor registro= db.rawQuery("select * from TblVehiculos where Placa='"+placa+"'",null);
        boolean vehiculoDisponible = false;

        if(registro.moveToNext()){
            if(registro.getString(4).equals("Si")){
                vehiculoDisponible = true;
            }
        }else{
            vehiculoDisponible = false;
        }

         */

        DB.close();
            //Validar que los campos no estan vacios
            if (!fecha.isEmpty() && !idenCliente.isEmpty() && !placa.isEmpty()){
                SQLiteDatabase db = admin.getWritableDatabase();
                ContentValues filaFactura = new ContentValues();
                ContentValues filaDetalleFactura = new ContentValues();

                // Llenar los ContentValues con los datos correspondientes
                filaFactura.put("CodFacturas", codigoFactura);
                filaFactura.put("Fecha", fecha);
                filaFactura.put("IdCliente", idenCliente);

                filaDetalleFactura.put("CodFactura", codigoFactura);
                filaDetalleFactura.put("Placa", placa);
                filaDetalleFactura.put("ValorRenta", valorRenta);

                respuesta1=db.insert("TblFacturas",null,filaFactura);
                respuesta2=db.insert("TblDetalle_Factura",null,filaDetalleFactura);

                if (respuesta1 > 0 && respuesta2 > 0){
                    Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                    anularVehiculo();
                    //Limpiar_campos();
                }else {
                    Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
                etfecha.requestFocus();
            }

    }

    //Metodo para anular vehiculo
    private void anularVehiculo(){
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues fila = new ContentValues();
        fila.put("Activo", "No");
        respuesta = db.update("TblVehiculos", fila, "Placa = '"+placa+"'", null);
    }//Fin metodo anularVehiculo
}