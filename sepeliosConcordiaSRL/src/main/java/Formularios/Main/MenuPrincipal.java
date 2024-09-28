package Formularios.Main;

import Formularios.Cliente.BuscarCliente;
import Formularios.Cliente.CrearCliente;
import Formularios.Cobrador.BuscarCobrador;
import Formularios.Cobrador.CargarCobrador;
import Formularios.Plan.BuscarPlan;
import Formularios.Plan.CargarPlan;
import Formularios.Zona.CargarZona;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jonathan
 */
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Casa de Sepelios - Sistema de Gestión");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JMenuBar menuBar = new JMenuBar();

        // Menú de Clientes
        JMenu menuClientes = new JMenu("Clientes");
        JMenuItem itemCargarCliente = new JMenuItem("Cargar Cliente");
        JMenuItem itemBuscarCliente = new JMenuItem("Buscar Cliente");

        itemCargarCliente.addActionListener(e -> new CrearCliente());
        itemBuscarCliente.addActionListener(e -> new BuscarCliente());

        menuClientes.add(itemCargarCliente);
        menuClientes.add(itemBuscarCliente);
        menuBar.add(menuClientes);

        // Menú de Planes
        JMenu menuPlanes = new JMenu("Planes");
        JMenuItem itemCargarPlan = new JMenuItem("Cargar Plan");
        JMenuItem itemBuscarPlan = new JMenuItem("Buscar Plan");

        itemCargarPlan.addActionListener(e -> new CargarPlan());
        itemBuscarPlan.addActionListener(e -> new BuscarPlan());

        menuPlanes.add(itemCargarPlan);
        menuPlanes.add(itemBuscarPlan);
        menuBar.add(menuPlanes);

        // Menú de Cobradores
        JMenu menuCobradores = new JMenu("Cobradores");
        JMenuItem itemCargarCobrador = new JMenuItem("Cargar Cobrador");
        JMenuItem itemBuscarCobrador = new JMenuItem("Buscar Cobrador");
        itemCargarCobrador.addActionListener(e -> new CargarCobrador());
        itemBuscarCobrador.addActionListener(e -> new BuscarCobrador());

        menuCobradores.add(itemCargarCobrador);
        menuCobradores.add(itemBuscarCobrador);
        menuBar.add(menuCobradores);

        JMenu menuZona = new JMenu("Zonas");
        JMenuItem itemCargarZona = new JMenuItem("Cargar Zona");
        itemCargarZona.addActionListener(e -> new CargarZona());
        menuZona.add(itemCargarZona);
        menuBar.add(menuZona);

        setJMenuBar(menuBar);
    }

}
