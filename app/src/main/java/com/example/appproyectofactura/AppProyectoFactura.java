package com.example.appproyectofactura;

import android.app.Application;

public class AppProyectoFactura extends Application {

    private String usuario;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
