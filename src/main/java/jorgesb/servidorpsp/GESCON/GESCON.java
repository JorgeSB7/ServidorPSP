package jorgesb.servidorpsp.GESCON;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        String op2 = "";
        String login = "";
        String pass = "";
        Cuenta cuenta = new Cuenta();

        try {

            do {
                op = dis.readUTF();
                switch (op) {
                    case "1":
                        login = dis.readUTF();
                        pass = dis.readUTF();
                        Cliente c = this.dao.getClienteByCredentials(login, pass);
                        if (c.getCodigoCliente() > -1) {
                            dos.writeUTF("ok");

                            do {
                                op2 = dis.readUTF();
                                System.out.println(op2);
                                switch (op2) {
                                    case "1":
                                        System.out.println("CASO 1");
                                        cuenta = this.dao.getCountByClient(c.getCodigoCliente());
                                        dos.writeUTF(cuenta.toString());
                                        break;
                                    case "2":
                                        System.out.println("CASO 2");
                                        cuenta = this.dao.getCountByClient(c.getCodigoCliente());
                                        String dineroRetirado = dis.readUTF();
                                        int dinero = Integer.parseInt(dineroRetirado);
                                        accesoCliente(1, cuenta, dinero);
                                        dos.writeUTF("Se ha retirado el dinero corretamente");
                                        break;
                                    case "3":
                                        System.out.println("CASO 3");
                                        cuenta = this.dao.getCountByClient(c.getCodigoCliente());
                                        String dineroIngresado = dis.readUTF();
                                        int dinero2 = Integer.parseInt(dineroIngresado);
                                        accesoCliente(2, cuenta, dinero2);
                                        dos.writeUTF("Se ha ingresado el dinero corretamente");
                                        break;
                                    case "0":
                                        break;
                                    default:
                                        break;
                                }
                                //op2 = dis.readUTF();
                            } while (!op2.equals("0"));
                        } else {
                            dos.writeUTF("No existe el usuario con esas credenciales");
                        }
                        break;
                    case "2":
                        login = dis.readUTF();
                        pass = dis.readUTF();
                        Operario o = this.dao.getOperarioByCredentials(login, pass);
                        if (o.getCodigoOperario() > -1) {
                            dos.writeUTF("ok");
                            op2 = dis.readUTF();
                            do {
                                switch (op2) {
                                    case "1":
                                        accesoOperario(1);
                                        dos.writeUTF("Se ha creado satisfactoriamente el cliente");
                                        break;
                                    case "2":
                                        List<Cliente> lcl = this.dao.getAllCliente();
                                        List<String> ids = new ArrayList<>();
                                        for (Cliente elem : lcl) {
                                            ids.add(elem.getCodigoCliente() + "\n");
                                        }
                                        dos.writeUTF(ids.toString());
                                        accesoOperario(2);
                                        dos.writeUTF("Se ha creado satisfactoriamente la cuenta");
                                        break;
                                    case "3":
                                        List<Cuenta> lc = this.dao.getAllCuenta();
                                        List<String> ids2 = new ArrayList<>();
                                        for (Cuenta elem : lc) {
                                            ids2.add(elem.getCodigoCuenta() + "\n");
                                        }
                                        dos.writeUTF(ids2.toString());
                                        String idC = dis.readUTF();
                                        int idCount = Integer.parseInt(idC);
                                        cuenta = this.dao.getByIDCuenta(idCount);
                                        dos.writeUTF(cuenta.toString());
                                        break;
                                    case "4":
                                        List<Cliente> lclients = this.dao.getAllCliente();
                                        List<String> ids3 = new ArrayList<>();
                                        for (Cliente elem : lclients) {
                                            ids3.add(elem.getCodigoCliente() + "\n");
                                        }
                                        dos.writeUTF(ids3.toString());
                                        String idCl = dis.readUTF();
                                        int idCli = Integer.parseInt(idCl);
                                        Cliente cli = this.dao.getByIDCliente(idCli);
                                        dos.writeUTF(cli.toString());
                                        break;
                                    case "5":
                                        List<Cuenta> lcu = this.dao.getAllCuenta();
                                        List<String> ids4 = new ArrayList<>();
                                        for (Cuenta elem : lcu) {
                                            ids4.add(elem.getCodigoCuenta() + "\n");
                                        }
                                        dos.writeUTF(ids4.toString());
                                        accesoOperario(3);
                                        dos.writeUTF("Se ha eliminafo correctamente la cuenta");
                                        break;
                                    case "0":
                                        break;
                                    default:
                                        break;
                                }
                                //  op2 = dis.readUTF();
                            } while (!op2.equals("0"));
                        } else {
                            dos.writeUTF("No existe el usuario con esas credenciales");
                        }
                        break;
                    case "0":

                        break;
                    default:
                        break;
                }
                //op = dis.readUTF();
            } while (op != "0");
        } catch (IOException ex) {
            Logger.getLogger(GESCON.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void accesoCliente(int n, Cuenta cuenta, int dinero) {
        switch (n) {
            case 1:
                cuenta.setSaldo(cuenta.getSaldo() - (float) dinero);
                this.dao.editCuenta(cuenta);
                break;
            case 2:
                cuenta.setSaldo(cuenta.getSaldo() + (float) dinero);
                this.dao.editCuenta(cuenta);
                break;
            default:
                break;
        }
    }

    public synchronized void accesoOperario(int n) throws IOException {
        switch (n) {
            case 1:
                String dni = dis.readUTF();
                String nombre = dis.readUTF();
                String apellidos = dis.readUTF();
                String fecha_nac = dis.readUTF();
                String telefono = dis.readUTF();
                String email = dis.readUTF();
                String log = dis.readUTF();
                String password = dis.readUTF();
                Date d = Date.valueOf(fecha_nac);
                Cliente cl = new Cliente(nombre, apellidos, dni, log, password, d, telefono, email);
                this.dao.insertCliente(cl);
                break;
            case 2:
                String idCliente = dis.readUTF();
                int id = Integer.parseInt(idCliente);
                Cliente c = this.dao.getByIDCliente(id);
                String saldo = dis.readUTF();
                int dinero = Integer.parseInt(saldo);
                Cuenta cuent = new Cuenta(dinero, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()));
                int idCuenta = this.dao.insertCuenta(cuent);
                this.dao.insertClienC(c.getCodigoCliente(), idCuenta, Timestamp.valueOf(LocalDateTime.now()));
                break;
            case 3:
                String idCu = dis.readUTF();
                int idCoun = Integer.parseInt(idCu);
                Cuenta cuenta = this.dao.getByIDCuenta(idCoun);
                this.dao.removeCuenta(cuenta);
                break;
            default:
                break;
        }
    }
}
