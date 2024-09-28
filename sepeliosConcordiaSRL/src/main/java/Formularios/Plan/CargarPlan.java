package Formularios.Plan;

import Controladores.PlanControlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CargarPlan extends JFrame {
    private JTextField txtNombrePlan;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JButton btnGuardar;

    public CargarPlan() {
        setTitle("Cargar Plan");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Configurar el panel y el layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre componentes
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtNombrePlan = new JTextField(20);
        txtDescripcion = new JTextArea(5, 20);
        txtPrecio = new JTextField(20);
        btnGuardar = new JButton("Guardar");

        // Configurar el layout del GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre del Plan:"), gbc);

        gbc.gridx = 1;
        panel.add(txtNombrePlan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panel.add(scrollDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Precio:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnGuardar, gbc);

        add(panel);

        // Acción de Guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPlan();
            }
        });

        setVisible(true);
    }

    private void guardarPlan() {
        PlanControlador planControlador = new PlanControlador();
        String nombrePlan = txtNombrePlan.getText();
        String descripcion = txtDescripcion.getText();
        double precio = 0;

        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean resultado = planControlador.agregarPlan(nombrePlan, descripcion, precio);
        if (resultado) {
            JOptionPane.showMessageDialog(this, "Plan guardado exitosamente.");
            dispose();  // Cierra el formulario después de guardar
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el plan.");
        }
    }
}
