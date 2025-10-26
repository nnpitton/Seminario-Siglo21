package com.estudiar.dao;

import com.estudiar.model.Docente;
import com.estudiar.model.Usuario;
import com.estudiar.utils.ConexionDB;

import java.sql.*;

public class UsuarioDAO {

    public Usuario autenticar(String email, String passwordHash) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND password_hash = ? AND activo = 1";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("🔍 Intentando login con:");
            System.out.println("Email = " + email);
            System.out.println("Password = " + passwordHash);

            stmt.setString(1, email);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Usuario encontrado en DB.");
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt("id_usuario"));
                user.setNombre(rs.getString("nombre"));
                user.setApellido(rs.getString("apellido"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setActivo(rs.getBoolean("activo"));
                user.setIdRol(rs.getInt("id_rol"));
                return user;
            } else {
                System.out.println("❌ No se encontró ningún usuario que coincida.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al autenticar usuario: " + e.getMessage());
        }

        return null;
    }

}
