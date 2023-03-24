package com.example.financicom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Arrays para los Spiners
    String[] cuotas = {"12", "24"};
    String[] prestamos = {"Vivienda", "Vehiculo"};
    //La variable que contendrá los items selecionados de los arrays
    int selecCuotas;
    String selecPrestamos;
    //Instaciar los elementos que tiene id
    EditText valorPrestamo, fecha;
    TextView valorDeuda, valorCuota;
    Spinner numCuotas, tipoPrestamo;
    Button calcular, limpiar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referenciar los objetos instaciados
        valorPrestamo = findViewById(R.id.etvarPrestamo);
        fecha = findViewById(R.id.etfecha);
        valorDeuda = findViewById(R.id.tvvalorDeuda);
        valorCuota = findViewById(R.id.tvvalorCuota);
        numCuotas = findViewById(R.id.spnumCuotas);
        tipoPrestamo = findViewById(R.id.sptipoPrestamo);
        calcular = findViewById(R.id.btncalcular);
        limpiar = findViewById(R.id.btnlimpiar);

        //Crear los ArrayAdapte (Adaptadores) para recibir la infomacion del array
        ArrayAdapter adpCuotas = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, cuotas);
        numCuotas.setAdapter(adpCuotas);
        ArrayAdapter adpPrestamos = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, prestamos);
        tipoPrestamo.setAdapter(adpPrestamos);

        //Generar el evento para cuando se seleccione uno de los item disponibles
        numCuotas.setOnItemSelectedListener(this);
        tipoPrestamo.setOnItemSelectedListener(this);

        //Evento del boton limpiar
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eliminar los elementos asociados VarlorPrestamos y Fecha
                valorPrestamo.setText("");
                fecha.setText("");
                valorPrestamo.requestFocus(); //Enviar el foco al elemento referenciado 'valorPrestamo'
            }
        });

        //Evento para el boton calcular
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar si todos los datos se haya diligenciados
                String mvalorPrestamo = valorPrestamo.getText().toString();
                String mfecha = fecha.getText().toString();

                if(!mvalorPrestamo.isEmpty() && !mfecha.isEmpty()){
                    double interes = 0;

                    switch (selecPrestamos){
                        case "Vivienda":
                            interes = 0.1;
                            break;
                        case "Vehiculo":
                            interes = 1.5;
                    }

                    //Hallar el valor deuda
                    double valorDeudaTotal = Double.valueOf(mvalorPrestamo) + interes;
                    //Hallar el valor cuota
                    double valorCuotaTotal = valorDeudaTotal / selecCuotas;

                    DecimalFormat numdecilformat = new DecimalFormat("###,###,###,###.##");

                    //Mostrando los valores
                    valorDeuda.setText(numdecilformat.format(valorDeudaTotal)); //El valor de la deuda
                    valorCuota.setText(numdecilformat.format(valorCuotaTotal)); //El valor de la cuota
                }else{
                    Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selecCuotas = Integer.parseInt(cuotas[position]);
        selecPrestamos = prestamos[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}