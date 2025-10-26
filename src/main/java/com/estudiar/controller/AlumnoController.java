package com.estudiar.controller;

import com.estudiar.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlumnoController {

    @FXML private Label lblBienvenida; // 🆕 etiqueta que usaremos en la vista

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
///com/estudiar/agenda_view.fxml
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
    private void handleLogout() {
        Stage stage = (Stage) lblBienvenida.getScene().getWindow();
        stage.close();
    }
}
