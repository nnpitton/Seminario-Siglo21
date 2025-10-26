package com.estudiar.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Date;



import com.estudiar.model.Clase;
import com.estudiar.utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClaseDAO {

    public boolean insertarClase(Clase clase) {
        String sql = "INSERT INTO clase (titulo, tema, objetivos, fecha, id_materia, id_docente) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clase.getTitulo());
            stmt.setString(2, clase.getTema());
            stmt.setString(3, clase.getObjetivos());
            stmt.setDate(4, java.sql.Date.valueOf(clase.getFecha()));
            stmt.setInt(5, clase.getIdMateria());
            stmt.setInt(6, clase.getIdDocente());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar clase: " + e.getMessage());
            return false;
        }
    }
    public List<Clase> obtenerClasesPorUsuario(int idUsuario, String rol, String filtroMateria, Date fechaDesde, Date fechaHasta) {
        List<Clase> clases = new ArrayList<>();
        String sql = "";

        switch (rol.toUpperCase()) {
            case "DOCENTE" -> sql = "SELECT c.* FROM clase c WHERE c.id_docente = ? AND c.activo = 1";
            case "ALUMNO" -> sql = """
            SELECT c.* FROM clase c
            JOIN materia m ON c.id_materia = m.id_materia
            JOIN curso cu ON cu.id_curso = m.id_curso
            JOIN alumno a ON a.id_curso = cu.id_curso
            WHERE a.id_alumno = ? AND c.activo = 1
        """;
            case "PADRE" -> sql = """
            SELECT c.* FROM clase c
            JOIN materia m ON c.id_materia = m.id_materia
            JOIN curso cu ON cu.id_curso = m.id_curso
            JOIN alumno a ON a.id_curso = cu.id_curso
            JOIN padre_alumno pa ON pa.id_alumno = a.id_alumno
            WHERE pa.id_padre = ? AND c.activo = 1
        """;
        }

        if (filtroMateria != null && !filtroMateria.isEmpty())
            sql += " AND m.nombre LIKE ?";
        if (fechaDesde != null && fechaHasta != null)
            sql += " AND c.fecha BETWEEN ? AND ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            stmt.setInt(index++, idUsuario);
            if (filtroMateria != null && !filtroMateria.isEmpty())
                stmt.setString(index++, "%" + filtroMateria + "%");
            if (fechaDesde != null && fechaHasta != null) {
                stmt.setDate(index++, fechaDesde);
                stmt.setDate(index++, fechaHasta);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Clase c = new Clase();
                c.setIdClase(rs.getInt("id_clase"));
                c.setTitulo(rs.getString("titulo"));
                c.setTema(rs.getString("tema"));
                c.setObjetivos(rs.getString("objetivos"));
                c.setFecha(rs.getDate("fecha").toLocalDate());
                clases.add(c);
            }

        } catch (Exception e) {
            System.err.println("Error al consultar clases: " + e.getMessage());
        }

        return clases;
    }
}
