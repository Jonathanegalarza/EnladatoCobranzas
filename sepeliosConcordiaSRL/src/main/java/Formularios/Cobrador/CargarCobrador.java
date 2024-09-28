package Formularios.Cobrador;

import Controladores.CobradorControlador;
import Controladores.ZonaControlador;
import Entidades.Zona;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CargarCobrador extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDomicilio;  // Campo para domicilio
    private JTextField txtTelefono;   // Campo para teléfono
    private JComboBox<String> cboZonas;  // JComboBox para zonas
    private DefaultComboBoxModel<String> zonaComboBoxModel;

    public CargarCobrador() {
        setTitle("Cargar Cobrador");
        setSize(500, 300);  // Tamaño ajustado para el formulario
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Crear panel con GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Espaciado entre componentes

        // Configuración del GridBagConstraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre del Cobrador:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtNombre = new JTextField();
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Apellido del Cobrador:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtApellido = new JTextField();
        panel.add(txtApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Domicilio:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtDomicilio = new JTextField();
        panel.add(txtDomicilio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtTelefono = new JTextField();
        panel.add(txtTelefono, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Zonas:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        zonaComboBoxModel = new DefaultComboBoxModel<>();
        cboZonas = new JComboBox<>(zonaComboBoxModel);
        cboZonas.setEditable(true);  // Permite escribir zonas nuevas (opcional)
        cargarZonas();  // Cargar zonas disponibles
        panel.add(cboZonas, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCobrador();  // Guardar el cobrador
            }
        });
        panel.add(btnGuardar, gbc);

        add(panel);
        setVisible(true);
    }

    // Método para cargar las zonas disponibles en el JComboBox
    private void cargarZonas() {
        ZonaControlador zonaControlador = new ZonaControlador();
        List<Zona> zonas = zonaControlador.obtenerZonas();
        for (Zona zona : zonas) {
            zonaComboBoxModel.addElement(zona.getNombreZona());
        }
    }

    // Método para guardar el cobrador con nombre, apellido, domicilio, teléfono y zonas
    private void guardarCobrador() {
        CobradorControlador cobradorControlador = new CobradorControlador();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String domicilio = txtDomicilio.getText();  // Obtener el valor del domicilio
        String telefono = txtTelefono.getText();    // Obtener el valor del teléfono
        String zonaSeleccionada = (String) cboZonas.getSelectedItem();

        // Convertir nombre de zona a ID
        ZonaControlador zonaControlador = new ZonaControlador();
        List<Integer> idsZonas = zonaControlador.obtenerIdsZonas(List.of(zonaSeleccionada));

        // Enviar nombre, apellido, domicilio, teléfono y zonas al controlador
        boolean resultado = cobradorControlador.agregarCobrador(nombre, apellido, domicilio, telefono, idsZonas);
        if (resultado) {
            JOptionPane.showMessageDialog(this, "Cobrador guardado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el cobrador.");
        }
    }
}
