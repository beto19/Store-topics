package com.example.roberto.storetopics.customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.roberto.storetopics.MainActivity;
import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.utils.RestApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ShippingFormActivity extends AppCompatActivity implements Response.Listener<JSONObject>,View.OnClickListener {
    ProgressDialog procesando;
    RestApi api;
    boolean opcion;
    RequestQueue tarea;
    EditText Nombres, Apellidos, Direccion, Ciudad, Estado, CP, Pais, Email, Telefono;
    private ProgressDialog progressDialog;
    Button boton;
    int idTipoPago;
    String metodoPagoID, nombrePago;
    boolean autenticado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_form);
        autenticado=getIntent().getBooleanExtra("autenticado",autenticado);
        boton = (Button) findViewById(R.id.btnBuyBuy);
        boton.setOnClickListener(listener);
        api = new RestApi(this);
        tarea = Volley.newRequestQueue(this);
        procesando = new ProgressDialog(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listenerSpinner);
    }


    AdapterView.OnItemSelectedListener listenerSpinner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getBaseContext(),String.valueOf(parent.getItemIdAtPosition(position)),Toast.LENGTH_SHORT).show();
            idTipoPago = (int) parent.getItemIdAtPosition(position);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

        public JSONObject getJson() {
            JSONObject json = new JSONObject();
            JSONObject jsonObject=new JSONObject();
            JSONObject direccion1 = new JSONObject();
            JSONObject direccion2 = new JSONObject();
            JSONObject detallesDePago = new JSONObject();
            JSONObject lineItemsChild = new JSONObject();

            JSONObject payment_details = new JSONObject();
            JSONObject billing_address = new JSONObject();
            JSONObject shipping_address = new JSONObject();
            JSONObject customer_id = new JSONObject();
            JSONObject line_items = new JSONObject();
            JSONArray line_items_array = new JSONArray();
            JSONArray arreglo=new JSONArray();
            try {

                detallesDePago.put("method_id", metodoPagoID);
                detallesDePago.put("method_title", nombrePago);
                //payment_details.put("paymen_details", detallesDePago);

                direccion1.put("first_name", Nombres.getText().toString());
                direccion1.put("last_name", Apellidos.getText().toString());
                direccion1.put("address_1", Direccion.getText().toString());
                direccion1.put("city", Ciudad.getText().toString());
                direccion1.put("state", Estado.getText().toString());
                direccion1.put("postcode", CP.getText().toString());
                direccion1.put("country", Pais.getText().toString());
                direccion1.put("email", Email.getText().toString());
                direccion1.put("phone", Telefono.getText().toString());
                //billing_address.put("billing_address", direccion1);

                direccion2.put("first_name", Nombres.getText().toString());
                direccion2.put("last_name", Apellidos.getText().toString());
                direccion2.put("address_1", Direccion.getText().toString());
                direccion2.put("city", Ciudad.getText().toString());
                direccion2.put("state", Estado.getText().toString());
                direccion2.put("postcode", CP.getText().toString());
                direccion2.put("country", Pais.getText().toString());
                //shipping_address.put("shipping_address", direccion2);

                customer_id.put("customer_id", 0);

                for (int i = 0; i < AgregarCarritoActivity.arrayProducts.size(); i++) {
                    lineItemsChild.put("product_id", AgregarCarritoActivity.arrayProducts.get(i).getId());
                    lineItemsChild.put("quantity", AgregarCarritoActivity.arrayProducts.get(i).getCantidad());
                    line_items_array.put(lineItemsChild);
                }


                json.put("payment_details",detallesDePago);
                json.put("billing_address",direccion1);
                json.put("shipping_address",direccion2);
                json.put("customer_id",0);
                json.put("line_items",line_items_array);
                jsonObject.put("order",json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(this,jsonObject.toString(),Toast.LENGTH_SHORT).show();
            return jsonObject;
        }

        public void prueba()
        {
            String json;
            json=getJson().toString();
            Toast.makeText(getBaseContext(),json,Toast.LENGTH_SHORT).show();
        }

        public void inserta() {
            procesando.setMessage("Guardando...");
            procesando.show();
            JSONObject data = getJson();
            JsonObjectRequest inserta = new JsonObjectRequest(Request.Method.POST, "https://storeitc-luisitoaltamira.rhcloud.com/wc-api/v3/orders", data, this, api) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return api.headers();
                }
            };

            tarea.add(inserta);
            tarea.start();
            AgregarCarritoActivity.arrayProducts.clear();
            if(autenticado)
            {
                Intent intent=new Intent(getBaseContext(),ClienteActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }

        }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Nombres=(EditText)findViewById(R.id.txtFirstName);
            Apellidos=(EditText)findViewById(R.id.txtLastName);
            Direccion=(EditText)findViewById(R.id.txtAddres1);
            Ciudad=(EditText)findViewById(R.id.txtCiudad);
            Estado=(EditText)findViewById(R.id.txtEstado);
            CP=(EditText)findViewById(R.id.txtPostCode);
            Pais=(EditText)findViewById(R.id.txtCountry);
            Email=(EditText)findViewById((R.id.txtEmail));
            Telefono=(EditText)findViewById(R.id.txtTelefono);
            if(idTipoPago==0)
            {
                metodoPagoID="bacs";
                nombrePago="Transferencia Bancaria Directa";
            }
            else if(idTipoPago==1)
            {
                metodoPagoID="cheque";
                nombrePago="Pago con Cheque";
            }
            else if (idTipoPago==2)
            {
                metodoPagoID="cod";
                nombrePago="Pago en entrega";
            }
           // prueba();
            //JSONObject json = getJson();
            //Toast.makeText(getBaseContext(),json.toString(),Toast.LENGTH_SHORT).show();
            inserta();
        }
    };

    @Override
    public void onClick(View v) {



    }

    @Override
    public void onResponse(JSONObject response) {
        if (opcion)
        {
            procesando.hide();
        }else{
            procesando.cancel();
            procesando = null;
            finish();
        }
    }
}

