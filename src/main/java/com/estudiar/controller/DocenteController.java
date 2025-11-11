package com.estudiar.controller;

import com.estudiar.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DocenteController {

    private int idDocenteActivo;

    public void setIdDocenteActivo(int idDocenteActivo) {
        this.idDocenteActivo = idDocenteActivo;
    }

    @FXML
    private void abrirRegistrarClase(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/RegistrarClaseView.fxml"));
            Parent root = loader.load();

            ClaseController claseController = loader.getController();
            claseController.setIdDocenteActivo(idDocenteActivo);

            Stage stage = new Stage();
            stage.setTitle("Registrar nueva clase");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al abrir RegistrarClaseView.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void handleConsultarAgenda() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/agenda_view.fxml"));
            Scene scene = new Scene(loader.load());

            AgendaController controller = loader.getController();
            controller.inicializar(idDocenteActivo, "DOCENTE");

            Stage stage = new Stage();
            stage.setTitle("Agenda Académica - Docente");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMensajes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/mensajeria-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Crear usuario con datos del docente
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idDocenteActivo);
            usuario.setIdRol(2); // Rol docente

            MensajeriaController controller = loader.getController();
            controller.inicializar(usuario);

            Stage stage = new Stage();
            stage.setTitle("Mensajería - Docente");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/login-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Obtener el stage actual y cambiar a la escena de login
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Estudi-AR - Iniciar Sesión");
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al volver al login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}