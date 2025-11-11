package com.estudiar.model;

import java.time.LocalDateTime;

public class Mensaje {
    private int idMensaje;
    private String asunto;
    private String cuerpo;
    private LocalDateTime fechaEnvio;
    private String estado; // "ENVIADO", "LEIDO" - para compatibilidad con la UI
    private boolean leido; // Para la BD
    private int idEmisor;
    private int idReceptor; // También conocido como id_destinatario en BD
    private String nombreEmisor;
    private String nombreReceptor;
    private boolean activo;

    public Mensaje() {
        this.fechaEnvio = LocalDateTime.now();
        this.estado = "ENVIADO";
        this.leido = false;
        this.activo = true;
    }

    public Mensaje(String asunto, String cuerpo, int idEmisor, int idReceptor) {
        this();
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
    }

    // Getters y Setters
    public int getIdMensaje() { return idMensaje; }
    public void setIdMensaje(int idMensaje) { this.idMensaje = idMensaje; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado;
        this.leido = "LEIDO".equals(estado);
    }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) {
        this.leido = leido;
        this.estado = leido ? "LEIDO" : "ENVIADO";
    }

    public int getIdEmisor() { return idEmisor; }
    public void setIdEmisor(int idEmisor) { this.idEmisor = idEmisor; }

    public int getIdReceptor() { return idReceptor; }
    public void setIdReceptor(int idReceptor) { this.idReceptor = idReceptor; }

    public String getNombreEmisor() { return nombreEmisor; }
    public void setNombreEmisor(String nombreEmisor) { this.nombreEmisor = nombreEmisor; }

    public String getNombreReceptor() { return nombreReceptor; }
    public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public void marcarComoLeido() {
        this.estado = "LEIDO";
        this.leido = true;
    }
}