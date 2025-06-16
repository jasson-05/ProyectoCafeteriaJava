package esfe.dominio;

/**
 * Clase que representa un empleado en el sistema de la cafetería.
 * Contiene atributos como ID, nombre, posición, salario, número de teléfono y dirección/email.
 */
public class Employee {
    private int id; // Identificador único del empleado
    private String name; // Nombre del empleado
    private String position; // Cargo o posición del empleado
    private double salary; // Salario del empleado
    private String phoneNumber; // Número de teléfono del empleado
    private String addressEmployees; // Dirección o email del empleado (usado como email en este contexto)

    // Constructor vacío (necesario para algunas operaciones)
    public Employee() {
    }

    /**
     * Constructor para crear un nuevo empleado sin un ID (se asignará automáticamente).
     * @param name Nombre del empleado.
     * @param position Posición/cargo del empleado.
     * @param salary Salario del empleado.
     * @param phoneNumber Número de teléfono del empleado.
     * @param addressEmployees Dirección o email del empleado.
     */
    public Employee(String name, String position, double salary, String phoneNumber, String addressEmployees) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.addressEmployees = addressEmployees;
    }

    /**
     * Constructor para actualizar o eliminar un empleado existente con un ID.
     * @param id ID único del empleado.
     * @param name Nombre del empleado.
     * @param position Posición/cargo del empleado.
     * @param salary Salario del empleado.
     * @param phoneNumber Número de teléfono del empleado.
     * @param addressEmployees Dirección o email del empleado.
     */
    public Employee(int id, String name, String position, double salary, String phoneNumber, String addressEmployees) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.addressEmployees = addressEmployees;
    }

    // --- Getters y Setters ---

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
