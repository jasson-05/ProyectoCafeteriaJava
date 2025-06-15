package esfe.dominio;

public class Menu {
    private int codpro;          // Clave primaria, consistente con DAO
    private String nompro;       // Nombre del producto
    private float precio;        // Precio (cambiado a 'float' primitivo para evitar nulls inesperados)
    private String categoria;    // Categoría
    private String tipo;         // Tipo

    /**
     * Constructor por defecto. Útil para instanciar objetos vacíos que serán llenados
     * posteriormente, por ejemplo, al recuperar datos de la base de datos.
     */
    public Menu() {
        // Inicializar con valores predeterminados seguros si es necesario
        this.codpro = 0; // O un valor que indique "no asignado"
        this.nompro = "";
        this.precio = 0.0f;
        this.categoria = "";
        this.tipo = "";
    }

    /**
     * Constructor para crear un nuevo objeto Menu sin un 'codpro' (ya que la base de datos
     * lo generará automáticamente si es una clave primaria autoincremental).
     * Este es el constructor que usarás al crear un nuevo producto desde la interfaz de usuario.
     *
     * @param nompro El nombre del producto.
     * @param precio El precio del producto.
     * @param categoria La categoría del producto.
     * @param tipo El tipo de producto (e.g., bebida, comida, postre).
     */
    public Menu(String nompro, float precio, String categoria, String tipo) {
        // No se asigna codpro aquí, se asume que la DB lo manejará
        this.nompro = nompro;
        this.precio = precio;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    /**
     * Constructor completo para crear o reconstruir un objeto Menu con un 'codpro' existente.
     * Este es el constructor que usarás al leer datos de la base de datos.
     *
     * @param codpro El código o ID único del producto.
     * @param nompro El nombre del producto.
     * @param precio El precio del producto.
     * @param categoria La categoría del producto.
     * @param tipo El tipo de producto.
     */
    public Menu(int codpro, String nompro, float precio, String categoria, String tipo) {
        this.codpro = codpro;
        this.nompro = nompro;
        this.precio = precio;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    // --- Getters y Setters ---

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
        // Añadir una pequeña validación: evitar nulls
        this.nompro = (nompro != null) ? nompro : "";
    }

    public float getPrecio() { // Cambiado a 'float' primitivo
        return precio;
    }

    public void setPrecio(float precio) { // Cambiado a 'float' primitivo
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = (categoria != null) ? categoria : "";
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = (tipo != null) ? tipo : "";
    }

    // --- Método toString (útil para depuración) ---

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