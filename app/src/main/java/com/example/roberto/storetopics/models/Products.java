package com.example.roberto.storetopics.models;

/**
 * Created by roberto on 26/11/16.
 */

public class Products {
    private int id;
    private String title;
    private String type;
    private double price;
    private String imageUrl;
    /*private String status;//status
    private int almacen;//stock_quantity
    private boolean disponibilidad;//in_stock
    private String descripcion;//description*/

    public Products(int _id, String _title, String _type, double _price, String _imageUrl)
    {
        id = _id;
        title = _title;
        type = _type;
        price = _price;
        imageUrl = _imageUrl;
        /*status=_status;
        almacen=_almacen;
        disponibilidad=_disponibilidad;
        descripcion=_descripcion;*/
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /*public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAlmacen() {
        return almacen;
    }

    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }*/
}
