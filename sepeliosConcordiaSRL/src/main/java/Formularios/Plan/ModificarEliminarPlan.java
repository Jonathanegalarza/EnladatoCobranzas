package Formularios.Plan;

import Controladores.PlanControlador;
import Entidades.Plan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModificarEliminarPlan extends JFrame {
    private JTextField txtIdPlan;
    private JTextField txtNombrePlan;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JButton btnBuscar;
    private JButton btnModificar;
    private JButton btnEliminar;

    private PlanControlador planControlador;

    public ModificarEliminarPlan() {
        planControlador = new PlanControlador();

        setTitle("Modificar/Eliminar Plan");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre componentes
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtIdPlan = new JTextField(20);
        txtNombrePlan = new JTextField(20);
        txtDescripcion = new JTextField(20);
        txtPrecio = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        // Configurar el GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID del Plan:"), gbc);

        gbc.gridx = 1;
        panel.add(txtIdPlan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre del Plan:"), gbc);

        gbc.gridx = 1;
        panel.add(txtNombrePlan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panel.add(txtDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Precio:"), gbc);

        gbc.gridx = 1;
        panel.add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(btnBuscar, gbc);

        gbc.gridy = 5;
        panel.add(btnModificar, gbc);

        gbc.gridy = 6;
        panel.add(btnEliminar, gbc);

        add(panel, BorderLayout.CENTER);

        // Acción de Buscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPlan();
            }
        });

        // Acción de Modificar
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarPlan();
            }
        });

        // Acción de Eliminar
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPlan();
            }
        });

        setVisible(true);
    }

    private void buscarPlan() {
        try {
            // Obtener el nombre del plan del campo de texto
            String nombrePlan = txtNombrePlan.getText();

            // Buscar plan por nombre
            Plan plan = planControlador.buscarPlanPorNombre(nombrePlan);
            if (plan == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el plan.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Mostrar datos del plan en los campos de texto
                txtIdPlan.setText(String.valueOf(plan.getIdPlan()));
                txtNombrePlan.setText(plan.getNombrePlan());
                txtDescripcion.setText(plan.getDescripcion());
                txtPrecio.setText(String.valueOf(plan.getPrecio()));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del plan debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarPlan() {
        try {
            int idPlan = Integer.parseInt(txtIdPlan.getText());
            String nombrePlan = txtNombrePlan.getText();
            String descripcion = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());

            boolean exito = planControlador.modificarPlan(idPlan, nombrePlan, descripcion, precio);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Plan modificado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar el plan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del plan y el precio deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPlan() {
        try {
            int idPlan = Integer.parseInt(txtIdPlan.getText());
            boolean exito = planControlador.eliminarPlan(idPlan);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Plan eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtNombrePlan.setText("");
                txtDescripcion.setText("");
                txtPrecio.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el plan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del plan debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
