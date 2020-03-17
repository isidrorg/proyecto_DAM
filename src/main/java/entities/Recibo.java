package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RECIBOS")

public class Recibo extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PROVEEDOR", referencedColumnName="ID", nullable = false)
    private Proveedor idProveedor;

    @Column(name = "NUMERO_RECIBO", nullable = false)
    private String numeroRecibo;

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

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
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
