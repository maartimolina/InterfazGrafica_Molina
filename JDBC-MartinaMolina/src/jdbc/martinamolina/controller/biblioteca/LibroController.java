/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.martinamolina.controller.biblioteca;
import jdbc.martinamolina.dao.biblioteca.LibroDAO;
import jdbc.martinamolina.daoimpl.biblioteca.LibroDAOImpl;
import jdbc.martinamolina.model.biblioteca.Libro;
import jdbc.martinamolina.view.biblioteca.BibliotecaView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author Mar
 */
public class LibroController {
    private final BibliotecaView view;
    private final LibroDAO dao;

    public LibroController(BibliotecaView view) {
        this.view = view;
        this.dao  = new LibroDAOImpl();
        wireEvents();
        refrescarTabla();
        view.setVisible(true); // si prefieres, puedes dejar el setVisible en el main
    }

    private void wireEvents() {
        view.getBtnRefrescar().addActionListener(e -> refrescarTabla());
        view.getBtnSoloDisponibles().addActionListener(e -> cargarTabla(dao.listarDisponibles()));
        view.getBtnBuscarAutor().addActionListener(e -> {
            String filtro = view.getTxtFiltroAutor().getText().trim();
            cargarTabla(dao.buscarPorAutor(filtro));
        });
        view.getBtnNuevo().addActionListener(e -> limpiarForm());
        view.getBtnGuardar().addActionListener(e -> {
            Libro l = tomarDelForm(false);
            if (dao.crear(l) > 0) { refrescarTabla(); limpiarForm(); }
        });
        view.getBtnActualizar().addActionListener(e -> {
            Libro l = tomarDelForm(true);
            if (dao.actualizar(l) > 0) { refrescarTabla(); limpiarForm(); }
        });
        view.getBtnEliminar().addActionListener(e -> {
            String idTxt = view.getTxtId().getText().trim();
            if (!idTxt.isEmpty()) {
                if (dao.eliminar(Integer.parseInt(idTxt)) > 0) { refrescarTabla(); limpiarForm(); }
            }
        });

        // al hacer click en la tabla, llevar los datos al form
        view.getTblLibros().getSelectionModel().addListSelectionListener(e -> {
            int r = view.getTblLibros().getSelectedRow();
            if (r >= 0) {
                JTable t = view.getTblLibros();
                view.getTxtId().setText(t.getValueAt(r, 0).toString());
                view.getTxtTitulo().setText(t.getValueAt(r, 1).toString());
                view.getTxtAutor().setText(t.getValueAt(r, 2).toString());
                view.getTxtAnio().setText(t.getValueAt(r, 3).toString());
                view.getTxtIsbn().setText(t.getValueAt(r, 4).toString());
                view.getChkDisponible().setSelected(Boolean.parseBoolean(t.getValueAt(r, 5).toString()));
            }
        });
    }

    private void refrescarTabla() { cargarTabla(dao.listarTodos()); }

    private void cargarTabla(List<Libro> data) {
        DefaultTableModel m = new DefaultTableModel(
                new Object[]{"ID","Título","Autor","Año","ISBN","Disponible"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Libro l : data) {
            m.addRow(new Object[]{
                    l.getId(), l.getTitulo(), l.getAutor(),
                    l.getAnioPublicacion(), l.getIsbn(), l.isDisponible()
            });
        }
        view.getTblLibros().setModel(m);
    }

    private Libro tomarDelForm(boolean conId) {
        Libro l = new Libro();
        if (conId && !view.getTxtId().getText().isBlank()) {
            l.setId(Integer.parseInt(view.getTxtId().getText().trim()));
        }
        l.setTitulo(view.getTxtTitulo().getText().trim());
        l.setAutor(view.getTxtAutor().getText().trim());
        String anioTxt = view.getTxtAnio().getText().trim();
        l.setAnioPublicacion(anioTxt.isEmpty() ? 0 : Integer.parseInt(anioTxt));
        l.setIsbn(view.getTxtIsbn().getText().trim());
        l.setDisponible(view.getChkDisponible().isSelected());
        return l;
    }

    private void limpiarForm() {
        view.getTxtId().setText("");
        view.getTxtTitulo().setText("");
        view.getTxtAutor().setText("");
        view.getTxtAnio().setText("");
        view.getTxtIsbn().setText("");
        view.getTxtFiltroAutor().setText("");
        view.getChkDisponible().setSelected(false);
        view.getTblLibros().clearSelection();
    }
}
