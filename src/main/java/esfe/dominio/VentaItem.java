package esfe.dominio;

import java.time.LocalDateTime;

public class VentaItem {
    private int idventa; // Clave primaria, auto-generada por la base de datos
    private int nomProduct; // ID del producto (FK a menuss.id_codpro)
    private double precioProduct; // Precio del producto en el momento de la venta
    private String nombreClient;
    private String estado;
    private LocalDateTime fecha;

    // Constructor vacío
    public VentaItem() {
    }

    // Constructor con campos para la inserción (sin idventa, ya que es auto-generado)
    public VentaItem(int nomProduct, double precioProduct, String nombreClient, String estado, LocalDateTime fecha) {
        this.nomProduct = nomProduct;
        this.precioProduct = precioProduct;
        this.nombreClient = nombreClient;
        this.estado = estado;
        this.fecha = fecha;
    }

    // --- Getters y Setters ---
    public int getIdventa() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }

    public int getNomProduct() {
        return nomProduct;
    }

    public void setNomProduct(int nomProduct) {
        this.nomProduct = nomProduct;
    }

    public double getPrecioProduct() {
        return precioProduct;
    }

    public void setPrecioProduct(double precioProduct) {
        this.precioProduct = precioProduct;
    }

    public String getNombreClient() {
        return nombreClient;
    }

    public void setNombreClient(String nombreClient) {
        this.nombreClient = nombreClient;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

