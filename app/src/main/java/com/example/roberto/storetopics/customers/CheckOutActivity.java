package com.example.roberto.storetopics.customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.adapters.CheckoutAdpater;
import com.example.roberto.storetopics.adapters.ProductsAdapter;
import com.example.roberto.storetopics.models.Carrito;
import com.example.roberto.storetopics.models.Products;
import com.example.roberto.storetopics.utils.LoadOrdersTask;
import com.example.roberto.storetopics.utils.NukeSSLCerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckOutActivity extends AppCompatActivity {
    String consumer_key = "ck_a64ead43b826563f0e0fad044920da0f166ae7fe";
    String consumer_secret = "cs_0b6239dded2cbf1bc56bbbae5a49e913842123b9";
    String url = "https://storeitc-luisitoaltamira.rhcloud.com/wc-api/v3/products";
    String jsonResult;
    List<Products> items = new ArrayList<Products>();
    ListView listProducts;
    ArrayList<Carrito> arrayProducts;
    Button boton;
    TextView Total;
    double total;
    boolean autenticado;
    int idCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        listProducts = (ListView) findViewById(R.id.list_checkProductos);
        idCliente=getIntent().getIntExtra("id",-1);
        autenticado=getIntent().getBooleanExtra("autenticado",false);
        boton=(Button)findViewById(R.id.btnBuy);
        boton.setOnClickListener(btnListener);
        arrayProducts=AgregarCarritoActivity.arrayProducts;
        NukeSSLCerts.nuke();
        //pruebaCarrito();
        loadProducts();


    }

    View.OnClickListener btnListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(autenticado)
            {

                Intent intent=new Intent(getBaseContext(),ShippingFormCustomerActivity.class);
                intent.putExtra("autenticado",autenticado);
                intent.putExtra("id",idCliente);
                //Toast.makeText(getBaseContext(), "autenticadopaps:"+idCliente, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getBaseContext(), ShippingFormActivity.class);
                intent.putExtra("autenticado", autenticado);
                startActivity(intent);
            }
        }
    };

    private void loadProducts() {
        LoadOrdersTask tarea = new LoadOrdersTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        ListProductos();

    }

    public void pruebaCarrito()
    {
        Carrito producto;
        //Toast.makeText(getBaseContext(),""+arrayProducts.size(),Toast.LENGTH_SHORT).show();
        for (int i = 0; i <arrayProducts.size() ; i++) {

            producto=arrayProducts.get(i);
            Toast.makeText(getBaseContext(),""+producto.getId(),Toast.LENGTH_SHORT).show();

        }
    }

    public void ListProductos() {

        try {
            //lbl1.setText(jsonResult);

            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");
            total=0;
            for (int j = 0; j <arrayProducts.size() ; j++) {


                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    Integer id_product = jsonChildNode.optInt("id");
                    if (arrayProducts.get(j).getId().equalsIgnoreCase(id_product + "")) {
                        String name = jsonChildNode.optString("title");
                        String type = jsonChildNode.optString("type");
                        Double price = jsonChildNode.optDouble("price");
                        String ImageURL = jsonChildNode.optString("featured_src");
                        total+=arrayProducts.get(j).getPrecio();
                        items.add(new Products(id_product, name, type, price, ImageURL));
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        listProducts.setAdapter(new ProductsAdapter(this, items));
        Total=(TextView)findViewById(R.id.tvTotal);
        Total.setText(""+total);


    }
}
