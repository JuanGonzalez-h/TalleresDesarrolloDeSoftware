public class Cliente {
    private final String id;
    private String nombre;
    private String documento;

    public Cliente(String id, String nombre, String documento) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nombre=" + nombre + ", documento=" + documento + "}";
    }
}
