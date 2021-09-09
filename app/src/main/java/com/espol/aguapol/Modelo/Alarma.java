package com.espol.aguapol.Modelo;


import java.io.Serializable;

public class Alarma implements Serializable {
    String titulo;
    String mensaje;
    String fechaHora;
    int urlIcon;
    String id;
    String correoUsuario;


    public Alarma(String titulo, String mensaje, String fechaHora, int urlIcon, String id, String correoUsuario) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
        this.urlIcon = urlIcon;
        this.id = id;
        this.correoUsuario = correoUsuario;
    }

    public Alarma() {
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
