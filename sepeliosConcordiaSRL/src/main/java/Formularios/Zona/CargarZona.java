package Formularios.Zona;

import Controladores.ZonaControlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CargarZona extends JFrame {

    private JTextField txtDescripcion;
    private JButton btnGuardar;

    public CargarZona() {
        setTitle("Cargar Zona");
        setSize(600, 200);  // Aumentar el tamaño de la ventana para acomodar el campo de texto
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre componentes
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtDescripcion = new JTextField();
        txtDescripcion.setPreferredSize(new Dimension(300, 25));  // Ajustar tamaño para aproximar a 15 cm
        btnGuardar = new JButton("Guardar");

        // Configurar el GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Configurar el ancho de la celda
        panel.add(new JLabel("Descripción de la Zona:"), gbc);

        gbc.gridx = 1;  // Ajustar el GridBagConstraints para colocar el campo de texto
        panel.add(txtDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ocupa dos columnas para centrar el botón
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnGuardar, gbc);

        add(panel);

        // Acción de Guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarZona();
            }
        });

        setVisible(true);
    }

    private void guardarZona() {
        String descripcion = txtDescripcion.getText();
        if (!descripcion.isEmpty()) {
            ZonaControlador zonaControlador = new ZonaControlador();
            boolean resultado = zonaControlador.agregarZona(descripcion);
            if (resultado) {
                JOptionPane.showMessageDialog(this, "Zona guardada exitosamente.");
                txtDescripcion.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la zona.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.");
        }
    }
}
