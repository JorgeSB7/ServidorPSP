package jorgesb.servidorpsp.Model;

/**
 *
 * @author jorog
 */
public class Operario extends Thread{
    protected int codigoOperario;
    protected String nombre;
    protected String apellidos;
    protected String login;
    protected String password;
    protected Cuenta cuenta;
    protected Cliente cliente;
    
    public Operario() {
        this(-1,"","","","");
    }

    public Operario(String nombre, String apellidos, String login, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.login = login;
        this.password = password;
    }

    public Operario(int codigoOperario, String nombre, String apellidos, String login, String password) {
        this.codigoOperario = codigoOperario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.login = login;
        this.password = password;
    }

    public Operario(int codigoOperario, String nombre, String apellidos, String login, String password, Cuenta cuenta, Cliente cliente) {
        this.codigoOperario = codigoOperario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.login = login;
        this.password = password;
        this.cuenta = cuenta;
        this.cliente = cliente;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    

    public int getCodigoOperario() {
        return codigoOperario;
    }

    public void setCodigoOperario(int codigoOperario) {
        this.codigoOperario = codigoOperario;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Operario{" + "codigoOperario=" + codigoOperario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", login=" + login + ", password=" + password + '}';
    }
    
}
