package com.estudiar.controller;

import com.estudiar.model.Clase;
import com.estudiar.dao.ClaseDAO;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.time.LocalDate;

public class ClaseController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtTema;
    @FXML private TextArea txtObjetivos;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbMateria;
    @FXML private Button btnGuardar;

    private int idDocenteActivo;  // se setea luego del login

    public void setIdDocenteActivo(int idDocente) {
        this.idDocenteActivo = idDocente;
    }

    @FXML
    private void initialize() {
        // Cargar materias del docente (simplificado)
        cbMateria.getItems().addAll("Matemática I", "Lengua I", "Historia");
    }

    @FXML
    private void registrarClase(ActionEvent event) {
        String titulo = txtTitulo.getText();
        String tema = txtTema.getText();
        String objetivos = txtObjetivos.getText();
        LocalDate fecha = dpFecha.getValue();
        String materiaSeleccionada = cbMateria.getValue();

        if (titulo.isEmpty() || tema.isEmpty() || objetivos.isEmpty() || fecha == null || materiaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Por favor, complete todos los campos.");
            return;
        }

        // (En implementación real, buscar el id_materia en BD)
        int idMateria = obtenerIdMateria(materiaSeleccionada);

        Clase clase = new Clase(titulo, tema, objetivos, fecha, idMateria, idDocenteActivo);
        ClaseDAO claseDAO = new ClaseDAO();

        boolean exito = claseDAO.insertarClase(clase);
        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Clase registrada correctamente.");
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar la clase.");
        }
    }

    private void limpiarCampos() {
        txtTitulo.clear();
        txtTema.clear();
        txtObjetivos.clear();
        dpFecha.setValue(null);
        cbMateria.setValue(null);
    }

    private int obtenerIdMateria(String nombreMateria) {
        // Temporal: en producción se debe consultar desde la BD
        return switch (nombreMateria) {
            case "Matemática I" -> 1;
            case "Lengua I" -> 2;
            case "Historia" -> 3;
            default -> 1;
        };
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
