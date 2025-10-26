package com.estudiar.model;

public class Docente extends Usuario {

    private String legajo;
    private String especialidad;


    public Docente() {
        super();
    }



    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void registrarClase(String titulo, String tema, String objetivos) {
        System.out.println("📘 Clase registrada por el docente " + getNombreCompleto() + ": " + titulo);
    }

    public void enviarMensaje(String destinatario, String asunto) {
        System.out.println("✉️  Mensaje enviado a " + destinatario + " con asunto: " + asunto);
    }


    @Override
    public String toString() {
        return "Docente{" +
                "id=" + getIdUsuario() +
                ", nombre='" + getNombreCompleto() + '\'' +
                ", legajo='" + legajo + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }
}
