package Entidades;

import java.util.ArrayList;
import java.util.List;

public class Cobrador {
    private int idCobrador;
    private String nombre;
    private String apellido;
    private String domicilio;
    private String telefono;
    private List<Integer> zonas;  // Lista de IDs de zonas asignadas al cobrador

    public Cobrador() {
        zonas = new ArrayList<>();  // Inicializamos la lista de zonas
    }

    public int getIdCobrador() {
        return idCobrador;
    }

    public void setIdCobrador(int idCobrador) {
        this.idCobrador = idCobrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<Integer> getZonas() {
        return zonas;
    }

    public void setZonas(List<Integer> zonas) {
        this.zonas = zonas;
    }

    // Método para agregar una zona
    public void addZona(int zonaId) {
        this.zonas.add(zonaId);
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
