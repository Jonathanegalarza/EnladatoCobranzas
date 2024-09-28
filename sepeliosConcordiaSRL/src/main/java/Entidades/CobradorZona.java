
package Entidades;

/**
 *
 * @author Jonathan
 */
public class CobradorZona {
    
    private int idCobrador;
    private int idZona;

    // Constructor
    public CobradorZona(int idCobrador, int idZona) {
        this.idCobrador = idCobrador;
        this.idZona = idZona;
    }

    // Getters y Setters
    public int getIdCobrador() { return idCobrador; }
    public void setIdCobrador(int idCobrador) { this.idCobrador = idCobrador; }

    public int getIdZona() { return idZona; }
    public void setIdZona(int idZona) { this.idZona = idZona; }
}

