package jorgesb.servidorpsp.MAIN;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import jorgesb.servidorpsp.DAO.DAO;
import jorgesb.servidorpsp.GESCON.GESCON;

/**
 *
 * @author jorog
 */
public class MAIN {
    public static void main(String[] args) {
          ServerSocket ss;
          DAO dao=new DAO();
        System.out.print("Inicializando servidor... ");
        try {
            ss = new ServerSocket(10578);
            System.out.println("\t[OK]");
            int idSession = 0;
            while (true) {
                Socket socket;
                socket = ss.accept();
                System.out.println("Nueva conexión entrante: "+socket);
                ((GESCON) new GESCON(socket, idSession,dao)).start();
                idSession++;
            }
        } catch (IOException ex) {
            Logger.getLogger(MAIN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
