package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.NumberFormat;

@Entity
@Table(name = "RECIBO_PRODUCTOS")

public class ReciboProductos extends CommonEntity {

    @Embeddable
    public static class Id implements Serializable {
        @ManyToOne
        @JoinColumn(name = "idRecibo", insertable = false, updatable = false)
        private Recibo recibo;

        @ManyToOne
        @JoinColumn(name = "idProducto", insertable = false, updatable = false)
        private Producto producto;

        public Id() {}

        public Id(Recibo recibo, Producto producto) {
            this.recibo = recibo;
            this.producto = producto;
        }

        public boolean equals(Object object) {
            if (object != null && object instanceof Id) {
                Id that = (Id)object;
                return
                    this.recibo.equals(that.recibo) &&
                    this.producto.equals(that.producto)
                ;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return recibo.hashCode() + producto.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIO_OFERTADO", nullable = false)
    private float precioOfertado;

    public ReciboProductos() {}

    public ReciboProductos(Recibo recibo,
                           Producto producto,
                           Integer cantidad,
                           float precioOfertado) {
        this.id.recibo = recibo;
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

    public Recibo getRecibo() {
        return id.recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.id.recibo = recibo;
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
