package com.espol.aguapol.Modelo;


public class Alarma {
    String titulo;
    String mensaje;
    String fechaHora;
    int urlIcon;

    public Alarma(String titulo, String mensaje, String fechaHora, int urlIcon) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
        this.urlIcon = urlIcon;
    }

    public Alarma() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(int urlIcon) {
        this.urlIcon = urlIcon;
    }
}
