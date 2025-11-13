package jdbc.martinamolina.controller.alumnos;

import jdbc.martinamolina.view.estudiantes.EstudiantesView;
import jdbc.martinamolina.dao.alumnos.EstudianteDAO;
import jdbc.martinamolina.daoimp.alumnos.EstudianteDAOImpl;
import jdbc.martinamolina.dao.alumnos.CalificacionDAO;
import jdbc.martinamolina.daoimp.alumnos.CalificacionDAOImpl;
import jdbc.martinamolina.model.alumnos.Estudiante;
import jdbc.martinamolina.model.alumnos.Calificacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class EstudiantesController {

    private final EstudiantesView view;
    private final EstudianteDAO estDao = new EstudianteDAOImpl();
    private final CalificacionDAO calDao = new CalificacionDAOImpl();
    private Integer currentEstudianteId = null;

    public EstudiantesController(EstudiantesView view) {
        this.view = view;
        configurarEventos();
        cargarEstudiantes();
        limpiarForm();
    }

    // ================= EVENTOS =================

    private void configurarEventos() {
        view.getBtnEstNuevo().addActionListener(e -> limpiarForm());
        view.getBtnEstGuardar().addActionListener(e -> onGuardar());
        view.getBtnEstEliminar().addActionListener(e -> onEliminar());
        view.getBtnEstActualizar().addActionListener(e -> onActualizar());
        view.getBtnEstRefrescar().addActionListener(e -> cargarEstudiantes());

        view.getBtnCalifAgregar().addActionListener(e -> onAgregarCalif());
        view.getBtnCalifEliminar().addActionListener(e -> onEliminarCalif());
        view.getBtnCalifPromedio().addActionListener(e -> actualizarPromedio());

        // selección en tabla estudiantes
        view.getTblEstudiantes().getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) onSeleccionEstudiante();
                });
    }

    // ================= FORMULARIO =================

    private void limpiarForm() {
        view.txtEstId.setText("");
        view.getTxtEstNombre().setText("");
        view.jTextField2.setText("");
        view.getTxtEstEmail().setText("");
        view.getTxtEstEdad().setText("");
        view.getTblEstudiantes().clearSelection();

        limpiarCalificaciones();
    }

    private Estudiante leerForm() {
        Estudiante e = new Estudiante();

        if (!view.txtEstId.getText().trim().isBlank()) {
            e.setId(Integer.valueOf(view.txtEstId.getText().trim()));
        }

        e.setNombre(view.getTxtEstNombre().getText().trim());
        e.setApellido(view.jTextField2.getText().trim());
        e.setEmail(view.getTxtEstEmail().getText().trim());

        String edadTxt = view.getTxtEstEdad().getText().trim();
        e.setEdad(edadTxt.isBlank() ? null : Integer.valueOf(edadTxt));

        return e;
    }

    // ================= ESTUDIANTES =================

    private void cargarEstudiantes() {
        DefaultTableModel m = (DefaultTableModel) view.getTblEstudiantes().getModel();
        m.setRowCount(0);

        for (Estudiante e : estDao.findAll()) {
            m.addRow(new Object[]{
                    e.getId(), e.getNombre(), e.getApellido(), e.getEmail(), e.getEdad()
            });
        }
    }

    private void onSeleccionEstudiante() {
        int fila = view.getTblEstudiantes().getSelectedRow();
        if (fila < 0) {
            currentEstudianteId = null;
            limpiarCalificaciones();
            return;
        }

        DefaultTableModel m = (DefaultTableModel) view.getTblEstudiantes().getModel();
        Integer id = (Integer) m.getValueAt(fila, 0);

        currentEstudianteId = id;
        view.txtEstId.setText(String.valueOf(id));
        view.getTxtEstNombre().setText((String) m.getValueAt(fila, 1));
        view.jTextField2.setText((String) m.getValueAt(fila, 2));
        view.getTxtEstEmail().setText((String) m.getValueAt(fila, 3));
        view.getTxtEstEdad().setText(String.valueOf(m.getValueAt(fila, 4)));

        cargarCalificaciones(id);
    }

    private void onGuardar() {
        Estudiante e = leerForm();

        if (e.getNombre().isBlank() || e.getApellido().isBlank()) {
            JOptionPane.showMessageDialog(view, "Nombre y Apellido son obligatorios.");
            return;
        }

        if (e.getId() == null) { // INSERT
            int id = estDao.insert(e);
            if (id > 0) {
                JOptionPane.showMessageDialog(view, "Guardado (ID " + id + ")");
                cargarEstudiantes();
                limpiarForm();
            }
        } else { // UPDATE
            if (estDao.update(e)) {
                JOptionPane.showMessageDialog(view, "Actualizado");
                cargarEstudiantes();
            }
        }
    }

    private void onActualizar() {
        Estudiante e = leerForm();
        if (e.getId() == null) {
            JOptionPane.showMessageDialog(view, "No hay ID para actualizar.");
            return;
        }

        if (estDao.update(e)) {
            JOptionPane.showMessageDialog(view, "Actualizado");
            cargarEstudiantes();
        }
    }

    private void onEliminar() {
        Integer id = currentEstudianteId;
        if (id == null) {
            JOptionPane.showMessageDialog(view, "Seleccioná un estudiante.");
            return;
        }

        if (JOptionPane.showConfirmDialog(view, "¿Eliminar ID " + id + "?") == 0) {
            estDao.delete(id);
            cargarEstudiantes();
            limpiarForm();
        }
    }

    // ================= CALIFICACIONES =================

    private void limpiarCalificaciones() {
        DefaultTableModel m = (DefaultTableModel) view.getTblCalificaciones().getModel();
        m.setRowCount(0);

        view.getTxtNota().setText("");
        view.getTxtFecha().setText("");
        view.getLblPromedio().setText("PROMEDIO");
    }

    private void cargarCalificaciones(int estId) {
        DefaultTableModel m = (DefaultTableModel) view.getTblCalificaciones().getModel();
        m.setRowCount(0);

        for (Calificacion c : calDao.listByEstudiante(estId)) {
            m.addRow(new Object[]{
                    c.getId(), c.getMateria(), c.getNota(), c.getFecha()
            });
        }

        actualizarPromedio();
    }

    private void onAgregarCalif() {
        if (currentEstudianteId == null) {
            JOptionPane.showMessageDialog(view, "Seleccioná un estudiante.");
            return;
        }

        String materia = String.valueOf(view.getCmbMateria().getSelectedItem());
        String notaTxt = view.getTxtNota().getText().trim();
        String fechaTxt = view.getTxtFecha().getText().trim();

        Double nota;

        try {
            nota = Double.valueOf(notaTxt);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Nota inválida.");
            return;
        }

        LocalDate fecha = null;

        if (!fechaTxt.isBlank()) {
            try {
                fecha = LocalDate.parse(fechaTxt);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(view, "Fecha inválida (usar yyyy-MM-dd)");
                return;
            }
        }

        calDao.insert(new Calificacion(currentEstudianteId, materia, nota, fecha));
        cargarCalificaciones(currentEstudianteId);
    }

    private void onEliminarCalif() {
        int fila = view.getTblCalificaciones().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(view, "Seleccioná una calificación.");
            return;
        }

        Integer id = (Integer) view.getTblCalificaciones().getValueAt(fila, 0);
        calDao.delete(id);
        cargarCalificaciones(currentEstudianteId);
    }

    private void actualizarPromedio() {
        if (currentEstudianteId == null) {
            view.getLblPromedio().setText("PROMEDIO");
            return;
        }

        Optional<Double> p = calDao.promedioDeEstudiante(currentEstudianteId);
        view.getLblPromedio()
                .setText(p.map(v -> String.format("PROMEDIO: %.2f", v))
                        .orElse("PROMEDIO"));
    }
}
