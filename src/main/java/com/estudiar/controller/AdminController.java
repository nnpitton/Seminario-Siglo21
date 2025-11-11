package com.estudiar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController {
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