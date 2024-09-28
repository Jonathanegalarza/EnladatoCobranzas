package Formularios.Cliente;

import Controladores.ClienteControlador;
import Controladores.PlanControlador;
import Controladores.ZonaControlador;
import Entidades.Plan;
import Entidades.Zona;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearCliente extends JFrame {
    private JTextField txtNombre, txtApellido, txtDni, txtDomicilio, txtTelefono, txtUltimoPeriodoPago;
    private JComboBox<String> comboPlan, comboZona;
    private Map<String, Integer> planMap, zonaMap;
    private SimpleDateFormat sdf;

    public CrearCliente() {
        setTitle("Cargar Cliente");
        setSize(500, 500);  // Tamaño ajustado
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel principal con GridBagLayout para mejor control del diseño
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Espaciado entre componentes

        planMap = new HashMap<>();
        zonaMap = new HashMap<>();
        sdf = new SimpleDateFormat("MM/yy");
        sdf.setLenient(false);

        // Configuración de componentes
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(20);  // Ancho aumentado
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1;
        txtApellido = new JTextField(20);  // Ancho aumentado
        panel.add(txtApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("DNI:"), gbc);

        gbc.gridx = 1;
        txtDni = new JTextField(20);  // Ancho aumentado
        panel.add(txtDni, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Domicilio:"), gbc);

        gbc.gridx = 1;
        txtDomicilio = new JTextField(20);  // Ancho aumentado
        panel.add(txtDomicilio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        txtTelefono = new JTextField(20);  // Ancho aumentado
        panel.add(txtTelefono, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Zona:"), gbc);

        gbc.gridx = 1;
        comboZona = new JComboBox<>();
        cargarZonas();  // Llamar al método para cargar zonas
        panel.add(comboZona, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Periodo de Pago (MM/YY):"), gbc);

        gbc.gridx = 1;
        txtUltimoPeriodoPago = new JTextField(20);  // Ancho aumentado
        panel.add(txtUltimoPeriodoPago, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Plan:"), gbc);

        gbc.gridx = 1;
        comboPlan = new JComboBox<>();
        cargarPlanes();  // Llamar al método para cargar planes
        panel.add(comboPlan, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarCliente());
        panel.add(btnGuardar, gbc);

        add(panel);
        setVisible(true);
    }

    // Método para cargar los planes en el JComboBox y en el mapa
    private void cargarPlanes() {
        PlanControlador planControlador = new PlanControlador();
        try {
            List<Plan> planes = planControlador.obtenerPlanes();
            for (Plan plan : planes) {
                comboPlan.addItem(plan.getNombrePlan());
                planMap.put(plan.getNombrePlan(), plan.getIdPlan());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los planes.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Método para cargar las zonas en el JComboBox y en el mapa
    private void cargarZonas() {
        ZonaControlador zonaControlador = new ZonaControlador();
        List<Zona> zonas = zonaControlador.obtenerZonas();
        for (Zona zona : zonas) {
            comboZona.addItem(zona.getNombreZona());
            zonaMap.put(zona.getNombreZona(), zona.getIdZona());
        }
    }

    // Método para guardar el cliente en la base de datos
    private void guardarCliente() {
        ClienteControlador clienteControlador = new ClienteControlador();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String dni = txtDni.getText();
        String domicilio = txtDomicilio.getText();
        String telefono = txtTelefono.getText();
        String zonaSeleccionada = (String) comboZona.getSelectedItem();
        int idZona = zonaMap.get(zonaSeleccionada);  // Obtener el id de la zona seleccionada
        String periodoPagoStr = txtUltimoPeriodoPago.getText();
        String planSeleccionado = (String) comboPlan.getSelectedItem();
        int idPlan = planMap.get(planSeleccionado);

        try {
            Date periodoPago = sdf.parse(periodoPagoStr);
            java.sql.Date periodoPagoSQL = new java.sql.Date(periodoPago.getTime());
            boolean resultado = clienteControlador.agregarCliente(nombre, apellido, dni, domicilio, telefono, idZona, idPlan, periodoPagoSQL);
            if (resultado) {
                JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente.");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el cliente.");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use MM/YY.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar el formulario
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtDomicilio.setText("");
        txtTelefono.setText("");
        txtUltimoPeriodoPago.setText("");
        comboPlan.setSelectedIndex(0);
        comboZona.setSelectedIndex(0);
    }
}
