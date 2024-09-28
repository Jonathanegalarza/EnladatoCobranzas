/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.util.Date;

/**
 *
 * @author Jonathan
 */
public class Talonarios {
    
    private int idTalonario;
    private int idCliente;
    private int idCobrador;
    private Date fecha;
    private double monto;
    private String mesAbonado;

    // Constructor
    public Talonarios(int idTalonario, int idCliente, int idCobrador, Date fecha, double monto, String mesAbonado) {
        this.idTalonario = idTalonario;
        this.idCliente = idCliente;
        this.idCobrador = idCobrador;
        this.fecha = fecha;
        this.monto = monto;
        this.mesAbonado = mesAbonado;
    }

    // Getters y Setters
    public int getIdTalonario() { return idTalonario; }
    public void setIdTalonario(int idTalonario) { this.idTalonario = idTalonario; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdCobrador() { return idCobrador; }
    public void setIdCobrador(int idCobrador) { this.idCobrador = idCobrador; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getMesAbonado() { return mesAbonado; }
    public void setMesAbonado(String mesAbonado) { this.mesAbonado = mesAbonado; }
}

