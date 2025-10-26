package com.estudiar.controller;

import com.estudiar.dao.UsuarioDAO;
import com.estudiar.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(email, password);

        if (usuario != null) {
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("Bienvenido, " + usuario.getNombreCompleto() + ".");

            try {
                // Determinar la vista según el rol
                String vista = switch (usuario.getIdRol()) {
                    case 1 -> "/com/estudiar/admin-view.fxml";
                    case 2 -> "/com/estudiar/docente-view.fxml";
                    case 3, 4 -> "/com/estudiar/alumno-view.fxml";
                    default -> null;
                };

                if (vista != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
                    Scene nuevaScene = new Scene(loader.load());
                    Stage stage = (Stage) txtEmail.getScene().getWindow();
                    if (usuario.getIdRol() == 2) {
                        com.estudiar.controller.DocenteController controller = loader.getController();
                        controller.setIdDocenteActivo(usuario.getIdUsuario());
                        System.out.println("✅ Docente logueado: ID " + usuario.getIdUsuario());
                    } else if (usuario.getIdRol() == 3 || usuario.getIdRol() == 4) {
                        com.estudiar.controller.AlumnoController controller = loader.getController();
                        controller.setUsuarioActual(usuario);
                    }

                    stage.setScene(nuevaScene);
                    stage.setTitle("Estudi-AR - Panel de usuario");
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                lblMensaje.setText("Error al cargar el panel del usuario.");
            }

        } else {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Credenciales incorrectas o usuario inactivo.");
        }
    }

}
