package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.NumberFormat;

@Entity
@Table(name = "FACTURA_PRODUCTOS")

public class FacturaProductos extends CommonEntity {

    @Embeddable
    public static class Id implements Serializable {
        @ManyToOne
        @JoinColumn(name = "idFactura", insertable = false, updatable = false)
        private Factura factura;

        @ManyToOne
        @JoinColumn(name = "idProducto", insertable = false, updatable = false)
        private Producto producto;

        public Id() {}

        public Id(Factura factura, Producto producto) {
            this.factura = factura;
            this.producto = producto;
        }

        public boolean equals(Object object) {
            if (object != null && object instanceof Id) {
                Id that = (Id)object;
                return
                    this.factura.equals(that.factura) &&
                    this.producto.equals(that.producto)
                ;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return factura.hashCode() + producto.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIO_OFERTADO")
    private float precioOfertado;

    public FacturaProductos() {}

    public FacturaProductos(Factura factura,
                            Producto producto,
                            Integer cantidad,
                            float precioOfertado) {
        this.id.factura = factura;
        this.id.producto = producto;

        this.cantidad = cantidad;
        this.precioOfertado = precioOfertado;
    }

    /**
     * ------------------------------------------------------------------------
     *   Getters y Setters
     * ------------------------------------------------------------------------
     */

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) { }

    public Factura getFactura() {
        return id.factura;
    }

    public void setFactura(Factura factura) {
        this.id.factura = factura;
    }

    public Producto getProducto() {
        return id.producto;
    }

    public void setProducto(Producto producto) {
        this.id.producto = producto;
    }

    public float getPrecioOfertado() {
        return precioOfertado;
    }

    public void setPrecioOfertado(float precioOfertado) {
        this.precioOfertado = precioOfertado;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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

    public String precioEnEuros() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(precioOfertado);
    }
}
