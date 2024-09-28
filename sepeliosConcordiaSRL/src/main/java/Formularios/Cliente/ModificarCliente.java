package Formularios.Cliente;

import Controladores.ClienteControlador;
import Entidades.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class ModificarCliente extends JFrame {
    private JTextField txtNombre, txtApellido, txtDni, txtDomicilio, txtTelefono, txtZona, txtIdPlan;
    private Cliente cliente;

    public ModificarCliente(Cliente cliente) {
        this.cliente = cliente;
        setTitle("Modificar Cliente");
        setSize(500, 400);  // Tamaño ajustado
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;

        // Añadir campos al panel con GridBagLayout
        panel.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(20);  // Ancho de 5 cm (20 columnas aproximadamente)
        txtNombre.setText(cliente.getNombre());
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Apellido:"), gbc);
        txtApellido = new JTextField(20);
        txtApellido.setText(cliente.getApellido());
        gbc.gridx = 1;
        panel.add(txtApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("DNI:"), gbc);
        txtDni = new JTextField(20);
        txtDni.setText(cliente.getDni());
        gbc.gridx = 1;
        panel.add(txtDni, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Domicilio:"), gbc);
        txtDomicilio = new JTextField(20);
        txtDomicilio.setText(cliente.getDomicilio());
        gbc.gridx = 1;
        panel.add(txtDomicilio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Teléfono:"), gbc);
        txtTelefono = new JTextField(20);
        txtTelefono.setText(cliente.getTelefono());
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Zona:"), gbc);
        txtZona = new JTextField(20);
        txtZona.setText(cliente.getZona());
        gbc.gridx = 1;
        panel.add(txtZona, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("ID Plan:"), gbc);
        txtIdPlan = new JTextField(20);
        txtIdPlan.setText(String.valueOf(cliente.getIdPlan()));
        gbc.gridx = 1;
        panel.add(txtIdPlan, gbc);

        // Botón de guardar
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarCliente();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Botón ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnGuardar, gbc);

        add(panel);
        setVisible(true);
    }

    private void modificarCliente() {
        ClienteControlador clienteControlador = new ClienteControlador();

        cliente.setNombre(txtNombre.getText());
        cliente.setApellido(txtApellido.getText());
        cliente.setDni(txtDni.getText());
        cliente.setDomicilio(txtDomicilio.getText());
        cliente.setTelefono(txtTelefono.getText());
        cliente.setZona(txtZona.getText());
        cliente.setIdPlan(Integer.parseInt(txtIdPlan.getText()));

        boolean exito = clienteControlador.modificarCliente(cliente.getIdCliente(), 
                cliente.getNombre(), 
                cliente.getApellido(), 
                cliente.getDni(), 
                cliente.getDomicilio(), 
                cliente.getTelefono(), 
                cliente.getZona(), 
                cliente.getIdPlan(), 
                (Date) cliente.getUltimoPeriodoPago());

        if (exito) {
            JOptionPane.showMessageDialog(this, "Cliente modificado correctamente.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar cliente.");
        }
    }
}
