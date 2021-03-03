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
       /* 
       ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;
 
        //puerto de nuestro servidor
        final int PUERTO = 10578;
 
        try {
            //Creamos el socket del servidor
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
 
            //Siempre estara escuchando peticiones
            while (true) {
 
                //Espero a que un cliente se conecte
                sc = servidor.accept();
 
                System.out.println("Cliente conectado");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());
 
                //Leo el mensaje que me envia
                String mensaje = in.readUTF();
 
                System.out.println(mensaje);
                 String mensaje2 = in.readUTF();
 
                System.out.println(mensaje2);
                //Le envio un mensaje
                out.writeUTF("¡Hola mundo desde el servidor!");
 
                //Cierro el socket
                sc.close();
                System.out.println("Cliente desconectado");
 
            }
 
        } catch (IOException ex) {
            Logger.getLogger(MAIN.class.getName()).log(Level.SEVERE, null, ex);
        }
 */
    }
}
