package com.estudiar.controller;

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

    // 🔹 Método para abrir la vista de Registrar Clase
    @FXML
    private void abrirRegistrarClase(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/RegistrarClaseView.fxml"));
            Parent root = loader.load();

            // Pasamos el ID del docente logueado al controlador de la vista
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

    // 🔹 Cerrar sesión (ya existente, ajustado)
    @FXML
    private void handleLogout(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
