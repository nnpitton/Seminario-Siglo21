package com.estudiar.controller;

import com.estudiar.dao.MensajeDAO;
import com.estudiar.model.Mensaje;
import com.estudiar.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MensajeriaController {

    @FXML private TableView<Mensaje> tablaMensajes;
    @FXML private TableColumn<Mensaje, String> colEstado;
    @FXML private TableColumn<Mensaje, String> colDe;
    @FXML private TableColumn<Mensaje, String> colAsunto;
    @FXML private TableColumn<Mensaje, String> colFecha;

    @FXML private Label lblTituloLista;
    @FXML private Label lblNoLeidos;
    @FXML private Label lblDe;
    @FXML private Label lblPara;
    @FXML private Label lblFecha;
    @FXML private Label lblAsunto;
    @FXML private TextArea txtMensaje;
    @FXML private Button btnResponder;

    private final MensajeDAO mensajeDAO = new MensajeDAO();
    private Usuario usuarioActual;
    private String vistaActual = "RECIBIDOS"; // RECIBIDOS o ENVIADOS

    public void inicializar(Usuario usuario) {
        this.usuarioActual = usuario;

        // Configurar columnas de la tabla
        colEstado.setCellValueFactory(cellData -> {
            String icono = cellData.getValue().getEstado().equals("LEIDO") ? "✅" : "📩";
            return new javafx.beans.property.SimpleStringProperty(icono);
        });

        colDe.setCellValueFactory(cellData -> {
            String nombre = vistaActual.equals("RECIBIDOS")
                    ? cellData.getValue().getNombreEmisor()
                    : cellData.getValue().getNombreReceptor();
            return new javafx.beans.property.SimpleStringProperty(nombre);
        });

        colAsunto.setCellValueFactory(new PropertyValueFactory<>("asunto"));

        colFecha.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getFechaEnvio().format(formatter)
            );
        });

        // Listener para selección de mensaje
        tablaMensajes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        mostrarDetalleMensaje(newSelection);
                    }
                }
        );

        // Cargar mensajes recibidos por defecto
        cargarMensajesRecibidos();
    }

    @FXML
    private void handleMostrarRecibidos() {
        vistaActual = "RECIBIDOS";
        cargarMensajesRecibidos();
    }

    @FXML
    private void handleMostrarEnviados() {
        vistaActual = "ENVIADOS";
        cargarMensajesEnviados();
    }

    private void cargarMensajesRecibidos() {
        lblTituloLista.setText("Mensajes Recibidos");
        List<Mensaje> mensajes = mensajeDAO.obtenerMensajesRecibidos(usuarioActual.getIdUsuario());
        ObservableList<Mensaje> data = FXCollections.observableArrayList(mensajes);
        tablaMensajes.setItems(data);

        // Actualizar contador de no leídos
        int noLeidos = mensajeDAO.contarMensajesNoLeidos(usuarioActual.getIdUsuario());
        lblNoLeidos.setText(noLeidos > 0 ? "(" + noLeidos + " sin leer)" : "");

        limpiarDetalle();
    }

    private void cargarMensajesEnviados() {
        lblTituloLista.setText("Mensajes Enviados");
        List<Mensaje> mensajes = mensajeDAO.obtenerMensajesEnviados(usuarioActual.getIdUsuario());
        ObservableList<Mensaje> data = FXCollections.observableArrayList(mensajes);
        tablaMensajes.setItems(data);

        lblNoLeidos.setText("");
        limpiarDetalle();
    }

    private void mostrarDetalleMensaje(Mensaje mensaje) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        lblDe.setText(mensaje.getNombreEmisor() != null ? mensaje.getNombreEmisor() : "");
        lblPara.setText(mensaje.getNombreReceptor() != null ? mensaje.getNombreReceptor() : "");
        lblFecha.setText(mensaje.getFechaEnvio().format(formatter));
        lblAsunto.setText(mensaje.getAsunto());
        txtMensaje.setText(mensaje.getCuerpo());

        btnResponder.setDisable(false);

        // Si es un mensaje recibido y no está leído, marcarlo como leído
        if (vistaActual.equals("RECIBIDOS") && mensaje.getEstado().equals("ENVIADO")) {
            if (mensajeDAO.marcarComoLeido(mensaje.getIdMensaje())) {
                mensaje.marcarComoLeido();
                tablaMensajes.refresh();

                // Actualizar contador
                int noLeidos = mensajeDAO.contarMensajesNoLeidos(usuarioActual.getIdUsuario());
                lblNoLeidos.setText(noLeidos > 0 ? "(" + noLeidos + " sin leer)" : "");
            }
        }
    }

    private void limpiarDetalle() {
        lblDe.setText("");
        lblPara.setText("");
        lblFecha.setText("");
        lblAsunto.setText("");
        txtMensaje.setText("");
        btnResponder.setDisable(true);
    }

    @FXML
    private void handleNuevoMensaje() {
        abrirVentanaNuevoMensaje(null);
    }

    @FXML
    private void handleResponder() {
        Mensaje seleccionado = tablaMensajes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirVentanaNuevoMensaje(seleccionado);
        }
    }

    private void abrirVentanaNuevoMensaje(Mensaje mensajeOriginal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estudiar/nuevo-mensaje-view.fxml"));
            Scene scene = new Scene(loader.load());

            NuevoMensajeController controller = loader.getController();
            controller.inicializar(usuarioActual, mensajeOriginal);

            Stage stage = new Stage();
            stage.setTitle(mensajeOriginal == null ? "Nuevo Mensaje" : "Responder Mensaje");
            stage.setScene(scene);
            stage.showAndWait();

            // Recargar mensajes después de enviar
            if (vistaActual.equals("RECIBIDOS")) {
                cargarMensajesRecibidos();
            } else {
                cargarMensajesEnviados();
            }

        } catch (Exception e) {
            System.err.println("Error al abrir ventana de nuevo mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}