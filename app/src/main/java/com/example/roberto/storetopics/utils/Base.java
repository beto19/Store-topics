package com.example.roberto.storetopics.utils;

import org.json.JSONObject;

/**
 * Created by roberto on 14/12/16.
 */

public interface Base  {
    void cargar();
    void cargarVista(JSONObject response);
    void eliminar();
    void eliminado(JSONObject response);
    void editar();
}