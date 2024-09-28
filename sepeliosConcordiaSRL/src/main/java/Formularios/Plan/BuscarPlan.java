package Formularios.Plan;

import Controladores.PlanControlador;
import Entidades.Plan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuscarPlan extends JFrame {

    private JComboBox<String> comboBoxPlanes;
    private JTextField txtNombrePlan;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JButton btnModificar;
    private JButton btnEliminar;

    private PlanControlador planControlador = new PlanControlador();

    public BuscarPlan() {
        setTitle("Gestión de Planes");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre componentes

        // Inicializar componentes
        comboBoxPlanes = new JComboBox<>();
        txtNombrePlan = new JTextField(20);
        txtDescripcion = new JTextArea(5, 20);
        txtPrecio = new JTextField(10);
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        txtDescripcion.setPreferredSize(new Dimension(300, 100));
        txtPrecio.setPreferredSize(new Dimension(200, 25));

        // Configurar GridBagLayout
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Seleccionar Plan:"), gbc);

        gbc.gridx = 1;
        panel.add(comboBoxPlanes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nuevo Nombre Plan:"), gbc);

        gbc.gridx = 1;
        panel.add(txtNombrePlan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panel.add(new JScrollPane(txtDescripcion), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Precio:"), gbc);

        gbc.gridx = 1;
        panel.add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnModificar, gbc);

        gbc.gridy = 5;
        panel.add(btnEliminar, gbc);

        add(panel);

        // Configurar acción de los botones
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    modificarPlan();
                } catch (SQLException ex) {
                    Logger.getLogger(BuscarPlan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarPlan();
                } catch (SQLException ex) {
                    Logger.getLogger(BuscarPlan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Configurar acción del comboBox para cargar datos del plan seleccionado
        comboBoxPlanes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cargarDatosPlan();
                } catch (SQLException ex) {
                    Logger.getLogger(BuscarPlan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Cargar planes al iniciar el formulario
        cargarPlanes();

        setVisible(true);
    }

    private void cargarPlanes() {
        try {
            List<Plan> planes = planControlador.obtenerPlanes(); // Método para obtener todos los planes
            comboBoxPlanes.removeAllItems();
            for (Plan plan : planes) {
                comboBoxPlanes.addItem(plan.getNombrePlan());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosPlan() throws SQLException {
        String nombrePlanSeleccionado = (String) comboBoxPlanes.getSelectedItem();
        if (nombrePlanSeleccionado != null) {
            Plan plan = planControlador.buscarPlanPorNombre(nombrePlanSeleccionado);
            if (plan != null) {
                txtNombrePlan.setText(plan.getNombrePlan());
                txtDescripcion.setText(plan.getDescripcion());
                txtPrecio.setText(String.valueOf(plan.getPrecio()));
            }
        }
    }

    private void modificarPlan() throws SQLException {
        String nombrePlanSeleccionado = (String) comboBoxPlanes.getSelectedItem();
        String nuevoNombre = txtNombrePlan.getText();
        String descripcion = txtDescripcion.getText();
        double precio = 0.0;

        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombrePlanSeleccionado != null && !nuevoNombre.isEmpty()) {
            Plan plan = planControlador.buscarPlanPorNombre(nombrePlanSeleccionado);
            if (plan != null) {
                boolean exito = planControlador.modificarPlan(plan.getIdPlan(), nuevoNombre, descripcion, precio);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Plan modificado con éxito");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar el plan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el plan para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El nombre del plan no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPlan() throws SQLException {
    String nombrePlan = (String) comboBoxPlanes.getSelectedItem();

    if (nombrePlan != null) {
        Plan plan = planControlador.buscarPlanPorNombre(nombrePlan);
        if (plan != null) {
            // Mostrar un diálogo de confirmación antes de eliminar
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de que desea eliminar el plan '" + nombrePlan + "'?",
                    "Confirmación de Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // Si el usuario confirma la eliminación
            if (confirm == JOptionPane.YES_OPTION) {
                planControlador.eliminarPlan(plan.getIdPlan());
                JOptionPane.showMessageDialog(this, "Plan eliminado con éxito");
                cargarPlanes(); // Actualizar la lista después de eliminar
                txtNombrePlan.setText("");
                txtDescripcion.setText("");
                txtPrecio.setText("");
            } else {
                // Si el usuario cancela la eliminación
                JOptionPane.showMessageDialog(this, "Eliminación cancelada", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el plan para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún plan para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
