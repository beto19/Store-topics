package com.example.roberto.storetopics.customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.adapters.OrderAdapter;
import com.example.roberto.storetopics.models.Orders;
import com.example.roberto.storetopics.utils.LoadOrdersTask;
import com.example.roberto.storetopics.utils.NukeSSLCerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StatusActivity extends AppCompatActivity {
    String consumer_key = "ck_a64ead43b826563f0e0fad044920da0f166ae7fe";
    String consumer_secret = "cs_0b6239dded2cbf1bc56bbbae5a49e913842123b9";
    String jsonResult;
    String url = "https://storeitc-luisitoaltamira.rhcloud.com/wc-api/v3/orders/";
    List<Orders> items = new ArrayList<Orders>();
    ListView listOrders;
    ArrayList<String> arrayProducts;
    ListView listProducts;
    int idCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        idCliente=getIntent().getIntExtra("idCliente",-1);
        listOrders=(ListView) findViewById(R.id.listOrders);
        Integer id=getIntent().getIntExtra("id",0);
        listProducts = (ListView) findViewById(R.id.listProductos);
        NukeSSLCerts.nuke();
        loadOrders(id);

    }


    private void loadOrders(int id) {
        LoadOrdersTask tarea = new LoadOrdersTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_SHORT).show();
        //ListOrders();
        selector(id);

    }

    public void selector (int idStatus)
    {
        switch(idStatus)
        {
            case 0:
                ListOrders("completed");
                break;
            case 1:
                ListOrders("processing");
                break;
            case 2:
                ListOrders("cancelled");
                break;
            case 3:
                ListOrders("on-hold");
                break;
            case 4:
                ListOrders("pending");
                break;
            case 5:
                ListOrders("refunded");
                break;
            case 6:
                ListOrders("failed");
                break;
        }
    }



    public void ListOrders(String status) {
        arrayProducts=new ArrayList<String>();
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("orders");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String statusCaso = jsonChildNode.optString("status");
                    Integer idCustomer=jsonChildNode.optInt("customer_id");
                if(status.equals(statusCaso) && idCustomer==idCliente) {
                    Integer id = jsonChildNode.optInt("id");
                    Integer numeroOrden = jsonChildNode.optInt("order_number");
                    String fecha = jsonChildNode.optString("completed_at");
                    Integer cantidadItems = jsonChildNode.optInt("total_line_items_quantity");
                    String total = jsonChildNode.optString("total");

                    JSONObject tipoPagoObjeto = jsonChildNode.getJSONObject("payment_details");
                    //JSONArray jsonSubNode = tipoPagoObjeto.optJSONArray("payment_details"); //tipoPagoObjeto.optJSONArray("payment_details");
                    //JSONObject jsonPayment = jsonSubNode.getJSONObject(0);
                    String tipoPago = tipoPagoObjeto.optString("method_title");

                    JSONObject personaObjeto = jsonChildNode.getJSONObject("billing_address");
                    String firstName = personaObjeto.optString("first_name");
                    String lastName = personaObjeto.optString("last_name");
                    String email = personaObjeto.optString("email");

                    JSONArray jsonMainProductos=  jsonChildNode.getJSONArray("line_items");

                    for(int j=0;j<jsonMainProductos.length();j++)
                    {
                        JSONObject jsonProducto = jsonMainProductos.optJSONObject(j);
                        arrayProducts.add(Integer.toString(jsonProducto.optInt("product_id")));

                    }
                    items.add(new Orders(id, numeroOrden, fecha, cantidadItems, total, tipoPago, firstName, lastName, email));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        listOrders.setAdapter(new OrderAdapter(this, items));

    }


}
