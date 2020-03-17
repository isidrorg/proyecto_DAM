package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USUARIOS")

public class Usuario extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @Column(name = "NUMERO_IDENTIFICACION", unique = true, nullable = false, length = 10)
    private String numeroIdentificacion;

    @Column(name = "NOMBRE", nullable = false, length = 30)
    private String nombre;

    @Column(name = "APELLIDOS", nullable = false, length = 60)
    private String apellidos;

    @Column(name = "DIRECCION", nullable = false)
    private String direccion;

    @Column(name = "NUMERO_DIRECCION")
    private Integer numeroDireccion;

    @Column(name = "PISO")
    private Integer piso;

    @Column(name = "PUERTA", length = 10)
    private String puerta;

    @Column(name = "MUNICIPIO", nullable = false)
    private String municipio;

    @Column(name = "PROVINCIA", nullable = false, length = 60)
    private String provincia;

    @Column(name = "CODIGO_POSTAL", nullable = false)
    private Integer codigoPostal;

    @Column(name = "MOVIL", nullable = false, length = 20)
    private String movil;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "CUENTA_BANCARIA", nullable = false, length = 24)
    private String cuentaBancaria;

    @Column(name = "FECHA_ALTA", length = 30)
    private Date fecha = new Date();

    @Column(name = "USUARIO", length = 30)
    private String usuario;

    @Column(name = "CONTRASENYA", length = 30)
    private String contrasenya;

    @Column(name = "SESION")
    private boolean sesion;

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

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public boolean getSesion() {
        return sesion;
    }

    public void setSesion(boolean sesion) {
        this.sesion = sesion;
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
