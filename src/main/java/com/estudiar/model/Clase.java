package com.estudiar.model;
import java.time.LocalDate;

public class Clase {
    private int idClase;
    private String titulo;
    private String tema;
    private String objetivos;
    private LocalDate fecha;
    private int idMateria;
    private int idDocente;

    public Clase() {}

    public Clase(String titulo, String tema, String objetivos, LocalDate fecha, int idMateria, int idDocente) {
        this.titulo = titulo;
        this.tema = tema;
        this.objetivos = objetivos;
        this.fecha = fecha;
        this.idMateria = idMateria;
        this.idDocente = idDocente;
    }

    // Getters y Setters
    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }
}
