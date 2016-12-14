package com.example.roberto.storetopics.models;

/**
 * Created by roberto on 25/11/16.
 */

public class Orders {
    private int idProducto; //id
    private int numOrden; //order_number
    private String fecha; //completed_at
    private int cantidadItems;//total_line_items_quantity
    private String total;//total
    private String tipoPago;//payment_details->method_title
    private String firstName;
    private String lastName;
    private String email;
    //billing address->//first_name
                       //last_name
                       //email


    public Orders(int idProducto, int numOrden, String fecha, int cantidadItems, String total, String tipoPago, String firstName, String lastName, String email) {
        this.idProducto = idProducto;
        this.numOrden = numOrden;
        this.fecha = fecha;
        this.cantidadItems = cantidadItems;
        this.total = total;
        this.tipoPago = tipoPago;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(int numOrden) {
        this.numOrden = numOrden;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidadItems() {
        return cantidadItems;
    }

    public void setCantidadItems(int cantidadItems) {
        this.cantidadItems = cantidadItems;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
