package Controladores;

import Configuracion.CConection;
import Entidades.Plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jonathan
 */
public class PlanControlador {

    // Agregar Plan
    public boolean agregarPlan(String nombrePlan, String descripcion, double precio) {
        String query = "INSERT INTO planes (nombre_plan, descripcion, precio) VALUES (?, ?, ?)";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nombrePlan);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precio);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Buscar Plan por Nombre
    public Plan buscarPlanPorNombre(String nombrePlan) {
        String query = "SELECT * FROM planes WHERE nombre_plan = ?";
        Plan plan = null;
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nombrePlan);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                plan = new Plan(
                    rs.getInt("id_plan"),
                    rs.getString("nombre_plan"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plan;
    }
    
    public List<Plan> obtenerPlanes() throws SQLException {
    String query = "SELECT * FROM planes";
    List<Plan> planes = new ArrayList<>();
    try (Connection conn = CConection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Plan plan = new Plan(
                rs.getInt("id_plan"),
                rs.getString("nombre_plan"),
                rs.getString("descripcion"),
                rs.getDouble("precio")
            );
            planes.add(plan);
        }
    }
    return planes;
}

    // Modificar Plan
    public boolean modificarPlan(int idPlan, String nombrePlan, String descripcion, double precio) {
        String query = "UPDATE planes SET nombre_plan = ?, descripcion = ?, precio = ? WHERE id_plan = ?";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nombrePlan);
            pstmt.setString(2, descripcion);
            pstmt.setDouble(3, precio);
            pstmt.setInt(4, idPlan);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar Plan
    public boolean eliminarPlan(int idPlan) {
        String query = "DELETE FROM planes WHERE id_plan = ?";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idPlan);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Buscar Todos los Planes
    public List<Plan> buscarTodosPlanes() {
        String query = "SELECT * FROM planes";
        List<Plan> planes = new ArrayList<>();
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Plan plan = new Plan(
                    rs.getInt("id_plan"),
                    rs.getString("nombre_plan"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio")
                );
                planes.add(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planes;
    }
}
