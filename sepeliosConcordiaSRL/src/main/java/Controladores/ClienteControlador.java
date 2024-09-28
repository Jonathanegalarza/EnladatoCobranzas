package Controladores;

import Configuracion.CConection;
import Entidades.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteControlador {

    // Método para agregar cliente con el nuevo campo 'periodoPago'
    public boolean agregarCliente(String nombre, String apellido, String dni, String domicilio, String telefono, int idZona, int idPlan, java.sql.Date ultimoPeriodoPago) {
        String query = "INSERT INTO clientes (nombre, apellido, dni, domicilio, telefono, id_zona, id_plan, ultimo_periodo_pago) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, dni);
            pstmt.setString(4, domicilio);
            pstmt.setString(5, telefono);
            pstmt.setInt(6, idZona);
            pstmt.setInt(7, idPlan);
            pstmt.setDate(8, ultimoPeriodoPago);  // Nuevo campo periodoPago

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar cliente por DNI, incluyendo el campo 'periodoPago'
    public List<String> buscarClientePorDNI(String dni) {
        String query = "SELECT * FROM clientes WHERE dni = ?";
        List<String> cliente = new ArrayList<>();
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                cliente.add(rs.getString("nombre"));
                cliente.add(rs.getString("apellido"));
                cliente.add(rs.getString("dni"));
                cliente.add(rs.getString("domicilio"));
                cliente.add(rs.getString("telefono"));
                cliente.add(rs.getString("id_zona"));
                cliente.add(String.valueOf(rs.getInt("id_plan")));
                cliente.add(rs.getString("ultimo_periodo_pago"));  // Nuevo campo periodoPago
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    // Método para buscar cliente por ID, incluyendo 'periodoPago'
    public Cliente buscarClientePorID(int idCliente) {
        String query = "SELECT * FROM clientes WHERE id_cliente = ?";
        Cliente cliente = null;
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("dni"),
                    rs.getString("domicilio"),
                    rs.getString("telefono"),
                    rs.getString("id_zona"),
                    rs.getInt("id_plan"),
                    rs.getDate("ultimo_periodo_pago")  // Nuevo campo periodoPago
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    // Método para buscar cliente por nombre o apellido, incluyendo 'periodoPago'
    public List<Cliente> buscarClientePorNombreOApellido(String termino) {
        String query = "SELECT * FROM clientes WHERE nombre LIKE ? OR apellido LIKE ?";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Usamos % para buscar coincidencias parciales
            pstmt.setString(1, "%" + termino + "%");
            pstmt.setString(2, "%" + termino + "%");

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
                    rs.getDate("ultimo_periodo_pago")  // Nuevo campo periodoPago
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    // Método para modificar cliente, incluyendo 'periodoPago'
    public boolean modificarCliente(int idCliente, String nombre, String apellido, String dni, String domicilio, String telefono, String idZona, int idPlan, Date UltimoPeriodoPago) {
        String query = "UPDATE clientes SET nombre = ?, apellido = ?, dni = ?, domicilio = ?, telefono = ?, id_zona = ?, id_plan = ?, ultimo_periodo_pago = ? WHERE id_cliente = ?";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, dni);
            pstmt.setString(4, domicilio);
            pstmt.setString(5, telefono);
            pstmt.setString(6, idZona);
            pstmt.setInt(7, idPlan);
            pstmt.setDate(8, UltimoPeriodoPago);  // Nuevo campo periodoPago
            pstmt.setInt(9, idCliente);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar cliente (sin cambios, ya que no afecta al nuevo campo)
    public boolean eliminarCliente(int idCliente) {
        String query = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idCliente);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
