package com.estudiar.controller;

import com.estudiar.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlumnoController {

    @FXML private Label lblBienvenida;

    private Usuario usuarioActual;

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;

        if (usuario.getIdRol() == 3) {
            lblBienvenida.setText("Bienvenido, Alumno " + usuario.getNombreCompleto());
        } else if (usuario.getIdRol() == 4) {
            lblBienvenida.setText("Bienvenido, Padre/Tutor  " + usuario.getNombreCompleto());
        } else {
            lblBienvenida.setText("Bienvenido a Estudi-AR");
        }
    }

    @FXML
    private void handleConsultarAgenda() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/agenda_view.fxml"));
            Scene scene = new Scene(loader.load());

            String rol = usuarioActual.getIdRol() == 4 ? "PADRE" : "ALUMNO";

            AgendaController controller = loader.getController();
            controller.inicializar(usuarioActual.getIdUsuario(), rol);

            Stage stage = new Stage();
            stage.setTitle("Agenda Académica - " + rol);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMensajes() {
        try {
            // Verificar el rol del usuario
            if (usuarioActual.getIdRol() == 3) {
                // Es un alumno - NO tiene acceso a mensajería
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("La mensajería está disponible solo para padres/tutores y docentes.\n\n" +
                        "Los alumnos pueden consultar la agenda académica.");
                alert.showAndWait();
                return;
            }

            // Es un padre (rol 4) - SÍ tiene acceso
            if (usuarioActual.getIdRol() == 4) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/mensajeria-view.fxml"));
                Scene scene = new Scene(loader.load());

                MensajeriaController controller = loader.getController();
                controller.inicializar(usuarioActual);

                Stage stage = new Stage();
                stage.setTitle("Mensajería - Padre/Tutor");
                stage.setScene(scene);
                stage.show();
            } else {
                // Otro rol no contemplado
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Acceso Denegado");
                alert.setHeaderText(null);
                alert.setContentText("No tiene permisos para acceder a la mensajería.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            System.err.println("Error al abrir mensajería: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al abrir el módulo de mensajería.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/login-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Obtener el stage actual y cambiar a la escena de login
            Stage stage = (Stage) lblBienvenida.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Estudi-AR - Iniciar Sesión");
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al volver al login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}