package com.example.roberto.storetopics.customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roberto.storetopics.R;

public class SelectStatusActivity extends AppCompatActivity {
    Integer idStatus;
    Button btnOrder;
    int idCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_status);
        idCliente=getIntent().getIntExtra("id",-1);
        Button boton=(Button)findViewById(R.id.btnOrder);
        Spinner spinner = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statusOrden, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listenerSpinner);
        boton.setOnClickListener(clickListener);


    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //Toast.makeText(getBaseContext(),String.valueOf(idStatus),Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getBaseContext(),StatusActivity.class);
            intent.putExtra("id",idStatus);
            intent.putExtra("idCliente",idCliente);
            startActivity(intent);

        }
    };

    AdapterView.OnItemSelectedListener listenerSpinner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getBaseContext(),String.valueOf(parent.getItemIdAtPosition(position)),Toast.LENGTH_SHORT).show();
            idStatus=(int) parent.getItemIdAtPosition(position);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
