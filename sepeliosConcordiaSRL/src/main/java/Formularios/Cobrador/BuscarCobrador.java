package Formularios.Cobrador;

import Controladores.CobradorControlador;
import Controladores.TalonarioControlador;
import Entidades.Cliente;
import Entidades.Cobrador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class BuscarCobrador extends JFrame {
    private JTextField txtBusqueda;
    private JTable tablaCobradores;
    private DefaultTableModel tableModel;
    private Cobrador cobradorSeleccionado;

    public BuscarCobrador() {
        setTitle("Buscar Cobrador");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por nombre:"));
        txtBusqueda = new JTextField(20);
        panelBusqueda.add(txtBusqueda);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarCobradores());
        panelBusqueda.add(btnBuscar);
        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla para mostrar los resultados
        String[] columnas = {"ID", "Nombre", "Apellido", "Domicilio", "Telefono", "Zonas"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaCobradores = new JTable(tableModel);
        tablaCobradores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaCobradores.getSelectedRow();
                if (filaSeleccionada != -1) {
                    int idCobrador = (int) tableModel.getValueAt(filaSeleccionada, 0);
                    CobradorControlador cobradorControlador = new CobradorControlador();
                    cobradorSeleccionado = cobradorControlador.buscarCobradorPorID(idCobrador);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tablaCobradores);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para modificar e imprimir talonarios
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnModificarCobrador = new JButton("Modificar Cobrador");
        btnModificarCobrador.addActionListener(e -> modificarCobrador());
        panelBotones.add(btnModificarCobrador);

        JButton btnImprimirTalonarios = new JButton("Imprimir Talonarios");
        btnImprimirTalonarios.addActionListener(e -> imprimirTalonariosCobrador());
        panelBotones.add(btnImprimirTalonarios);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    // Método para buscar cobradores por nombre
    private void buscarCobradores() {
        CobradorControlador cobradorControlador = new CobradorControlador();
        String termino = txtBusqueda.getText().trim();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para buscar.");
            return;
        }

        List<Cobrador> cobradores = cobradorControlador.buscarCobradorPorNombre(termino, "");

        // Limpiar la tabla antes de mostrar nuevos resultados
        tableModel.setRowCount(0);

        // Mostrar los resultados en la tabla
        for (Cobrador cobrador : cobradores) {
            Object[] fila = {
                cobrador.getIdCobrador(),
                cobrador.getNombre(),
                cobrador.getApellido(),
                cobrador.getDomicilio(),
                cobrador.getTelefono(),
                cobrador.getZonas().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
            };
            tableModel.addRow(fila);
        }

        if (cobradores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron cobradores.");
        }
    }

    // Método para modificar el cobrador seleccionado
    private void modificarCobrador() {
        if (cobradorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cobrador para modificar.");
            return;
        }
        new ModificarCobrador(cobradorSeleccionado);
    }

    // Método para imprimir los talonarios del cobrador seleccionado
    private void imprimirTalonariosCobrador() {
    if (cobradorSeleccionado == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un cobrador para imprimir talonarios.");
        return;
    }

    String periodoPago = JOptionPane.showInputDialog(this, "Ingrese el periodo de pago (ej. MM/yy):");

    if (periodoPago == null || !isValidPeriodFormat(periodoPago)) {
        JOptionPane.showMessageDialog(this, "Debe ingresar un periodo de pago válido en formato 'MM/yy'.");
        return;
    }

    java.sql.Date sqlDate;
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        sdf.setLenient(false);
        java.util.Date utilDate = sdf.parse(periodoPago); // Parseamos el periodo ingresado

        // Crear un calendario para ajustar la fecha al último día del mes
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);

        // Ajustar el año para ser el año actual + 2000
        int year = cal.get(Calendar.YEAR);
        if (year < 100) {
            year += 2000; // Asegúrate de que el año sea del siglo XXI
        }
        cal.set(Calendar.YEAR, year);
        
        // Establecer el día al último día del mes
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        sqlDate = new java.sql.Date(cal.getTimeInMillis()); // Convertir a java.sql.Date
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use 'MM/yy'.");
        return;
    }

    CobradorControlador cobradorControlador = new CobradorControlador();
    List<Cliente> clientes = cobradorControlador.obtenerClientesPorZonas(cobradorSeleccionado.getIdCobrador());

    // Depuración: imprimir los IDs de los clientes seleccionados
    System.out.println("Clientes asociados al cobrador: ");
    for (Cliente cliente : clientes) {
        System.out.println("Cliente ID: " + cliente.getIdCliente());
    }

    List<Integer> idClientes = clientes.stream().map(Cliente::getIdCliente).collect(Collectors.toList());

    TalonarioControlador talonarioControlador = new TalonarioControlador();
    talonarioControlador.generarTalonarios(cobradorSeleccionado.getIdCobrador(), periodoPago, idClientes, sqlDate); // Añadir sqlDate aquí

    // Actualizar el campo UltimoPeriodoPago de cada cliente relacionado con el cobrador
    for (Cliente cliente : clientes) {
        try {
            cobradorControlador.actualizarUltimoPeriodoPago(cliente.getIdCliente(), sqlDate);
        } catch (Exception e) {
            System.err.println("Error al actualizar el periodo para el cliente ID " + cliente.getIdCliente() + ": " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al actualizar el periodo para el cliente ID " + cliente.getIdCliente());
        }
    }

    JOptionPane.showMessageDialog(this, "Talonarios generados y periodo de pago actualizado.");
}

// Método para validar el formato MM/AA
private boolean isValidPeriodFormat(String periodo) {
    String regex = "^(0[1-9]|1[0-2])/\\d{2}$"; // Formato MM/AA
    return periodo.matches(regex);
}

}
