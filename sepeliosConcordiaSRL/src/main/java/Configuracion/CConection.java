package Configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CConection {

    private static final String URL = "jdbc:mysql://localhost:3307/";
    private static final String DB_NAME = "casa_sepelios";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Cambia por tu contraseña

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Método para inicializar la base de datos
    public static void initDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement()) {

            // Crear base de datos si no existe
            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(createDatabase);

            // Conectar a la base de datos y crear las tablas si no existen
            try (Connection dbConnection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD); Statement dbStatement = dbConnection.createStatement()) {

                // Crear las tablas
                createTables(dbStatement);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para crear las tablas si no existen
    private static void createTables(Statement stmt) throws SQLException {
        // Crear tablas sin referencias primero
        String createPlanesTable = "CREATE TABLE IF NOT EXISTS planes (" 
                + "id_plan INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre_plan VARCHAR(255), "
                + "descripcion VARCHAR(255), "
                + "precio DECIMAL(10, 2)"
                + ");";
        stmt.execute(createPlanesTable);

        String createCobradoresTable = "CREATE TABLE IF NOT EXISTS cobradores (" 
                + "id_cobrador INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre VARCHAR(100) NOT NULL, "
                + "apellido VARCHAR(100) NOT NULL, "
                + "domicilio VARCHAR(255), "
                + "telefono VARCHAR(20)"
                + ");";
        stmt.execute(createCobradoresTable);

        String createZonasTable = "CREATE TABLE IF NOT EXISTS zonas (" 
                + "id_zona INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre_zona VARCHAR(100) NOT NULL"
                + ");";
        stmt.execute(createZonasTable);

        // Crear tablas con referencias después
        String createClientesTable = "CREATE TABLE IF NOT EXISTS clientes (" 
                + "id_cliente INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre VARCHAR(50) NOT NULL, "
                + "apellido VARCHAR(50) NOT NULL, "
                + "dni VARCHAR(15) NOT NULL, "
                + "domicilio VARCHAR(100), "
                + "telefono VARCHAR(20), "
                + "id_zona INT, "
                + "id_plan INT, "
                + "ultimo_periodo_pago DATE, "
                + "FOREIGN KEY (id_zona) REFERENCES zonas(id_zona) ON DELETE SET NULL, "
                + "FOREIGN KEY (id_plan) REFERENCES planes(id_plan) ON DELETE SET NULL"
                + ");";
        stmt.execute(createClientesTable);

        String createTalonariosTable = "CREATE TABLE IF NOT EXISTS talonarios (" 
                + "id_talonario INT AUTO_INCREMENT PRIMARY KEY, "
                + "id_cliente INT, "
                + "id_cobrador INT, "
                + "fecha DATE, "
                + "monto DECIMAL(10, 2), "
                + "mes_abonado VARCHAR(10), "
                + "FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente), "
                + "FOREIGN KEY (id_cobrador) REFERENCES cobradores(id_cobrador)"
                + ");";
        stmt.execute(createTalonariosTable);

        String createCobradorZonaTable = "CREATE TABLE IF NOT EXISTS cobrador_zona (" 
                + "id_cobrador INT, "
                + "id_zona INT, "
                + "PRIMARY KEY (id_cobrador, id_zona), "
                + "FOREIGN KEY (id_cobrador) REFERENCES cobradores(id_cobrador), "
                + "FOREIGN KEY (id_zona) REFERENCES zonas(id_zona)"
                + ");";
        stmt.execute(createCobradorZonaTable);
    }
}
