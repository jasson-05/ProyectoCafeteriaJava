package esfe.dominio;
import java.util.Objects;

/**
 * Clase de dominio que representa un empleado.
 * Esta clase mapea a la tabla 'Employees' en la base de datos SQL Server.
 */
public class Employee {

    // Atributos que corresponden a las columnas de la tabla Employees
    private int id;
    private String name;
    private String position;
    private double salary;
    private String phoneNumber;
    private String addressEmployees; // Cambiado de 'addressemployess' para reflejar el nombre de la columna más precisamente

    /**
     * Constructor por defecto.
     * Necesario para algunas operaciones de frameworks como ORM.
     */
    public Employee() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param id El ID único del empleado.
     * @param name El nombre completo del empleado.
     * @param position El puesto de trabajo del empleado.
     * @param salary El salario del empleado.
     * @param phoneNumber El número de teléfono del empleado.
     * @param addressEmployees La dirección del empleado.
     */
    public Employee(int id, String name, String position, double salary, String phoneNumber, String addressEmployees) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.addressEmployees = addressEmployees;
    }

    /**
     * Constructor sin el ID, útil para crear nuevos empleados
     * antes de que la base de datos asigne un ID (IDENTITY).
     *
     * @param name El nombre completo del empleado.
     * @param position El puesto de trabajo del empleado.
     * @param salary El salario del empleado.
     * @param phoneNumber El número de teléfono del empleado.
     * @param addressEmployees La dirección del empleado.
     */
    public Employee(String name, String position, double salary, String phoneNumber, String addressEmployees) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.addressEmployees = addressEmployees;
    }

    // --- Getters y Setters para todos los atributos ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressEmployees() {
        return addressEmployees;
    }

    public void setAddressEmployees(String addressEmployees) {
        this.addressEmployees = addressEmployees;
    }

    // --- Métodos hashCode, equals y toString para una buena práctica ---

    /**
     * Genera un valor hash para el objeto Employee.
     * Basado principalmente en el ID del empleado, ya que es la clave primaria.
     * @return El valor hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id); // Solo el ID es suficiente para un hash único
    }

    /**
     * Compara este objeto Employee con otro.
     * Dos empleados se consideran iguales si tienen el mismo ID.
     * @param obj El objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id == employee.id;
    }

    /**
     * Proporciona una representación de cadena del objeto Employee.
     * Útil para depuración y logging.
     * @return Una cadena que representa el objeto Employee.
     */
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", addressEmployees='" + addressEmployees + '\'' +
                '}';
    }
}
