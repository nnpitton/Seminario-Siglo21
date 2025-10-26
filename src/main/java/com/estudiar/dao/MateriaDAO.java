package com.estudiar.dao;

import com.estudiar.utils.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    public List<String> obtenerMateriasPorUsuario(int idUsuario, String rol) {
        List<String> materias = new ArrayList<>();
        String sql = "";

        switch (rol.toUpperCase()) {
            case "DOCENTE" -> sql = """
        SELECT DISTINCT m.nombre
        FROM materia m
        JOIN docente d ON d.id_docente = ?
        JOIN clase c ON c.id_materia = m.id_materia AND c.id_docente = d.id_docente
        WHERE m.activo = 1
    """;
            case "ALUMNO" -> sql = """
        SELECT DISTINCT m.nombre
        FROM materia m
        JOIN curso cu ON cu.id_curso = m.id_curso
        JOIN alumno a ON a.id_curso = cu.id_curso
        WHERE a.id_alumno = ? AND m.activo = 1
    """;
            case "PADRE" -> sql = """
        SELECT DISTINCT m.nombre
        FROM materia m
        JOIN curso cu ON cu.id_curso = m.id_curso
        JOIN alumno a ON a.id_curso = cu.id_curso
        JOIN padre_alumno pa ON pa.id_alumno = a.id_alumno
        WHERE pa.id_padre = ? AND m.activo = 1
    """;
        }


        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                materias.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener materias: " + e.getMessage());
        }
        return materias;
    }
}
