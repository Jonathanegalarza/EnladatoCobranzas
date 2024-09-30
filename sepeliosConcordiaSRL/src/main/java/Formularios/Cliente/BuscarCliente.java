package Formularios.Cliente;

import Controladores.ClienteControlador;
import Controladores.TalonarioControlador;
import Entidades.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BuscarCliente extends JFrame {
    private JTextField txtBusqueda;
    private JTable tablaClientes;
    private DefaultTableModel tableModel;

    public BuscarCliente() {
        setTitle("Buscar Cliente");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por nombre o apellido:"));
        txtBusqueda = new JTextField(20);
        panelBusqueda.add(txtBusqueda);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarClientes());
        panelBusqueda.add(btnBuscar);
        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla para mostrar los resultados
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Domicilio", "Teléfono", "Zona", "ID Plan"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaClientes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para modificar, eliminar, e imprimir talonarios
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnModificarCliente = new JButton("Modificar Cliente");
        btnModificarCliente.addActionListener(e -> modificarCliente());
        panelBotones.add(btnModificarCliente);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarCliente());
        panelBotones.add(btnEliminar);
        
        // Nuevo botón para imprimir el talonario
        JButton btnImprimirTalonario = new JButton("Imprimir Talonario");
        btnImprimirTalonario.addActionListener(e -> imprimirTalonarioCliente());
        panelBotones.add(btnImprimirTalonario);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    // Método para buscar clientes por nombre o apellido
    private void buscarClientes() {
        ClienteControlador clienteControlador = new ClienteControlador();
        String termino = txtBusqueda.getText().trim();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre o apellido para buscar.");
            return;
        }

        List<Cliente> clientes = clienteControlador.buscarClientePorNombreOApellido(termino);

        // Limpiar la tabla antes de mostrar nuevos resultados
        tableModel.setRowCount(0);

        // Mostrar los resultados en la tabla
        for (Cliente cliente : clientes) {
            Object[] fila = {
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getDni(),
                    cliente.getDomicilio(),
                    cliente.getTelefono(),
                    cliente.getZona(),
                    cliente.getIdPlan()
            };
            tableModel.addRow(fila);
        }

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron clientes.");
        }
    }

    // Método para modificar el cliente seleccionado
    private void modificarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para modificar.");
            return;
        }

        // Obtener el ID del cliente seleccionado
        int idCliente = (int) tableModel.getValueAt(filaSeleccionada, 0);
        ClienteControlador clienteControlador = new ClienteControlador();
        Cliente cliente = clienteControlador.buscarClientePorID(idCliente);

        if (cliente != null) {
            new ModificarCliente(cliente);  // Abre el formulario para modificar el cliente
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se pudo encontrar el cliente.");
        }
    }

    // Método para eliminar el cliente seleccionado
    private void eliminarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Obtener el ID del cliente seleccionado
            int idCliente = (int) tableModel.getValueAt(filaSeleccionada, 0);
            ClienteControlador clienteControlador = new ClienteControlador();
            boolean resultado = clienteControlador.eliminarCliente(idCliente);

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                buscarClientes();  // Actualizar la tabla después de eliminar
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.");
            }
        }
    }

    // Método para imprimir el talonario del cliente seleccionado
    private void imprimirTalonarioCliente() {
    int filaSeleccionada = tablaClientes.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un cliente para imprimir el talonario.");
        return;
    }

    // Obtener el ID del cliente seleccionado
    int idCliente = (int) tableModel.getValueAt(filaSeleccionada, 0);
    ClienteControlador clienteControlador = new ClienteControlador();
    Cliente cliente = clienteControlador.buscarClientePorID(idCliente);

    if (cliente == null) {
        JOptionPane.showMessageDialog(this, "No se encontró el cliente.");
        return;
    }

    // Solicitar al usuario el periodo de pago en formato 'MM/yy'
    String periodoPago = JOptionPane.showInputDialog(this, "Ingrese el periodo de pago (ej. MM/yy):");
    if (periodoPago == null || !isValidPeriodFormat(periodoPago)) {
        JOptionPane.showMessageDialog(this, "Debe ingresar un periodo de pago válido en formato 'MM/yy'.");
        return;
    }

    // Convertir el periodo de pago usando el método convertirAPeriodo() del TalonarioControlador
    TalonarioControlador talonarioControlador = new TalonarioControlador();
    java.sql.Date sqlDate;
    try {
        sqlDate = talonarioControlador.convertirAPeriodo(periodoPago);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
        return;
    }

    // Generar el talonario usando TalonarioControlador
    try {
        talonarioControlador.generarTalonarioCliente(idCliente, periodoPago, sqlDate);
        JOptionPane.showMessageDialog(this, "Talonario generado y periodo de pago actualizado.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al generar el talonario: " + e.getMessage());
    }
}

// Método para validar el formato del periodo de pago 'MM/yy'
private boolean isValidPeriodFormat(String periodo) {
    return periodo.matches("(0[1-9]|1[0-2])/\\d{2}");  // Verifica formato MM/yy
}

}
