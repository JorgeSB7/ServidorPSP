package jorgesb.servidorpsp.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author jorog
 */
public class Cuenta extends Thread{
    protected int codigoCuenta;
    protected float saldo;
    protected Timestamp fechaHoraC;
    protected Timestamp fechaHoraUM;
    protected ArrayList<Cliente> ListClient;
    protected Timestamp fechaHoraUA;
    
    public Cuenta() {
        this.codigoCuenta=-1;
    }

    public Cuenta(float saldo, Timestamp fechaHoraC, Timestamp fechaHoraUM) {
        this.saldo = saldo;
        this.fechaHoraC = fechaHoraC;
        this.fechaHoraUM = fechaHoraUM;
    }

    public Cuenta(int codigoCuenta, float saldo, Timestamp fechaHoraC, Timestamp fechaHoraUM) {
        this.codigoCuenta = codigoCuenta;
        this.saldo = saldo;
        this.fechaHoraC = fechaHoraC;
        this.fechaHoraUM = fechaHoraUM;
    }

    public Cuenta(int codigoCuenta, float saldo, Timestamp fechaHoraC, Timestamp fechaHoraUM, ArrayList<Cliente> ListClient, Timestamp fechaHoraUA) {
        this.codigoCuenta = codigoCuenta;
        this.saldo = saldo;
        this.fechaHoraC = fechaHoraC;
        this.fechaHoraUM = fechaHoraUM;
        this.ListClient = ListClient;
        this.fechaHoraUA = fechaHoraUA;
    }

    public ArrayList<Cliente> getListClient() {
        return ListClient;
    }

    public void setListClient(ArrayList<Cliente> ListClient) {
        this.ListClient = ListClient;
    }

    public Timestamp getFechaHoraUA() {
        return fechaHoraUA;
    }

    public void setFechaHoraUA(Timestamp fechaHoraUA) {
        this.fechaHoraUA = fechaHoraUA;
    }

    public int getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(int codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public Timestamp getFechaHoraC() {
        return fechaHoraC;
    }

    public void setFechaHoraC(Timestamp fechaHoraC) {
        this.fechaHoraC = fechaHoraC;
    }

    public Timestamp getFechaHoraUM() {
        return fechaHoraUM;
    }

    public void setFechaHoraUM(Timestamp fechaHoraUM) {
        this.fechaHoraUM = fechaHoraUM;
    }

    @Override
    public String toString() {
        return "Cuenta{" + "codigoCuenta=" + codigoCuenta + ", saldo=" + saldo + ", fechaHoraC=" + fechaHoraC + ", fechaHoraUM=" + fechaHoraUM + '}'+"\n";
    }


    
}
