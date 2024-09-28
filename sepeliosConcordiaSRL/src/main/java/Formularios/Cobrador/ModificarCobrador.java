package Formularios.Cobrador;

import Controladores.CobradorControlador;
import Entidades.Cobrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class ModificarCobrador extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDomicilio;
    private JTextField txtTelefono;
    private JTextField txtZonas;  // Para mostrar las zonas pre-cargadas
    private Cobrador cobrador;

    public ModificarCobrador(Cobrador cobrador) {
        this.cobrador = cobrador;
        setTitle("Modificar Cobrador");
        setSize(500, 350);  // Tamaño ajustado
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;

        // Añadir campos al panel con GridBagLayout
        panel.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(20);  // Ancho de 5 cm (20 columnas aproximadamente)
        txtNombre.setText(cobrador.getNombre());
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Apellido:"), gbc);
        txtApellido = new JTextField(20);
        txtApellido.setText(cobrador.getApellido());
        gbc.gridx = 1;
        panel.add(txtApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Domicilio:"), gbc);
        txtDomicilio = new JTextField(20);
        txtDomicilio.setText(cobrador.getDomicilio());
        gbc.gridx = 1;
        panel.add(txtDomicilio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Teléfono:"), gbc);
        txtTelefono = new JTextField(20);
        txtTelefono.setText(cobrador.getTelefono());
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Zonas (IDs separadas por comas):"), gbc);
        String zonasTexto = cobrador.getZonas().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        txtZonas = new JTextField(20);  // Ancho de 5 cm (20 columnas aproximadamente)
        txtZonas.setText(zonasTexto);
        gbc.gridx = 1;
        panel.add(txtZonas, gbc);

        // Botón de modificar
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarCobrador();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Botón ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnModificar, gbc);

        add(panel);
        setVisible(true);
    }

    private void modificarCobrador() {
        CobradorControlador controlador = new CobradorControlador();

        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String domicilio = txtDomicilio.getText();
        String telefono = txtTelefono.getText();

        // Convertir el texto de las zonas en una lista de enteros (IDs de zonas)
        List<Integer> zonasIds;
        try {
            zonasIds = List.of(txtZonas.getText().split(",\\s*")).stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Asegúrate de que las zonas sean números separados por comas.");
            return;
        }

        // Llamar al método del controlador con los parámetros correctos
        boolean resultado = controlador.modificarCobrador(cobrador.getIdCobrador(), nombre, apellido, domicilio, telefono, zonasIds);
        if (resultado) {
            JOptionPane.showMessageDialog(this, "Cobrador modificado con éxito.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el cobrador.");
        }
    }
}
