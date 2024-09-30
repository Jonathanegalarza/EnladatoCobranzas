package Controladores;

import Configuracion.CConection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TalonarioControlador {

    // Método para generar talonarios y actualizar el último periodo de pago
   public void generarTalonarios(int idCobrador, String ultimoPeriodoPago, List<Integer> idsClientes, java.sql.Date nuevoPeriodo) {
    try {
        // Ruta del archivo .jasper (el informe ya compilado)
        String jasperPath = "src/main/resources/talonarios.jasper";

        // Cargar el informe
        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);

        // Establecer parámetros para el informe (si es necesario)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idCobrador", idCobrador);
        parameters.put("EspacioEntreTalonarios", 28); // Altura en px para el espacio entre talonarios
        parameters.put("ultimoPeriodoPago", nuevoPeriodo); // Añadir el periodo aquí si es necesario

        // Llenar el informe con los datos
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, CConection.getConnection());

        // Mostrar el informe en un visor de JasperReports
        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setVisible(true);

        // Actualizar el último periodo de pago para los clientes
        actualizarUltimoPeriodoPago(idsClientes, nuevoPeriodo);

    } catch (JRException e) {
        System.err.println("Error al generar el informe: " + e.getMessage());
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        System.err.println("Error de fecha: " + e.getMessage());
    }
}
public void generarTalonarioCliente(int idCliente, String ultimoPeriodoPago, java.sql.Date nuevoPeriodo) {
        try {
            // Ruta del archivo .jasper (el informe ya compilado)
            String jasperPath = "src/main/resources/talonarios.jasper";

            // Cargar el informe
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);

            // Establecer parámetros para el informe
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("idCliente", idCliente);  // Parámetro con el id del cliente
            parameters.put("ultimoPeriodoPago", nuevoPeriodo);  // Periodo del cliente
            parameters.put("EspacioEntreTalonarios", 28);  // Espacio entre talonarios (si aplicas espaciado)

            // Llenar el informe con los datos del cliente seleccionado
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, CConection.getConnection());

            // Mostrar el informe en un visor de JasperReports
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);

            // Actualizar el último periodo de pago para el cliente seleccionado
            actualizarUltimoPeriodoPago(idCliente, nuevoPeriodo);

        } catch (JRException e) {
            System.err.println("Error al generar el informe: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error de fecha: " + e.getMessage());
        }
    }


// Método adicional para convertir el periodo de pago en formato MM/yy a java.sql.Date
public java.sql.Date convertirAPeriodo(String periodoPago) {
    try {
        // Verificar que el periodo de pago no sea nulo o vacío
        if (periodoPago == null || periodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El periodo de pago no puede estar vacío.");
        }

        // Convertir el periodo de pago (ej. "09/24") a una fecha
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        sdf.setLenient(false);
        java.util.Date utilDate = sdf.parse("01/" + periodoPago); // Usar el primer día del mes para crear la fecha
        return new java.sql.Date(utilDate.getTime());
    } catch (ParseException e) {
        throw new IllegalArgumentException("Formato de fecha inválido. Use 'MM/yy'.", e);
    }
}
public boolean actualizarUltimoPeriodoPago(int idCliente, Date nuevoPeriodo) {
        String sql = "UPDATE clientes SET ultimo_periodo_pago = ? WHERE id_cliente = ?";
        try (Connection connection = CConection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, nuevoPeriodo);  // Establecer el nuevo periodo de pago
            statement.setInt(2, idCliente);  // Establecer el id del cliente

            // Ejecutar la actualización
            int filasActualizadas = statement.executeUpdate();

            System.out.println("Último periodo de pago actualizado para el cliente con ID: " + idCliente);
            return filasActualizadas == 1;  // Devuelve true si se realizó una actualización exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Devolver false en caso de error
        }
    }

// Método para actualizar el último periodo de pago de múltiples clientes usando batch
public boolean actualizarUltimoPeriodoPago(List<Integer> idClientes, Date nuevoPeriodo) {
    String sql = "UPDATE clientes SET ultimo_periodo_pago = ? WHERE id_cliente = ?";
    try (Connection connection = CConection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        for (int idCliente : idClientes) {
            statement.setDate(1, new java.sql.Date(nuevoPeriodo.getTime()));  // Convertir Date a java.sql.Date
            statement.setInt(2, idCliente);
            statement.addBatch();  // Añadir a batch
        }

        // Ejecutar todas las actualizaciones en batch
        int[] filasActualizadas = statement.executeBatch();

        System.out.println("Último periodo de pago actualizado para " + filasActualizadas.length + " clientes.");
        return filasActualizadas.length == idClientes.size();  // Devuelve true si todas las actualizaciones se realizaron
    } catch (SQLException e) {
        e.printStackTrace();
        return false;  // Devolver false en caso de error
    }
}
}
