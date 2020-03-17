package utils;

public class ProductoEnFactura {
    private Integer id;
    private String nombre;
    private String marca;
    private Integer unidades;
    private String precio;

    public ProductoEnFactura() {
        this.id = 0;
        this.nombre = "";
        this.marca = "";
        this.unidades = 0;
        this.precio = "";
    }

    public ProductoEnFactura(Integer id, String nombre, String marca, Integer unidades, String precio) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.unidades = unidades;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return String.format("%-60s %-15s %3s %12s\n", getNombre(), getMarca(), getUnidades(), getPrecio());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
