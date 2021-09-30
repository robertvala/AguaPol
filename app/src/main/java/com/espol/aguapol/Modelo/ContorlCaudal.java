package com.espol.aguapol.Modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContorlCaudal {
    String fecha;
    float promedio;
    HashMap<String,Float> valores;

    public ContorlCaudal(String fecha, float promedio, HashMap<String, Float> valores) {
        this.fecha = fecha;
        this.promedio = promedio;
        this.valores = valores;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }

    public HashMap<String, Float> getValores() {
        return valores;
    }

    public void setValores(HashMap<String, Float> valores) {
        this.valores = valores;
    }

    public float obtenerPromedio(){
        int cont=0;
        float suma=0.0F;
        for(Map.Entry<String,Float> map: valores.entrySet()){
            cont++;
            suma=suma+map.getValue();
        }
        this.promedio=suma/cont;
        return promedio;
    }

}
