package stories.facturas;

import daos.ClienteDao;
import daos.FacturaDao;
import daos.FacturaProductosDao;
import daos.ProductoDao;
import entities.Cliente;
import entities.Factura;
import entities.FacturaProductos;
import entities.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import utils.Alerta;
import utils.ProductoEnFactura;
import utils.Validar;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class FacturasFacadeImpl implements FacturasFacade {

    private static final Logger logger = LogManager.getLogger(FacturasFacadeImpl.class);

    /**
     * Devuelve una lista de todos los clientes
     * @return ObservableList
     */
    @Override
    public ObservableList<Cliente> obtenerListaClientes() {
        List<Cliente> clientes = ClienteDao.getInstance().obtenerClientes();
        return FXCollections.observableArrayList(clientes);
    }

    /**
     * Devuelve una lista de todos los productos
     * @return ObservableList
     */
    @Override
    public ObservableList<Producto> obtenerListaProductos() {
        List<Producto> productos = ProductoDao.getInstance().obtenerProductos();
        return FXCollections.observableArrayList(productos);
    }

    /**
     * Devuelve el número de la siguiente factura
     * @return String
     */
    @Override
    public String obtenerNumeroSiguienteFactura() {
        String numeroSiguienteFactura;

        List<Factura> ultimaFactura = FacturaDao.getInstance().obtenerFacturas();

        if (ultimaFactura.size() == 0) {
            return "F00001";
        }

        String numeroUltimaFactura = ultimaFactura.get(ultimaFactura.size() - 1).getNumeroFactura();
        Integer siguienteNumero = Integer.valueOf(numeroUltimaFactura.replace("F", "")) + 1;
        numeroSiguienteFactura = "F" + String.format("%05d", siguienteNumero);

        return numeroSiguienteFactura;
    }

    /**
     * Inserta la factura en la base de datos
     * @param numeroFactura
     * @param cliente
     * @param productosEnFactura
     * @param totalFactura
     * @throws SQLException
     */
    @Override
    public void emitirFactura(String numeroFactura,
                              Cliente cliente,
                              ObservableList<ProductoEnFactura> productosEnFactura,
                              float totalFactura) throws SQLException {
        Factura factura = new Factura();

        factura.setNumeroFactura(numeroFactura);
        factura.setIdCliente(cliente);
        factura.setImporte(totalFactura);
        factura.setFecha(new Date());

        Factura facturaInsertada = FacturaDao.getInstance().insertarFactura(factura);
        if (facturaInsertada == null) {
            new Alerta("ERROR", "Error en la emisi\u00f3n de factura.", "No se ha podido crear la factura en la base de datos.");
            return;
        }

        // Recogida de la factura añadida a la base de datos
        List<Factura> facturaBuscadaPorNumero = FacturaDao.getInstance().buscarFacturaPorNumero(numeroFactura);
        if (facturaBuscadaPorNumero.isEmpty()) {
            new Alerta("ERROR", "Error en la emisi\u00f3n de factura.", "No se ha podido obtener la factura desde la base de datos.");
            return;
        }

        // Obtención de la factura desde la base de datos
        Factura facturaBuscada = facturaBuscadaPorNumero.get(0);

        // Inserción de los productos a la tabla de la relación asociativa
        for (ProductoEnFactura productoEnFactura: productosEnFactura) {
            Producto productoFacturado = ProductoDao.getInstance().get(productoEnFactura.getId());
            if (productoFacturado == null) {
                new Alerta("ERROR", "Error en la emisi\u00f3n de factura.", "No se ha podido obtener un producto de la base de datos.");
                return;
            }

            FacturaProductos facturaProductos = new FacturaProductos(facturaBuscada,
                                                                     productoFacturado,
                                                                     productoEnFactura.getUnidades(),
                                                                     productoFacturado.getPrecio());

            FacturaProductos fp = FacturaProductosDao.getInstance().insertarProductosEnFactura(facturaProductos);

            if (fp == null || productoFacturado.getCantidad() < productoEnFactura.getUnidades()) {
                new Alerta("ERROR",
                        "Error en la emisi\u00f3n de factura.",
                        "No se ha podido registrar el producto" + productoFacturado.getNombre() + "en la factura.");
                return;
            }

            // Se retiran la cantidad de productos facturados del almacén
            Integer productosRestantes = productoFacturado.getCantidad() - productoEnFactura.getUnidades();
            productoFacturado.setCantidad(productosRestantes);
        }

        new Alerta("INFORMATION", "Emisi\u00f3n de factura.", "Factura emitida correctamente.");
    }

    /**
     * Imprime la factura en un PDF utilizando una plantilla de empresa
     * @param numeroFactura
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void imprimirFactura(String numeroFactura) throws IOException, SQLException {

        // Obtención de parámetros
        List<Factura> listaFactura = FacturaDao.getInstance().buscarFacturaPorNumero(numeroFactura);
        Factura factura = listaFactura.get(0);
        float importeTotal = factura.getImporte();
        Cliente cliente = factura.getIdCliente();
        List<FacturaProductos> productosDeFactura = FacturaProductosDao.getInstance().obtenerProductosDeFactura(factura);
        String formTemplate = "src/main/resources/views/templates/factura.pdf";

        try (PDDocument pdfDocument = PDDocument.load(new File(formTemplate))) {

            // Recoger el catálogo de campos de formulario
            PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

            // Comprobación en caso de que no hubiese formulario
            if (acroForm != null) {

                // Recogida de campos de la plantilla
                PDTextField numeroFacturaField = (PDTextField) acroForm.getField("numeroFactura");
                PDTextField clienteField = (PDTextField) acroForm.getField("cliente");
                PDTextField direccionField = (PDTextField) acroForm.getField("direccion");
                PDTextField movilField = (PDTextField) acroForm.getField("movil");
                PDTextField telefonoFijoField = (PDTextField) acroForm.getField("telefonoFijo");
                PDTextField correoElectronicoField = (PDTextField) acroForm.getField("correoElectronico");
                PDTextField tablaProductosField = (PDTextField) acroForm.getField("tablaProductos");
                PDTextField baseImponibleField = (PDTextField) acroForm.getField("baseImponible");
                PDTextField impuestosBaseField = (PDTextField) acroForm.getField("impuestosBase");
                PDTextField totalField = (PDTextField) acroForm.getField("total");

                //Formateo de impresión
                NumberFormat nf = NumberFormat.getCurrencyInstance();

                String nombreCliente = "";
                String direccionCliente = "";
                String movilCliente = "";
                String telefonoCliente = "";
                String correoCliente = "";
                StringBuffer productosEnTabla = new StringBuffer();

                if (cliente != null) {
                    nombreCliente = cliente.getNombre() + " " + cliente.getApellidos();
                    direccionCliente = cliente.getDireccion() + " " + cliente.getNumeroDireccion() + ", " + cliente.getMunicipio();
                    movilCliente = cliente.getMovil();
                    telefonoCliente = cliente.getTelefono();
                    correoCliente = cliente.getEmail();
                }

                for (FacturaProductos productosEnFactura: productosDeFactura) {
                    Producto producto = productosEnFactura.getProducto();
                    String nombreProducto = String.format("%-74s", producto.getMarca() + ", " + producto.getNombre());
                    String udsProducto = String.format("%7s", productosEnFactura.getCantidad() + " uds");
                    String precioProducto = String.format("%12s", nf.format(productosEnFactura.getPrecioOfertado()));
                    productosEnTabla.append(nombreProducto + udsProducto + precioProducto + "\n");
                }

                String productosEnFactura = String.valueOf(productosEnTabla);

                String baseImponibleEnEuros = nf.format(importeTotal / 1.07f) + " ";
                String impuestosEnEuros = nf.format(importeTotal * 0.07f / 1.07f) + " ";
                String totalEnEuros = nf.format(importeTotal) + " ";

                // Introducción de datos en la plantilla
                numeroFacturaField.setValue(numeroFactura);
                clienteField.setValue(nombreCliente);
                direccionField.setValue(direccionCliente);
                movilField.setValue(movilCliente);
                telefonoFijoField.setValue(telefonoCliente);
                correoElectronicoField.setValue(correoCliente);
                tablaProductosField.setValue(productosEnFactura);
                baseImponibleField.setValue(baseImponibleEnEuros + " ");
                impuestosBaseField.setValue(impuestosEnEuros + " ");
                totalField.setValue(totalEnEuros + " ");
            }

            // Guardar el documento PDF
            pdfDocument.save("target/" + numeroFactura + ".pdf");
        }
    }

    /**
     * Devuelve una lista de facturas buscadas por su número o por su cliente
     * @param numeroFacturaBuscado
     * @param clienteBuscado
     * @return ObservableList
     */
    @Override
    public ObservableList<Factura> buscarFactura(String numeroFacturaBuscado, Cliente clienteBuscado) {
        List<Factura> facturasEncontradas = null;

        if (StringUtils.isNotBlank(numeroFacturaBuscado)) {

            facturasEncontradas = FacturaDao.getInstance().buscarFacturaPorNumero(numeroFacturaBuscado);
        }

        if (clienteBuscado != null) {
            facturasEncontradas = FacturaDao.getInstance().buscarFacturasPorCliente(clienteBuscado);
        }

        return FXCollections.observableArrayList(facturasEncontradas);
    }

    /**
     * Muestra información de la factura en la caja de texto
     * @param factura
     * @return String
     */
    @Override
    public String detallesFactura(Factura factura) {
        StringBuffer detalleFactura = new StringBuffer(
                "Factura Nº:           " + factura.getNumeroFactura() + "\n" +
                "Fecha de facturaci\u00f3n: " + factura.getFecha() + "\n"
        );

        if (factura.getIdCliente() != null) {
            Cliente cliente = factura.getIdCliente();
            detalleFactura.append(
                "Cliente:              " + cliente.getApellidos() + ", " + cliente.getNombre() + ", " + cliente.getnumeroIdentificacion() + "\n"
            );
        }

        List<FacturaProductos> facturaProductos = FacturaProductosDao.getInstance().obtenerProductosDeFactura(factura);

        if (facturaProductos == null) {
            new Alerta("ERROR", "Error en la obtenci\u00f3n de datos", "No se ha podido obtener los productos de la factura.");
            return "";
        }

        detalleFactura.append("--------------------------------------------------------------------------------\n");

        for (FacturaProductos fp: facturaProductos) {
            Producto producto = fp.getProducto();
            detalleFactura.append(
                String.format("%-54s", producto.getNombre()) +
                String.format("%7s", fp.getCantidad()) + " uds" +
                String.format("%10s", fp.getPrecioOfertado()) + " €/ud" + "\n"
            );
        }

        detalleFactura.append("--------------------------------------------------------------------------------\n");

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        float total = factura.getImporte();

        String baseImponibleEnEuros = nf.format(total / 1.07f) + " ";
        String impuestosEnEuros = nf.format(total * 0.07f / 1.07f) + " ";
        String totalEnEuros = nf.format(total) + " ";

        detalleFactura.append(
                "Base imponible:       " + String.format("%10s", baseImponibleEnEuros) + "\n" +
                "Impuestos:            " + String.format("%10s", impuestosEnEuros) + "\n" +
                "Importe total:        " + String.format("%10s", totalEnEuros) + "\n"
        );

        return String.valueOf(detalleFactura);
    }
}
