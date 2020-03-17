package entities;

import javax.persistence.*;
import java.text.NumberFormat;

@Entity
@Table(name = "PRODUCTOS")

public class Producto extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @Column(name = "CODIGO_BARRAS", unique = true, nullable = false, length = 16)
    private String codigoBarras;

    @Column(name="NOMBRE", nullable = false, length = 120)
    private String nombre;

    @Column(name="MARCA", nullable = false, length = 30)
    private String marca;

    @Column(name="CATEGORIA", nullable = false, length = 30)
    private String categoria;

    @Column(name="DESCRIPCION")
    private String descripcion;

    @Column(name="PRECIO_VENTA", nullable = false)
    private float precioVenta;

    @Column(name="CANTIDAD")
    private Integer cantidad;

    public Producto() {
        super();
    }

    public Producto(String codigoBarras,
                    String nombre,
                    String marca,
                    String categoria,
                    String descripcion,
                    float precioVenta) {
        super();
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
    }

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

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precioVenta;
    }

    public void setPrecio(float precio) {
        this.precioVenta = precio;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * ------------------------------------------------------------------------
     *   Devolución formateada
     * ------------------------------------------------------------------------
     */

    @Override
    public String toString() {
        return nombre + ", " + marca.toUpperCase() + ", código: " + codigoBarras;
    }

    public String precioEnEuros() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(precioVenta);
    }

    public String detalles() {
        StringBuffer string = new StringBuffer(
            "C\u00f3digo de barras:  " + codigoBarras + "\n" +
            "Nombre:            " + nombre + "\n" +
            "Marca:             " + marca + "\n" +
            "Categor\u00eda:         " + categoria + "\n" +
            "Precio de Venta:   " + precioEnEuros() + "\n" +
            "Uds en Almac\u00e9n:    " + cantidad
        );

        if (!descripcion.isEmpty()) string.append("\n\nDescripci\u00f3n:\n" + descripcion);

        return String.valueOf(string);
    }
}
