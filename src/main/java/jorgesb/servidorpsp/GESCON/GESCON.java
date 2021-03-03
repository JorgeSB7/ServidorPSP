package jorgesb.servidorpsp.GESCON;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.*;
import jorgesb.servidorpsp.DAO.DAO;
import jorgesb.servidorpsp.Model.Cliente;
import jorgesb.servidorpsp.Model.Cuenta;
import jorgesb.servidorpsp.Model.Operario;

/**
 *
 * @author jorog
 */
public class GESCON extends Thread {

    private Socket socket;
    private DAO dao;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;

    public GESCON(Socket socket, int id, DAO dao) {
        this.socket = socket;
        this.idSessio = id;
        this.dao = dao;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(GESCON.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(GESCON.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        String op = "";
        String login = "";
        String pass = "";
        Cuenta cuenta = new Cuenta();
        try {
            op = dis.readUTF();
            switch (op) {
                case "1":
                    login = dis.readUTF();
                    pass = dis.readUTF();
                    Cliente c = this.dao.getClienteByCredentials(login, pass);
                    if (c != null) {
                        dos.writeUTF("ok");
                        String op2 = dis.readUTF();
                        do {
                            switch (op2) {
                                case "1":
                                    cuenta = this.dao.getCountByClient(c.getCodigoCliente());
                                    dos.writeUTF(cuenta.toString());
                                    break;
                                case "2":
                                    cuenta = this.dao.getCountByClient(c.getCodigoCliente());
                                    String dineroRetirado = dis.readUTF();
                                    int dinero = Integer.parseInt(dineroRetirado);
                                    cuenta.setSaldo(cuenta.getSaldo() - (float) dinero);
                                    this.dao.editCuenta(cuenta);
                                    dos.writeUTF("Se ha retirado el dinero corretamente");
                                    break;
                                case "3":
                                    cuenta = this.dao.getCountByClient(c.getCodigoCliente());
                                    String dineroIngresado = dis.readUTF();
                                    int dinero2 = Integer.parseInt(dineroIngresado);
                                    cuenta.setSaldo(cuenta.getSaldo() + (float) dinero2);
                                    this.dao.editCuenta(cuenta);
                                    dos.writeUTF("Se ha ingresado el dinero corretamente");
                                    break;
                                case "0":
                                    break;
                            }
                        } while (op != "0");
                    } else {
                        dos.writeUTF("No existe el usuario con esas credenciales");
                    }
                    break;
                case "2":
                    login = dis.readUTF();
                    pass = dis.readUTF();
                    Operario o = this.dao.getOperarioByCredentials(login, pass);
                    if (o != null) {
                        dos.writeUTF("ok");
                        String op2 = dis.readUTF();
                        do {
                            switch (op2) {
                                case "1":
                                    String dni = dis.readUTF();
                                    String nombre = dis.readUTF();
                                    String apellidos = dis.readUTF();
                                    String fecha_nac = dis.readUTF();
                                    String telefono = dis.readUTF();
                                    String email = dis.readUTF();
                                    String log = dis.readUTF();
                                    String password = dis.readUTF();
                                    Cliente cl = new Cliente(nombre, apellidos, dni, log, password, Date.valueOf(fecha_nac), telefono, email);
                                    this.dao.insertCliente(cl);
                                    break;
                                case "2":
                                    List<Cliente> lcl = this.dao.getAllCliente();
                                    for (Cliente elem : lcl) {
                                        dos.writeUTF(elem.getCodigoCliente() + "|" + elem.getName() + "|" + elem.getApellidos() + "|" + elem.getTelefono() + "|" + elem.getEmail() + "|" + elem.getDni());
                                    }
                                    String idCliente = dis.readUTF();
                                    int id = Integer.parseInt(idCliente);
                                    Cliente cl2 = this.dao.getByIDCliente(id);
                                    String saldo = dis.readUTF();
                                    int dinero = Integer.parseInt(saldo);
                                    Cuenta cuent = new Cuenta(dinero, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()));
                                    int idCuenta = this.dao.insertCuenta(cuent);
                                    this.dao.insertClienC(cl2.getCodigoCliente(), idCuenta, Timestamp.valueOf(LocalDateTime.now()));
                                    break;
                                case "3":
                                    List<Cuenta> lc = this.dao.getAllCuenta();
                                    for (Cuenta elem : lc) {
                                        dos.writeUTF(elem.getCodigoCuenta() + "");
                                        dos.writeUTF("--------------------------");
                                    }
                                    String idC = dis.readUTF();
                                    int idCount = Integer.parseInt(idC);
                                    cuenta = this.dao.getByIDCuenta(idCount);
                                    dos.writeUTF(cuenta.toString());
                                    break;
                                case "4":
                                    List<Cliente> lclients = this.dao.getAllCliente();
                                    for (Cliente elem : lclients) {
                                        dos.writeUTF(elem.getCodigoCliente() + "");
                                        dos.writeUTF("---------------------------");
                                    }
                                    String idCl = dis.readUTF();
                                    int idCli = Integer.parseInt(idCl);
                                    Cliente cli = this.dao.getByIDCliente(idCli);
                                    dos.writeUTF(cli.toString());
                                    break;
                                case "5":
                                    List<Cuenta> lcu = this.dao.getAllCuenta();
                                    for (Cuenta elem : lcu) {
                                        dos.writeUTF(elem.getCodigoCuenta() + "");
                                        dos.writeUTF("--------------------------");
                                    }
                                    String idCu = dis.readUTF();
                                    int idCoun = Integer.parseInt(idCu);
                                    cuenta = this.dao.getByIDCuenta(idCoun);
                                    this.dao.removeCuenta(cuenta);
                                    break;
                                case "0":
                                    break;
                            }
                        } while (op != "0");
                    } else {
                        dos.writeUTF("No existe el usuario con esas credenciales");
                    }
                    break;
                case "0":
                    desconnectar();
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(GESCON.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
