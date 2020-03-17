package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROVEEDORES")

public class Proveedor extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "idProveedor")
    private List<Recibo> recibos = new ArrayList<Recibo>();

    @Column(name = "NUMERO_IDENTIFICACION", unique = true, nullable = false, length = 9)
    private String numeroIdentificacion;

    @Column(name = "NOMBRE", nullable = false, length = 60)
    private String nombre;

    @Column(name = "APELLIDOS", length = 120)
    private String apellidos;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "NUMERO_DIRECCION")
    private Integer numeroDireccion;

    @Column(name = "PISO")
    private Integer piso;

    @Column(name = "PUERTA", length = 10)
    private String puerta;

    @Column(name = "MUNICIPIO")
    private String municipio;

    @Column(name = "PROVINCIA", length = 60)
    private String provincia;

    @Column(name = "CODIGO_POSTAL")
    private Integer codigoPostal;

    @Column(name = "MOVIL", length = 20)
    private String movil;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CUENTA_BANCARIA", length = 24)
    private String cuentaBancaria;

    @Column(name = "FECHA_ALTA")
    private Date fecha = new Date();

    public Proveedor() {}

    public Proveedor(String numeroIdentificacion,
                     String nombre,
                     String apellidos,
                     String direccion,
                     Integer numeroDireccion,
                     Integer piso,
                     String puerta,
                     String municipio,
                     String provincia,
                     Integer codigoPostal,
                     String movil,
                     String telefono,
                     String email,
                     String cuentaBancaria) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.numeroDireccion = numeroDireccion;
        this.piso = piso;
        this.puerta = puerta;
        this.municipio = municipio;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.movil = movil;
        this.telefono = telefono;
        this.email = email;
        this.cuentaBancaria = cuentaBancaria;
        this.fecha = new Date();
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getnumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setnumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public List<Recibo> getRecibos() {
        return recibos;
    }

    public void setRecibos(List<Recibo> recibos) {
        this.recibos = recibos;
    }

    public Integer getNumeroDireccion() {
        return numeroDireccion;
    }

    public void setNumeroDireccion(Integer numeroDireccion) {
        this.numeroDireccion = numeroDireccion;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    /**
     * ------------------------------------------------------------------------
     *   Devolución formateada
     * ------------------------------------------------------------------------
     */

    @Override
    public String toString() {
        return nombre + " " + apellidos + ", " + numeroIdentificacion;
    }

    public String detalles() {
        StringBuffer string = new StringBuffer("Nombre:      " );

        if (!apellidos.isEmpty()) string.append(apellidos + ", ");
        string.append(nombre + "\n");
        string.append("Nif/Cif/Nie: " + numeroIdentificacion + "\n");

        if (!direccion.isEmpty()) {
            string.append(
            "\n" +
            "Direcci\u00f3n:   " + direccion + " " + numeroDireccion + ", piso: " + piso + ", puerta: " + puerta + "\n" +
            "             " + municipio + ", " + provincia + ", CP: " + codigoPostal + "\n"
            );
        }

        if (!movil.isEmpty())          string.append("M\u00f3vil:       " + movil + "\n");
        if (!telefono.isEmpty())       string.append("Tlf. Fijo:   " + telefono + "\n");
        if (!email.isEmpty())          string.append("Email:       " + email + "\n");
        if (!cuentaBancaria.isEmpty()) string.append("Cta. banc.:  " + cuentaBancaria + "\n");
        if (fecha != null)             string.append("Fecha Alta:  " + fecha);

        return String.valueOf(string);
    }
}
