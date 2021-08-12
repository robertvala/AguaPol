package com.espol.aguapol.Modelo;

import java.util.ArrayList;

public class HistorialCaudal {
    String fecha;
    ArrayList<ContorlCaudal> listcaudal;
    Double promedio;

    public HistorialCaudal(String fecha, ArrayList<ContorlCaudal> listcaudal, Double promedio) {
        this.fecha = fecha;
        this.listcaudal = listcaudal;
        this.promedio = promedio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<ContorlCaudal> getListcaudal() {
        return listcaudal;
    }

    public void setListcaudal(ArrayList<ContorlCaudal> listcaudal) {
        this.listcaudal = listcaudal;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }
}
