package com.espol.aguapol.Modelo;

public class ContorlCaudal {
    String hora;
    String valor;

    public ContorlCaudal() {
    }

    public ContorlCaudal(String hora, String valor) {
        this.hora = hora;
        this.valor = valor;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
