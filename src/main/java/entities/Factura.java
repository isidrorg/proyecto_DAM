package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FACTURAS")

public class Factura extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName="ID")
    private Cliente idCliente;

    @Column(name = "NUMERO_FACTURA", nullable = false)
    private String numeroFactura;

    @Column(name = "FECHA", nullable = false)
    private Date fecha = new Date();

    @Column(name = "IMPORTE", nullable = false)
    private float importe;

    /**
     * ------------------------------------------------------------------------
     *   Getters y Setters
     * ------------------------------------------------------------------------
     */
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {}

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    /**
     * ------------------------------------------------------------------------
     *   Devolución formateada
     * ------------------------------------------------------------------------
     */

    @Override
    public String toString() {
        return null;
    }
}
