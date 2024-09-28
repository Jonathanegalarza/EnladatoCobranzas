package Controladores;

import Configuracion.CConection;
import Entidades.Cliente;
import Entidades.Cobrador;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CobradorControlador {

    // Método para agregar un cobrador
    public boolean agregarCobrador(String nombre, String apellido, String domicilio, String telefono, List<Integer> zonas) {
        String queryCobrador = "INSERT INTO cobradores (nombre, apellido, domicilio, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmtCobrador = conn.prepareStatement(queryCobrador, Statement.RETURN_GENERATED_KEYS)) {

            pstmtCobrador.setString(1, nombre);
            pstmtCobrador.setString(2, apellido);
            pstmtCobrador.setString(3, domicilio);
            pstmtCobrador.setString(4, telefono);
            pstmtCobrador.executeUpdate();

            // Obtener el ID del cobrador insertado
            try (ResultSet rs = pstmtCobrador.getGeneratedKeys()) {
                if (rs.next()) {
                    int idCobrador = rs.getInt(1);
                    asignarZonas(conn, idCobrador, zonas);
                }
            }
            return true;
        } catch (SQLException e) {
            mostrarError("Error al agregar cobrador: " + e);
            return false;
        }
    }

    // Método para asignar zonas al cobrador
    private void asignarZonas(Connection conn, int idCobrador, List<Integer> zonas) throws SQLException {
        String queryCobradorZona = "INSERT INTO cobrador_zona (id_cobrador, id_zona) VALUES (?, ?)";
        try (PreparedStatement pstmtCobradorZona = conn.prepareStatement(queryCobradorZona)) {
            for (int idZona : zonas) {
                pstmtCobradorZona.setInt(1, idCobrador);
                pstmtCobradorZona.setInt(2, idZona);
                pstmtCobradorZona.addBatch();
            }
            pstmtCobradorZona.executeBatch();
        }
    }

    // Buscar cobrador por nombre y apellido
    public List<Cobrador> buscarCobradorPorNombre(String nombre, String apellido) {
        String query = "SELECT c.id_cobrador, c.nombre, c.apellido, c.domicilio, c.telefono, z.id_zona " +
                       "FROM cobradores c " +
                       "LEFT JOIN cobrador_zona cz ON c.id_cobrador = cz.id_cobrador " +
                       "LEFT JOIN zonas z ON cz.id_zona = z.id_zona " +
                       "WHERE c.nombre LIKE ? AND c.apellido LIKE ?";
        List<Cobrador> cobradores = new ArrayList<>();

        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + nombre + "%");
            pstmt.setString(2, "%" + apellido + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cobrador cobrador = new Cobrador();
                cobrador.setIdCobrador(rs.getInt("id_cobrador"));
                cobrador.setNombre(rs.getString("nombre"));
                cobrador.setApellido(rs.getString("apellido"));
                cobrador.setDomicilio(rs.getString("domicilio"));
                cobrador.setTelefono(rs.getString("telefono"));
                cobrador.addZona(rs.getInt("id_zona"));
                cobradores.add(cobrador);
            }
        } catch (SQLException e) {
            mostrarError("Error al buscar cobrador: " + e);
        }
        return cobradores;
    }

    // Buscar cobrador por ID
    public Cobrador buscarCobradorPorID(int idCobrador) {
        String query = "SELECT c.id_cobrador, c.nombre, c.apellido, c.domicilio, c.telefono, cz.id_zona " +
                       "FROM cobradores c " +
                       "LEFT JOIN cobrador_zona cz ON c.id_cobrador = cz.id_cobrador " +
                       "WHERE c.id_cobrador = ?";
        Cobrador cobrador = null;

        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idCobrador);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                cobrador = new Cobrador();
                cobrador.setIdCobrador(rs.getInt("id_cobrador"));
                cobrador.setNombre(rs.getString("nombre"));
                cobrador.setApellido(rs.getString("apellido"));
                cobrador.setDomicilio(rs.getString("domicilio"));
                cobrador.setTelefono(rs.getString("telefono"));
                do {
                    cobrador.addZona(rs.getInt("id_zona"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            mostrarError("Error al buscar cobrador por ID: " + e);
        }
        return cobrador;
    }

    // Modificar cobrador
    public boolean modificarCobrador(int idCobrador, String nombre, String apellido, String domicilio, String telefono, List<Integer> zonas) {
        String queryCobrador = "UPDATE cobradores SET nombre = ?, apellido = ?, domicilio = ?, telefono = ? WHERE id_cobrador = ?";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmtCobrador = conn.prepareStatement(queryCobrador)) {

            pstmtCobrador.setString(1, nombre);
            pstmtCobrador.setString(2, apellido);
            pstmtCobrador.setString(3, domicilio);
            pstmtCobrador.setString(4, telefono);
            pstmtCobrador.setInt(5, idCobrador);
            pstmtCobrador.executeUpdate();

            // Eliminar zonas actuales y agregar las nuevas
            eliminarZonas(conn, idCobrador);
            asignarZonas(conn, idCobrador, zonas);
            return true;
        } catch (SQLException e) {
            mostrarError("Error al modificar cobrador: " + e);
            return false;
        }
    }

    // Eliminar zonas del cobrador
    private void eliminarZonas(Connection conn, int idCobrador) throws SQLException {
        String deleteQuery = "DELETE FROM cobrador_zona WHERE id_cobrador = ?";
        try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteQuery)) {
            pstmtDelete.setInt(1, idCobrador);
            pstmtDelete.executeUpdate();
        }
    }
