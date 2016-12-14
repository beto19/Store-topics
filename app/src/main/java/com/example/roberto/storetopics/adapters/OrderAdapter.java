package com.example.roberto.storetopics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roberto.storetopics.R;
import com.example.roberto.storetopics.models.Orders;

import java.util.List;

/**
 * Created by roberto on 25/11/16.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Orders> orden;

    public OrderAdapter(Context context, List<Orders> orden) {
        super();
        this.context = context;
        this.orden = orden;
    }

    @Override
    public int getCount() {
        return this.orden.size();
    }

    @Override
    public Object getItem(int position) {
        return this.orden.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView= convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_ordenes, null);
        }
        TextView tvOrden=(TextView)rowView.findViewById(R.id.tvOrden);
        TextView tvFecha=(TextView)rowView.findViewById(R.id.tvFecha);
        TextView tvTotProductos=(TextView)rowView.findViewById(R.id.tvTotProductos);
        TextView tvTotCompra=(TextView)rowView.findViewById(R.id.tvTotCompra);
        TextView tvTipoPago=(TextView)rowView.findViewById(R.id.tvTipoPago);
        TextView tvNombre=(TextView)rowView.findViewById(R.id.tvNombre);
        TextView tvEmail=(TextView)rowView.findViewById(R.id.tvEmail);
        final Orders item = this.orden.get(position);


        tvOrden.setText(Integer.toString(item.getNumOrden()));
        tvFecha.setText(item.getFecha());
        tvTotProductos.setText(Integer.toString(item.getCantidadItems()));
        tvTotCompra.setText(item.getTotal());
        String nombre=item.getFirstName()+" "+item.getLastName();
        tvNombre.setText(nombre);
        tvEmail.setText(item.getEmail());
        tvTipoPago.setText(item.getTipoPago());
        rowView.setTag(item.getIdProducto());//idProducto de la clase Orders solo es el id de la orden
        return rowView;
    }
}
