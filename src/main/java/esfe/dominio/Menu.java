package esfe.dominio;

public class Menu {
    private int codpro;          // Clave primaria, consistente con DAO
    private String nompro;       // Nombre del producto
    private Float precio;        // Precio (puede ser null)
    private String categoria;    // Categoría
    private String tipo;         // Tipo

    public Menu() {
        // Constructor por defecto
    }

    public Menu(int codpro, String nompro, Float precio, String categoria, String tipo) {
        this.codpro = codpro;
        this.nompro = nompro;
        this.precio = precio;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getCodpro() {
        return codpro;
    }

    public void setCodpro(int codpro) {
        this.codpro = codpro;
    }

    public String getNompro() {
        return nompro;
    }

    public void setNompro(String nompro) {
        this.nompro = nompro;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
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

    @Override
    public String toString() {
        return "Menu{" +
                "codpro=" + codpro +
                ", nompro='" + nompro + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
