package com.example.roberto.storetopics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roberto.storetopics.adapters.ProductsAdapter;
import com.example.roberto.storetopics.customers.AgregarCarritoActivity;
import com.example.roberto.storetopics.customers.CheckOutActivity;
import com.example.roberto.storetopics.customers.FiltroActivity;
import com.example.roberto.storetopics.models.Products;
import com.example.roberto.storetopics.utils.LoadOrdersTask;
import com.example.roberto.storetopics.utils.NukeSSLCerts;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //ArrayList<String> arrayProducts;
    String consumer_key = "ck_a64ead43b826563f0e0fad044920da0f166ae7fe";
    String consumer_secret = "cs_0b6239dded2cbf1bc56bbbae5a49e913842123b9";
    String url = "https://storeitc-luisitoaltamira.rhcloud.com/wc-api/v3/products";
    String jsonResult;
    public static final String url_principal = "https://storeitc-luisitoaltamira.rhcloud.com";

    List<Products> items = new ArrayList<Products>();
    ListView listProducts;
    ArrayList<String> arrayProducts;
    Products p;
    boolean autenticado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listProducts = (ListView) findViewById(R.id.listProductos);
        listProducts.setOnItemClickListener(listenerProductos);
        NukeSSLCerts.nuke();
        loadProducts();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //arrayProducts = new ArrayList<String>();
        //arrayProducts=(ArrayList<String>)getIntent().getSerializableExtra("arrayProducts");



    }

    AdapterView.OnItemClickListener listenerProductos= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            p = items.get(position);
            //Toast.makeText(getBaseContext(), "id:"+p.getId(), Toast.LENGTH_SHORT).show();
            //loadReviews(p.getId());
            Intent intent=new Intent(getBaseContext(),AgregarCarritoActivity.class);
            intent.putExtra("idProducto",""+p.getId());
            intent.putExtra("precio",p.getPrice());
            intent.putExtra("nombre",p.getTitle());
            intent.putExtra("imagen",p.getImageUrl());
            intent.putExtra("type",p.getType());
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
                String name = jsonChildNode.optString("title");
                String type = jsonChildNode.optString("type");
                Double price = jsonChildNode.optDouble("price");
                String ImageURL = jsonChildNode.optString("featured_src");
                items.add(new Products(id_product, name, type, price, ImageURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }

        listProducts.setAdapter(new ProductsAdapter(this, items));



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.n3ds) {
            listProducts("3DS");
        } else if (id == R.id.wiiu) {
            listProducts("Wii u");
        } else if (id == R.id.ps3) {
            listProducts("PS3");
        } else if (id == R.id.ps4) {
            listProducts("PS4");
        } else if (id == R.id.xbox360) {
            listProducts("Xbox 360");
        } else if (id == R.id.xboxOne) {
            listProducts("Xbox One");
        } else if (id == R.id.funkos){
            listProducts("Funko");
        }else if (id == R.id.playeras){
            listProducts("Playeras");
        }else if (id==R.id.nav_share){
            //login
            Intent intent=new Intent(getBaseContext(),LoginActivity.class);
            startActivity(intent);

            finish();
        }else if(id==R.id.nav_send){
            //sign in
        }else if(id==R.id.carrito){
            Intent intent=new Intent(getBaseContext(),CheckOutActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    AdapterView.OnItemClickListener listenerListView=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getBaseContext(),FiltroActivity.class);
            intent.putExtra("arrayProducts",arrayProducts);
            startActivity(intent);
        }
    };

    public void listProducts(String status) {
        arrayProducts=new ArrayList<String>();

        try {
            //lbl1.setText(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                JSONArray jsonMainProductos =  jsonChildNode.optJSONArray("categories");
                String categoria=jsonMainProductos.optString(0);
                //Toast.makeText(getApplicationContext(), categoria,Toast.LENGTH_SHORT).show();
                if (categoria.equals(status))
                {
                    Integer id_product = jsonChildNode.optInt("id");
                    arrayProducts.add(Integer.toString(id_product));
                    //Toast.makeText(getApplicationContext(), Integer.toString(id_product),Toast.LENGTH_SHORT).show();
                }
            }
            Intent intent=new Intent(getBaseContext(),FiltroActivity.class);
            intent.putExtra("arrayProducts",arrayProducts);
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
