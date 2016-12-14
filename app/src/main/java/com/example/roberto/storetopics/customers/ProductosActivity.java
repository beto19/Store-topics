package com.example.roberto.storetopics.customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.adapters.ProductsAdapter;
import com.example.roberto.storetopics.models.Products;
import com.example.roberto.storetopics.utils.LoadOrdersTask;
import com.example.roberto.storetopics.utils.NukeSSLCerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ProductosActivity extends AppCompatActivity {
    String consumer_key = "ck_a64ead43b826563f0e0fad044920da0f166ae7fe";
    String consumer_secret = "cs_0b6239dded2cbf1bc56bbbae5a49e913842123b9";
    String url = "https://storeitc-luisitoaltamira.rhcloud.com/wc-api/v3/products";
    String jsonResult;
    List<Products> items = new ArrayList<Products>();
    ListView listProducts;
    ArrayList<String> arrayProducts;
    Products p;
    String cat;
    boolean autenticado;
    int idCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        listProducts = (ListView) findViewById(R.id.listCLienteProductos);
        listProducts.setOnItemClickListener(listener);
        cat=getIntent().getStringExtra("categoria");
        idCliente=getIntent().getIntExtra("idCliente",-1);
        autenticado=getIntent().getBooleanExtra("autenticado",false);
        NukeSSLCerts.nuke();
        loadProducts();
    }

    AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            p=items.get(position);
            //Toast.makeText(getBaseContext(),""+p.getPrice(),Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getBaseContext(),AgregarCarritoActivity.class);
            intent.putExtra("idProducto",""+p.getId());
            intent.putExtra("precio",p.getPrice());
            intent.putExtra("nombre",p.getTitle());
            intent.putExtra("imagen",p.getImageUrl());
            intent.putExtra("tupe",p.getType());
            intent.putExtra("autenticado",autenticado);
            intent.putExtra("idCliente",idCliente);
            startActivity(intent);
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

    public void ListProductos() {

        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                JSONArray jsonMainProductos =  jsonChildNode.optJSONArray("categories");
                String categoria=jsonMainProductos.optString(0);
                if(cat.equalsIgnoreCase(categoria) || cat.equalsIgnoreCase(" ")) {
                    Integer id_product = jsonChildNode.optInt("id");
                    String name = jsonChildNode.optString("title");
                    String type = jsonChildNode.optString("type");
                    Double price = jsonChildNode.optDouble("price");
                    String ImageURL = jsonChildNode.optString("featured_src");
                    items.add(new Products(id_product, name, type, price, ImageURL));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        listProducts.setAdapter(new ProductsAdapter(this, items));



    }
}
