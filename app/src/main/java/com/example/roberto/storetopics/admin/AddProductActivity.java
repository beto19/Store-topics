package com.example.roberto.storetopics.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.roberto.storetopics.MainActivity;
import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.models.Product;
import com.example.roberto.storetopics.utils.RestApi;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by roberto on 14/12/16.
 */

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener,Response.Listener<JSONObject> {

    private RequestQueue tarea;
    private ProgressDialog procesando;
    private RestApi api;
    EditText editTitle,editPrice,editDescription,editStock;
    private Product product;
    private Button btnGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_edit_product);
        tarea = Volley.newRequestQueue(this);
        procesando = new ProgressDialog(this);
        api = new RestApi(this);
        editTitle = (EditText) findViewById(R.id.editTitleProduct);
        editPrice = (EditText) findViewById(R.id.editPriceProduct);
        editDescription  = (EditText) findViewById(R.id.editDescription);
        TextView t = (TextView) findViewById(R.id.textView11);
        procesando.setMessage("Creando...");
        t.setText("");
        TextView t1 = (TextView) findViewById(R.id.textView12);
        t1.setText("");
        btnGuardar = (Button) findViewById(R.id.btnGuardarProduct);
        btnGuardar.setOnClickListener(this);
        product = new Product();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this,"Producto creado con exito",Toast.LENGTH_SHORT).show();
        procesando.hide();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        procesando.cancel();
        procesando = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnGuardarProduct)
            guardar();
    }
    private void guardar()
    {
        procesando.show();
        product.setTitle(editTitle.getText().toString());
        product.setDescription(editDescription.getText().toString());
        product.setPrice(editPrice.getText().toString());
        JSONObject data = product.getJson();
        JsonObjectRequest create = new JsonObjectRequest(Request.Method.POST, MainActivity.url_principal+"/wc-api/v3/products",data,this,api)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return api.headers();
            }
        };
        tarea.add(create);
        tarea.start();
    }
}
