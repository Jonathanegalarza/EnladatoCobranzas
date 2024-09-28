package com.mycompany.sepeliosconcordiasrl;

import Formularios.Main.MenuPrincipal;
import javax.swing.SwingUtilities;

public class SepeliosConcordiaSRL {
    public static void main(String[] args) {
        // Inicia la base de datos
        try {
            System.out.println("Iniciando la base de datos...");
            Configuracion.CConection.initDatabase();
            System.out.println("Base de datos inicializada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
            return; // Si falla la inicialización, no continuar
        }

        // Inicia la interfaz gráfica si la base de datos fue inicializada correctamente
        SwingUtilities.invokeLater(() -> {
            System.out.println("Iniciando la interfaz gráfica...");
            new MenuPrincipal().setVisible(true);
        });
    }
}
