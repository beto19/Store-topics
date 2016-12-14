package com.example.roberto.storetopics.customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.adapters.ProductsAdapter;
import com.example.roberto.storetopics.models.Products;
import com.example.roberto.storetopics.utils.LoadOrdersTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FiltroActivity extends AppCompatActivity {
    ArrayList<String> arrayProducts;
    String consumer_key = "ck_a64ead43b826563f0e0fad044920da0f166ae7fe";
    String consumer_secret = "cs_0b6239dded2cbf1bc56bbbae5a49e913842123b9";
    String url = "https://storeitc-luisitoaltamira.rhcloud.com/wc-api/v3/products";
    String jsonResult;
    List<Products> items = new ArrayList<Products>();
    ListView listProducts;
    Products p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        listProducts=(ListView)findViewById(R.id.listFiltro);
        listProducts.setOnItemClickListener(listenerp);
        arrayProducts = new ArrayList<String>();
        arrayProducts=(ArrayList<String>)getIntent().getSerializableExtra("arrayProducts");
        NukeSSLCerts.nuke();
        loadProducts();
    }

    AdapterView.OnItemClickListener listenerp=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            p=items.get(position);
            //Toast.makeText(getBaseContext(),""+p.getPrice(),Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getBaseContext(),AgregarCarritoActivity.class);
            intent.putExtra("idProducto",""+p.getId());
            intent.putExtra("precio",p.getPrice());
            intent.putExtra("nombre",p.getTitle());
            intent.putExtra("imagen",p.getImageUrl());
            intent.putExtra("type",p.getType());
            startActivity(intent);
        }
    };

    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    private void loadProducts() {
        LoadOrdersTask tarea = new LoadOrdersTask(this, consumer_key, consumer_secret);
        try {
            jsonResult = tarea.execute(new String[] { url }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(), jsonResult, Toast.LENGTH_LONG).show();
        ListProductos();

    }

    public void ListProductos() {

        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                Integer id_product = jsonChildNode.optInt("id");
                for (int j = 0; j <arrayProducts.size() ; j++)
                {
                    if(id_product==Integer.parseInt(arrayProducts.get(j)))
                    {
                        String name = jsonChildNode.optString("title");
                        String type = jsonChildNode.optString("type");
                        Double price = jsonChildNode.optDouble("price");
                        String ImageURL = jsonChildNode.optString("featured_src");
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



    }
}
