package com.espol.aguapol.Modelo;

public class AlarmaManejada extends Alarma{
    String user;

    public AlarmaManejada(String titulo, String mensaje, String fechaHora, int urlIcon, String id, String user) {
        super(titulo, mensaje, fechaHora, urlIcon, id);
        this.user = user;
    }

    public AlarmaManejada() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
