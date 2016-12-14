package com.example.roberto.storetopics.customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.roberto.storetopics.MainActivity;
import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.models.Customer;
import com.example.roberto.storetopics.utils.RestApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ShippingFormCustomerActivity extends AppCompatActivity implements Response.Listener<JSONObject>, View.OnClickListener {
    private Customer customer;
    private ProgressDialog procesando;
    RequestQueue tarea;
    RequestQueue tareaPost;
    RestApi api;
    String direccion;
    String postcode;
    String metodoPagoID,nombrePago;
    Button btnPagarCarrito;
    int idTipoPago,idCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_form_customer);
        idCliente=getIntent().getIntExtra("id",-1);
        customer=new Customer(idCliente);
        api=new RestApi(this);
        tarea= Volley.newRequestQueue(this);
        btnPagarCarrito=(Button)findViewById(R.id.btnPagarCarrito);
        btnPagarCarrito.setOnClickListener(this);
        procesando = new ProgressDialog(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listenerSpinner);
        cargar();
        procesando.setMessage("Cargando...");
    }
    //Cargo los datos
    private void cargar ()
    {
        procesando.show();
        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(Request.Method.GET,
                "https://storeitc-luisitoaltamira.rhcloud.com/wc-api/v3/customers/"+idCliente,null,
                this,api)
        {
            @Override
            public Map<String,String> getHeaders ()throws AuthFailureError
            {
                return api.headers();
            }
        };
        tarea.add(jsonObjectRequest);
        tarea.start();
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
            Intent intent=new Intent(getBaseContext(),ClienteActivity.class);
            intent.putExtra("idCliente",idCliente);
            startActivity(intent);
    }


    public void data (JSONObject json)
    {
        try {
            JSONObject jsonCustomer = json.getJSONObject("customer");

            customer.setLast_name(jsonCustomer.getString("last_name"));
            customer.setUsername(jsonCustomer.getString("username"));
            customer.setFirst_name(jsonCustomer.getString("first_name"));
            customer.setEmail(jsonCustomer.getString("email"));

            JSONObject jsonBiladdr = jsonCustomer.getJSONObject("billing_address");
            customer.setCity(jsonBiladdr.getString("city"));
            customer.setState(jsonBiladdr.getString("state"));
            customer.setCountry(jsonBiladdr.getString("country"));
            customer.setPhone(jsonBiladdr.getString("phone"));
            direccion=jsonBiladdr.getString("address_1");
            postcode=jsonBiladdr.getString("postcode");

        }catch (JSONException e) {
            e.printStackTrace();
        }

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

            direccion1.put("first_name", customer.getFirst_name());
            direccion1.put("last_name", customer.getLast_name());
            direccion1.put("address_1", direccion);
            direccion1.put("city", customer.getCity());
            direccion1.put("state", customer.getState());
            direccion1.put("postcode", postcode);
            direccion1.put("country", customer.getCountry());
            direccion1.put("email", customer.getEmail());
            direccion1.put("phone", customer.getPhone());
            //billing_address.put("billing_address", direccion1);

            direccion2.put("first_name", customer.getFirst_name());
            direccion2.put("last_name", customer.getLast_name());
            direccion2.put("address_1", direccion);
            direccion2.put("city", customer.getCity());
            direccion2.put("state", customer.getState());
            direccion2.put("postcode", postcode);
            direccion2.put("country", customer.getCountry());
            //shipping_address.put("shipping_address", direccion2);

            customer_id.put("customer_id", idCliente);

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

    @Override
    public void onClick(View v) {
    if (v.getId()==R.id.btnPagarCarrito) {
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

        inserta();

    }
    }

    @Override
    public void onResponse(JSONObject response) {
        data(response);
        procesando.hide();
    }
}
