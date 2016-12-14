package com.example.roberto.storetopics.customers;

import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roberto.storetopics.R;

public class ClienteActivity extends AppCompatActivity {
    int idCategoria;
    int idCliente;
    Button btnProductos,btnCarrito,btnDatosPersonales,btnOrden;
    boolean autenticado=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        idCliente=getIntent().getIntExtra("idCliente",-1);
        //Toast.makeText(this, ""+idCliente, Toast.LENGTH_SHORT).show();
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listenerSpinner);
        btnProductos=(Button)findViewById(R.id.btnMostrarProductos);
        btnProductos.setOnClickListener(listenerCategoria);
        btnCarrito=(Button)findViewById(R.id.btnCarrito);
        btnCarrito.setOnClickListener(listenerCarrito);
        btnDatosPersonales=(Button)findViewById(R.id.btnDatosPersonales);
        btnDatosPersonales.setOnClickListener(listenerDatosPersonales);
        btnOrden=(Button)findViewById(R.id.btnOrd);
        btnOrden.setOnClickListener(listenerOrder);
    }

    View.OnClickListener listenerOrder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getBaseContext(),SelectStatusActivity.class);
            intent.putExtra("id",idCliente);
            startActivity(intent);
        }
    };

    AdapterView.OnItemSelectedListener listenerSpinner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            idCategoria = (int) parent.getItemIdAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener listenerDatosPersonales=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent=new Intent(getBaseContext(),EditCustomerActivity.class);
            intent.putExtra("id",idCliente);
            startActivity(intent);
        }
    };

    View.OnClickListener listenerCarrito=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getBaseContext(),CheckOutActivity.class);
            intent.putExtra("id",idCliente);
            intent.putExtra("autenticado",autenticado);
            startActivity(intent);
        }
    };

    View.OnClickListener listenerCategoria=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (idCategoria==0)
            {
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("autenticado",autenticado);
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("categoria"," ");
                startActivity(intent);
            }else if (idCategoria==1) {
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","3DS");
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);

            }else if (idCategoria==2){
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","Wii u");
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);

            }else if (idCategoria==3){
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","PS3");
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);
            }else if (idCategoria==4){
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","PS4");
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);

            }else if (idCategoria==5){
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","Xbox 360");
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);
            }else if (idCategoria==6){
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","Xbox One");
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);
            }else if (idCategoria==7){
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","Funko");
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);
            }else if (idCategoria==8){
                Intent intent=new Intent(getBaseContext(),ProductosActivity.class);
                intent.putExtra("categoria","Playeras");
                intent.putExtra("idCliente",idCliente);
                intent.putExtra("autenticado",autenticado);
                startActivity(intent);
            }

        }
    };
}
