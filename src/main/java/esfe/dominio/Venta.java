package esfe.dominio; // Manteniendo el mismo paquete de tu ejemplo

import java.time.LocalDateTime; // Para mapear el tipo DATETIME de SQL Server

public class Venta {
    private int idVenta;
    private int nomProducto; // Aunque "nomProduct" es int en la DB, podría ser un ID de producto
    private double precioProducto;
    private String nombreCliente;
    private String estado; // nchar(10) en SQL Server
    private LocalDateTime fecha; // datetime en SQL Server

    // Constructor vacío (necesario para algunos frameworks como JPA/Hibernate)
    public Venta() {
    }

    // Constructor con todos los campos (excepto idVenta, que es IDENTITY)
    public Venta(int nomProducto, double precioProducto, String nombreCliente, String estado, LocalDateTime fecha) {
        this.nomProducto = nomProducto;
        this.precioProducto = precioProducto;
        this.nombreCliente = nombreCliente;
        this.estado = estado;
        this.fecha = fecha;
    }

    // --- Getters ---
    public int getIdVenta() {
        return idVenta;
    }

    public int getNomProducto() {
        return nomProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    // --- Setters ---
    // El setter para idVenta es útil si se asigna después de la inserción en la DB
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public void setNomProducto(int nomProducto) {
        this.nomProducto = nomProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setEstado(String estado) {
        // Opcional: Aquí podrías añadir alguna validación para el estado
        this.estado = estado;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    // Opcional: Método para obtener una representación legible del estado
    // Si tu campo 'estado' en la DB almacena valores como "Pendiente", "Enviado", etc.,
    // entonces un método como este podría no ser tan necesario como en el ejemplo de 'byte status'
    // que se convierte a String. Sin embargo, lo mantengo como ejemplo de lógica adicional.
    public String getStrEstado(){
        if (estado != null) {
            // Suponiendo que 'estado' podría ser "PENDIENTE ", "ENVIADO   ", etc. debido a nchar(10)
            // Usamos trim() para eliminar espacios en blanco al final.
            String trimmedEstado = estado.trim().toUpperCase();
            switch (trimmedEstado) {
                case "PENDIENTE":
                    return "Pendiente de envío";
                case "ENVIADO":
                    return "Producto enviado";
                case "ENTREGADO":
                    return "Producto entregado";
                default:
                    return trimmedEstado; // Devuelve el estado tal cual si no hay mapeo específico
            }
        }
        return "Desconocido";
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", nomProducto=" + nomProducto +
                ", precioProducto=" + precioProducto +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", estado='" + estado + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
