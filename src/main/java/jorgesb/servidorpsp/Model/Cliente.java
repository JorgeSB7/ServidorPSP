package jorgesb.servidorpsp.Model;

import java.sql.Date;

/**
 *
 * @author jorog
 */
public class Cliente extends Thread{
    
    protected int codigoCliente;
    protected String nombre;
    protected String apellidos;
    protected String dni;
    protected String login;
    protected String password;
    protected Date fecha_nac;
    protected String telefono;
    protected String email;
    protected Cuenta cuenta;
    protected int op;
    protected int op4;

    public Cliente(int codigoCliente, String nombre, String apellidos, String dni, String login, String password, Date fecha_nac, String telefono, String email, Cuenta cuenta,int op, int op4) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
        this.email = email;
        this.cuenta = cuenta;
        this.op = op;
        this.op4 = op4;
    }
    public Cliente() {
         this.codigoCliente = -1;
    }

    public Cliente(String nombre, String apellidos, String dni, String login, String password, Date fecha_nac, String telefono, String email) {
        this.codigoCliente=-1;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
        this.email = email;
    }

    public Cliente(int codigoCliente, String nombre, String apellidos, String dni, String login, String password, Date fecha_nac, String telefono, String email) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
        this.email = email;
    }

    public Cliente(int codigoCliente, String nombre, String apellidos, String dni, String login, String password, Date fecha_nac, String telefono, String email, Cuenta cuenta) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
        this.email = email;
        this.cuenta = cuenta;
    }
    public Cliente(int codigoCliente, String nombre, String apellidos, String dni, String login, String password, Date fecha_nac, String telefono, String email, Cuenta cuenta,int op) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
        this.email = email;
        this.cuenta = cuenta;
        this.op=op;
    }

    public Cliente(int codigoCliente, String nombre, String apellidos, String dni, String login, String password, Date fecha_nac, String telefono, String email, int op) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.login = login;
        this.password = password;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
        this.email = email;
        this.op = op;
    }

    public int getOp4() {
        return op4;
    }

    public void setOp4(int op4) {
        this.op4 = op4;
    }

    public Cliente(int op) {
        this.op = op;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return "Cliente{" + "codigoCliente=" + codigoCliente + ", nombre=" + nombre + ", apellidos=" + apellidos + ", dni=" + dni + ", login=" + login + ", password=" + password + ", fecha_nac=" + fecha_nac + ", telefono=" + telefono + ", email=" + email +'}'+"\n";
    }


    
            
    
}
