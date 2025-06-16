package esfe.dominio;

import java.util.Objects; // Para el método equals y hashCode

/**
 * Clase de modelo (POJO) para representar un producto de la tabla 'menuss'.
 * Contiene los atributos que corresponden a las columnas de la tabla.
 */
public class Producto {
    private int idCodpro;
    private String nompro;
    private double precio;
    private String categoria;
    private String tipo;

    // Constructor vacío (necesario para algunas operaciones, aunque no usado directamente aquí)
    public Producto() {
    }

    public Producto(int idCodpro, String nompro, double precio, String categoria, String tipo) {
        this.idCodpro = idCodpro;
        this.nompro = nompro;
        this.precio = precio;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    // --- Getters y Setters ---
    public int getIdCodpro() {
        return idCodpro;
    }

    public void setIdCodpro(int idCodpro) {
        this.idCodpro = idCodpro;
    }

    public String getNompro() {
        return nompro;
    }

    public void setNompro(String nompro) {
        this.nompro = nompro;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // --- Override toString() para mostrar el nombre del producto en JComboBox ---
    @Override
    public String toString() {
        return nompro + " ($" + String.format("%.2f", precio) + ")";
    }

    // --- Override equals() y hashCode() para asegurar que JComboBox maneje bien los objetos ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return idCodpro == producto.idCodpro;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCodpro);
    }
}
