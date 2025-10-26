package com.estudiar.model;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String passwordHash;
    private boolean activo;
    private int idRol;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellido, String email, String passwordHash, boolean activo, int idRol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.passwordHash = passwordHash;
        this.activo = activo;
        this.idRol = idRol;
    }


    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
