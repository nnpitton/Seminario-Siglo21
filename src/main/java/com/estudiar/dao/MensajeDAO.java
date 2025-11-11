package com.estudiar.dao;

import com.estudiar.model.Mensaje;
import com.estudiar.model.Usuario;
import com.estudiar.utils.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MensajeDAO {

    /**
     * Inserta un nuevo mensaje en la base de datos
     */
    public boolean enviarMensaje(Mensaje mensaje) {
        String sql = "INSERT INTO mensaje (asunto, cuerpo, fecha_envio, remitente_id, destinatario_id, leido, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mensaje.getAsunto());
            stmt.setString(2, mensaje.getCuerpo());
            stmt.setTimestamp(3, Timestamp.valueOf(mensaje.getFechaEnvio()));
            stmt.setInt(4, mensaje.getIdEmisor());
            stmt.setInt(5, mensaje.getIdReceptor());
            stmt.setBoolean(6, false); // Siempre inicia como no leído
            stmt.setBoolean(7, mensaje.isActivo());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todos los mensajes recibidos por un usuario
     */
    public List<Mensaje> obtenerMensajesRecibidos(int idUsuario) {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = """
            SELECT m.*, 
                   CONCAT(u_emisor.nombre, ' ', u_emisor.apellido) AS nombre_emisor
            FROM mensaje m
            JOIN usuario u_emisor ON m.remitente_id = u_emisor.id_usuario
            WHERE m.destinatario_id = ? AND m.activo = 1
            ORDER BY m.fecha_envio DESC
        """;

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Mensaje m = new Mensaje();
                m.setIdMensaje(rs.getInt("id_mensaje"));
                m.setAsunto(rs.getString("asunto"));
                m.setCuerpo(rs.getString("cuerpo"));
                m.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());

                boolean leido = rs.getBoolean("leido");
                m.setEstado(leido ? "LEIDO" : "ENVIADO");

                m.setIdEmisor(rs.getInt("remitente_id"));
                m.setIdReceptor(rs.getInt("destinatario_id"));
                m.setNombreEmisor(rs.getString("nombre_emisor"));
                m.setActivo(rs.getBoolean("activo"));
                mensajes.add(m);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener mensajes recibidos: " + e.getMessage());
            e.printStackTrace();
        }

        return mensajes;
    }

    /**
     * Obtiene todos los mensajes enviados por un usuario
     */
    public List<Mensaje> obtenerMensajesEnviados(int idUsuario) {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = """
            SELECT m.*, 
                   CONCAT(u_receptor.nombre, ' ', u_receptor.apellido) AS nombre_receptor
            FROM mensaje m
            JOIN usuario u_receptor ON m.destinatario_id = u_receptor.id_usuario
            WHERE m.remitente_id = ? AND m.activo = 1
            ORDER BY m.fecha_envio DESC
        """;

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Mensaje m = new Mensaje();
                m.setIdMensaje(rs.getInt("id_mensaje"));
                m.setAsunto(rs.getString("asunto"));
                m.setCuerpo(rs.getString("cuerpo"));
                m.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());

                boolean leido = rs.getBoolean("leido");
                m.setEstado(leido ? "LEIDO" : "ENVIADO");

                m.setIdEmisor(rs.getInt("remitente_id"));
                m.setIdReceptor(rs.getInt("destinatario_id"));
                m.setNombreReceptor(rs.getString("nombre_receptor"));
                m.setActivo(rs.getBoolean("activo"));
                mensajes.add(m);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener mensajes enviados: " + e.getMessage());
            e.printStackTrace();
        }

        return mensajes;
    }

    /**
     * Marca un mensaje como leído
     */
    public boolean marcarComoLeido(int idMensaje) {
        String sql = "UPDATE mensaje SET leido = true WHERE id_mensaje = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMensaje);
            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al marcar mensaje como leído: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la lista de destinatarios disponibles para un usuario (docentes para padres, padres para docentes)
     */
    public List<Usuario> obtenerDestinatariosDisponibles(int idUsuario, int idRol) {
        List<Usuario> destinatarios = new ArrayList<>();
        String sql = "";

        // Si es docente (rol 2), puede enviar a padres (rol 4) de sus alumnos
        if (idRol == 2) {
            sql = """
                SELECT DISTINCT u.id_usuario, u.nombre, u.apellido, u.email
                FROM usuario u
                JOIN padre p ON p.id_padre = u.id_usuario
                JOIN padre_alumno pa ON pa.id_padre = p.id_padre
                JOIN alumno a ON a.id_alumno = pa.id_alumno
                JOIN curso c ON c.id_curso = a.id_curso
                JOIN materia m ON m.id_curso = c.id_curso
                JOIN clase cl ON cl.id_materia = m.id_materia
                WHERE cl.id_docente = ? AND u.activo = 1
            """;
        }
        // Si es padre (rol 4), puede enviar a docentes (rol 2) de sus hijos
        else if (idRol == 4) {
            sql = """
                SELECT DISTINCT u.id_usuario, u.nombre, u.apellido, u.email
                FROM usuario u
                JOIN docente d ON d.id_docente = u.id_usuario
                JOIN clase cl ON cl.id_docente = d.id_docente
                JOIN materia m ON m.id_materia = cl.id_materia
                JOIN curso c ON c.id_curso = m.id_curso
                JOIN alumno a ON a.id_curso = c.id_curso
                JOIN padre_alumno pa ON pa.id_alumno = a.id_alumno
                WHERE pa.id_padre = ? AND u.activo = 1
            """;
        } else {
            // Rol no válido para mensajería (ej: alumno rol 3, admin rol 1)
            System.err.println("⚠️ Rol " + idRol + " no tiene permisos de mensajería");
            return destinatarios; // Retorna lista vacía
        }

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setEmail(rs.getString("email"));
                destinatarios.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener destinatarios: " + e.getMessage());
            e.printStackTrace();
        }

        return destinatarios;
    }

    /**
     * Cuenta los mensajes no leídos de un usuario
     */
    public int contarMensajesNoLeidos(int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM mensaje WHERE destinatario_id = ? AND leido = false AND activo = 1";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error al contar mensajes no leídos: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}