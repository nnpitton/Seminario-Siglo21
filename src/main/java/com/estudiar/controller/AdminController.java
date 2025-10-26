package com.estudiar.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminController {
    @FXML
    private void handleLogout() {
        Stage stage = (Stage) new Button().getScene().getWindow();
        stage.close();
    }
}
