package com.estudiar.controller;

import com.estudiar.dao.MensajeDAO;
import com.estudiar.model.Mensaje;
import com.estudiar.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;

public class NuevoMensajeController {

    @FXML private ComboBox<Usuario> cmbDestinatario;
    @FXML private TextField txtAsunto;
    @FXML private TextArea txtCuerpo;
    @FXML private Label lblMensaje;

    private final MensajeDAO mensajeDAO = new MensajeDAO();
    private Usuario usuarioActual;

    public void inicializar(Usuario usuario, Mensaje mensajeOriginal) {
        this.usuarioActual = usuario;

        // Configurar ComboBox de destinatarios
        cmbDestinatario.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario user) {
                return user != null ? user.getNombreCompleto() + " (" + user.getEmail() + ")" : "";
            }

            @Override
            public Usuario fromString(String string) {
                return null;
            }
        });

        // Cargar destinatarios disponibles
        List<Usuario> destinatarios = mensajeDAO.obtenerDestinatariosDisponibles(
                usuario.getIdUsuario(),
                usuario.getIdRol()
        );
        cmbDestinatario.getItems().setAll(destinatarios);

        // Si es una respuesta, pre-cargar datos
        if (mensajeOriginal != null) {
            // Buscar el destinatario en la lista (el emisor del mensaje original)
            Usuario destinatario = destinatarios.stream()
                    .filter(u -> u.getIdUsuario() == mensajeOriginal.getIdEmisor())
                    .findFirst()
                    .orElse(null);

            if (destinatario != null) {
                cmbDestinatario.setValue(destinatario);
            }

            txtAsunto.setText("Re: " + mensajeOriginal.getAsunto());
            txtCuerpo.setText("\n\n--- Mensaje original ---\n" + mensajeOriginal.getCuerpo());
            txtCuerpo.positionCaret(0);
        }
    }

    @FXML
    private void handleEnviar() {
        // Validar campos
        if (cmbDestinatario.getValue() == null) {
            lblMensaje.setText("Debe seleccionar un destinatario.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        if (txtAsunto.getText().trim().isEmpty()) {
            lblMensaje.setText("El asunto no puede estar vacío.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        if (txtCuerpo.getText().trim().isEmpty()) {
            lblMensaje.setText("El mensaje no puede estar vacío.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        // Crear y enviar mensaje
        Mensaje mensaje = new Mensaje(
                txtAsunto.getText().trim(),
                txtCuerpo.getText().trim(),
                usuarioActual.getIdUsuario(),
                cmbDestinatario.getValue().getIdUsuario()
        );

        boolean exito = mensajeDAO.enviarMensaje(mensaje);

        if (exito) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Mensaje enviado correctamente.");
            alert.showAndWait();

            cerrarVentana();
        } else {
            lblMensaje.setText("Error al enviar el mensaje. Intente nuevamente.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtAsunto.getScene().getWindow();
        stage.close();
    }
}