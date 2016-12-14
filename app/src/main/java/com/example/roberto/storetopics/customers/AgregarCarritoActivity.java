package com.example.roberto.storetopics.customers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roberto.storetopics.MainActivity;
import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.models.Carrito;
import com.example.roberto.storetopics.models.Products;
import com.example.roberto.storetopics.utils.NukeSSLCerts;

import java.util.ArrayList;

public class AgregarCarritoActivity extends AppCompatActivity {

    String idProducto,nombre,imagen,type;
    double precio,cantidadTotal;
    TextView nombreCarrito,precioCarrito;
    EditText cantidad;
    double total;
    Button boton;
    boolean autenticado;
    int idCliente;
    static ArrayList <Carrito> arrayProducts=new ArrayList<Carrito>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_carrito);
        autenticado=getIntent().getBooleanExtra("autenticado",false);
        NukeSSLCerts.nuke();

        idCliente=getIntent().getIntExtra("idCliente",1);
        idProducto=getIntent().getStringExtra("idProducto");
        precio=getIntent().getDoubleExtra("precio",0);
        nombre=getIntent().getStringExtra("nombre");
        imagen=getIntent().getStringExtra("imagen");
        type=getIntent().getStringExtra("type");

        nombreCarrito=(TextView)findViewById(R.id.tvNombreCarrito);
        precioCarrito=(TextView)findViewById(R.id.tvPrecioCarrito);
        cantidad=(EditText)findViewById(R.id.txtCantidad);
        boton=(Button)findViewById(R.id.btnAgregar);
        boton.setOnClickListener(btnListener);

        nombreCarrito.setText(nombre);
        precioCarrito.setText(String.valueOf(precio));


    }

 View.OnClickListener btnListener=new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Toast.makeText(getBaseContext(),idProducto,Toast.LENGTH_SHORT).show();
         cantidadTotal=Double.parseDouble(cantidad.getText().toString());
         if(cantidad.getText().toString().isEmpty()) {
             Toast.makeText(getBaseContext(),"Debe introducir una cantidad",Toast.LENGTH_SHORT).show();
         }
         else
         {
             total = 0;
             total = (double) precio * cantidadTotal;
             //Toast.makeText(getBaseContext(),""+total,Toast.LENGTH_SHORT).show();
             arrayProducts.add(new Carrito(idProducto, Integer.parseInt(cantidad.getText().toString()), total));
             //Toast.makeText(getBaseContext(),""+arrayProducts.size(),Toast.LENGTH_SHORT).show();
             if(autenticado)
             {
                 Intent intent=new Intent(getBaseContext(), ClienteActivity.class);
                 intent.putExtra("idCliente",idCliente);
                 startActivity(intent);

             }else {
                 Intent intent = new Intent(getBaseContext(), MainActivity.class);
                 startActivity(intent);
             }
         }
     }
 };
}
