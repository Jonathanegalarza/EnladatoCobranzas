package Controladores;

import Configuracion.CConection;
import Entidades.Zona;
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
public class ZonaControlador {

    // Método para agregar una nueva zona
    public boolean agregarZona(String descripcion) {
        String query = "INSERT INTO zonas (nombre_zona) VALUES (?)";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, descripcion);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todas las zonas
    public List<Zona> obtenerZonas() {
        String query = "SELECT id_zona, nombre_zona FROM zonas";
        List<Zona> zonas = new ArrayList<>();
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int idZona = rs.getInt("id_zona");
                String nombreZona = rs.getString("nombre_zona");
                zonas.add(new Zona(idZona, nombreZona));  // Crear y añadir objeto Zona a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zonas;
    }

    // Obtener IDs de zonas a partir de nombres
    public List<Integer> obtenerIdsZonas(List<String> nombresZonas) {
        String query = "SELECT id_zona FROM zonas WHERE nombre_zona = ?";
        List<Integer> idsZonas = new ArrayList<>();
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (String nombreZona : nombresZonas) {
                pstmt.setString(1, nombreZona);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    idsZonas.add(rs.getInt("id_zona"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idsZonas;
    }

    // Método para eliminar una zona
    public boolean eliminarZona(int idZona) {
        String query = "DELETE FROM zonas WHERE id_zona = ?";
        try (Connection conn = CConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idZona);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
