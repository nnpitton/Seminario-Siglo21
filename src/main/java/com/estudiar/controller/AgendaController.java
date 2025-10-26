package com.estudiar.controller;

import com.estudiar.dao.ClaseDAO;
import com.estudiar.dao.MateriaDAO;
import com.estudiar.model.Clase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AgendaController {

    @FXML
    private ComboBox<String> cmbMateria;

    @FXML
    private DatePicker dpDesde;

    @FXML
    private DatePicker dpHasta;

    @FXML
    private TableView<Clase> tablaClases;

    @FXML
    private TableColumn<Clase, String> colFecha;

    @FXML
    private TableColumn<Clase, String> colTema;

    @FXML
    private TableColumn<Clase, String> colObjetivos;

    @FXML
    private Label lblMensaje;

    private final ClaseDAO claseDAO = new ClaseDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private int idUsuario;
    private String rol;

    /** Inicializa el controlador de Agenda con el usuario y rol actual */
    public void inicializar(int idUsuario, String rol) {
        this.idUsuario = idUsuario;
        this.rol = rol;

        // 🔧 Fuerza el locale para que DatePicker funcione correctamente
        Locale.setDefault(new Locale("es", "AR"));


        // 🔧 Configurar DatePickers para que sean clickeables
        configurarDatePicker(dpDesde);
        configurarDatePicker(dpHasta);

        // Cargar materias al ComboBox
        List<String> materias = materiaDAO.obtenerMateriasPorUsuario(idUsuario, rol);
        cmbMateria.getItems().setAll(materias);

        // Configurar las columnas de la tabla
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTema.setCellValueFactory(new PropertyValueFactory<>("tema"));
        colObjetivos.setCellValueFactory(new PropertyValueFactory<>("objetivos"));

       /* javafx.application.Platform.runLater(() -> {
            dpDesde.setEditable(false);
            dpHasta.setEditable(false);
        });*/

    }

    /**
     * Configura un DatePicker para que sea clickeable y muestre fechas en español
     */
    private void configurarDatePicker(DatePicker datePicker) {
        // Formato de fecha en español
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    try {
                        return LocalDate.parse(string, formatter);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }
        });

        // Hacer que el campo sea editable pero con formato controlado
        datePicker.setEditable(true);

        // Mostrar el popup del calendario al hacer click
        datePicker.setOnMouseClicked(event -> {
            if (!datePicker.isShowing()) {
                datePicker.show();
            }
        });

        // Agregar prompt text
        datePicker.setPromptText("dd/mm/aaaa");
    }

            //

    /** Maneja el evento de búsqueda de clases por materia y rango de fechas */
    @FXML
    private void handleBuscar() {
        try {
            String filtroMateria = cmbMateria.getValue();
            Date desde = dpDesde.getValue() != null ? Date.valueOf(dpDesde.getValue()) : null;
            Date hasta = dpHasta.getValue() != null ? Date.valueOf(dpHasta.getValue()) : null;

            List<Clase> lista = claseDAO.obtenerClasesPorUsuario(idUsuario, rol, filtroMateria, desde, hasta);
            if (lista.isEmpty()) {
                lblMensaje.setText("No hay clases registradas en el rango seleccionado.");
                tablaClases.setItems(FXCollections.observableArrayList());
            } else {
                ObservableList<Clase> data = FXCollections.observableArrayList(lista);
                tablaClases.setItems(data);
                lblMensaje.setText("Se encontraron " + lista.size() + " clases.");
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al consultar la agenda.");
            e.printStackTrace();
        }
    }
}