public void actualizarUltimoPeriodoPago(int idCliente, Date ultimoPeriodoPago) {
    String sql = "UPDATE clientes SET ultimo_periodo_pago = ? WHERE id_cliente = ?";
    
    try (Connection connection = CConection.getConnection(); // Abre la conexión aquí
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        
        preparedStatement.setDate(1, ultimoPeriodoPago);
        preparedStatement.setInt(2, idCliente);
        preparedStatement.executeUpdate();
    } catch (SQLException e) {
        mostrarError("Error al actualizar último periodo de pago: " + e);
    }
}

    // Borrar cobrador
    public boolean borrarCobrador(int idCobrador) {
        String deleteCobradorQuery = "DELETE FROM cobradores WHERE id_cobrador = ?";
        String deleteCobradorZonaQuery = "DELETE FROM cobrador_zona WHERE id_cobrador = ?";

        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmtDeleteCobradorZona = conn.prepareStatement(deleteCobradorZonaQuery);
             PreparedStatement pstmtDeleteCobrador = conn.prepareStatement(deleteCobradorQuery)) {

            // Eliminar las zonas asociadas
            pstmtDeleteCobradorZona.setInt(1, idCobrador);
            pstmtDeleteCobradorZona.executeUpdate();

            // Eliminar el cobrador
            pstmtDeleteCobrador.setInt(1, idCobrador);
            pstmtDeleteCobrador.executeUpdate();

            return true;
        } catch (SQLException e) {
            mostrarError("Error al borrar cobrador: " + e);
            return false;
        }
    }

    // Obtener clientes por zonas del cobrador
    public List<Cliente> obtenerClientesPorZonas(int idCobrador) {
        String query = "SELECT cl.id_cliente, cl.nombre, cl.apellido, cl.dni, cl.domicilio, cl.telefono, cl.id_zona, cl.id_plan, cl.ultimo_periodo_pago " +
                       "FROM clientes cl " +
                       "JOIN zonas z ON cl.id_zona = z.id_zona " +
                       "JOIN cobrador_zona cz ON cz.id_zona = z.id_zona " +
                       "WHERE cz.id_cobrador = ?";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idCobrador);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("dni"),
                    rs.getString("domicilio"),
                    rs.getString("telefono"),
                    rs.getString("id_zona"),
                    rs.getInt("id_plan"),
                    rs.getDate("ultimo_periodo_pago")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            mostrarError("Error al obtener clientes por zonas: " + e);
        }
        return clientes;
    }

    // Método para imprimir talonario
    public void imprimirTalonario(Cobrador cobrador, List<Cliente> clientes) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("ruta/al/archivo.talonario.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientes);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("NombreCobrador", cobrador.getNombre() + " " + cobrador.getApellido());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            mostrarError("Error al imprimir talonario: " + e);
        }
    }

    // Método para mostrar errores
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
