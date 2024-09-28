package Entidades;

import java.util.Date;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String dni;
    private String domicilio;
    private String telefono;
    private String zona;
    private int idPlan;
    private Date UltimoPeriodoPago;  // Cambiar a tipo Date

    public Cliente(int idCliente, String nombre, String apellido, String dni, String domicilio, String telefono, String zona, int idPlan, Date periodoPago) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.zona = zona;
        this.idPlan = idPlan;
        this.UltimoPeriodoPago = UltimoPeriodoPago;  // Inicializar nuevo campo
    }

    // Getters y setters para todos los atributos

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public Date getUltimoPeriodoPago() {
        return UltimoPeriodoPago;
    }

    public void setUltimoPeriodoPago(Date UltimoPeriodoPago) {
        this.UltimoPeriodoPago = UltimoPeriodoPago;
    }

    
}
