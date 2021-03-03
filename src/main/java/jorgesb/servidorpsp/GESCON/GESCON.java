package jorgesb.servidorpsp.GESCON;

import java.io.*;
import java.net.*;
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
                        String op2 = dis.readUTF();
                        do {
                            switch (op2) {
                                case "1":

                                    break;
                                case "2":

                                    break;
                                case "3":

                                    break;
                                case "4":

                                    break;
                                case "5":

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
