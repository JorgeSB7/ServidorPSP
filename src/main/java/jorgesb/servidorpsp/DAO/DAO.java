package jorgesb.servidorpsp.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jorgesb.servidorpsp.Connection.ConnectionUtils;
import jorgesb.servidorpsp.Model.Cliente;
import jorgesb.servidorpsp.Model.Cuenta;
import jorgesb.servidorpsp.Model.Operario;

/**
 *
 * @author jorog
 */
public class DAO {

    Connection conn;

    enum queriesCliente {
        INSERT("INSERT INTO cliente (codigoCliente, dni, login, nombre,apellidos,password,fecha_nac,telefono,email) VALUES (NULL,?,?,?,?,?,?,?,?)"),
        UPDATE("UPDATE cliente SET dni=?,login=?,nombre=?,apellidos=?,password=?,fecha_nac=?,telefono=?,email=? WHERE codigoCliente=?"),
        DELETE("DELETE FROM cliente WHERE codigoCliente=?"),
        GETBYID("SELECT codigoCliente, dni, login, nombre,apellidos,password,fecha_nac,telefono,email FROM cliente Where codigoCliente=?"),
        GETALL("SELECT codigoCliente, dni, login, nombre,apellidos,password,fecha_nac,telefono,email FROM cliente"),
        GETCLIENTBYCREDENTIALS("select codigoCliente, dni, login, nombre,apellidos,password,fecha_nac,telefono,email from cliente where login like ? and  password like ?");

        private String q;

        queriesCliente(String q) {
            this.q = q;
        }

        public String getQ() {
            return this.q;
        }
    }

    enum queriesOperario {
        INSERT("INSERT INTO operarios (ID, Nombre, Apellidos, Login, Password) VALUES (NULL,?,?,?,?)"),
        UPDATE("UPDATE operarios SET Nombre=?,Apellidos=?,Login=?,Password=? WHERE codigoOperario=?"),
        DELETE("DELETE FROM operarios WHERE codigoOperario=?"),
        DELETEALL("DELETE FROM operarios INNER JOIN WHERE codigoOperario=?"),
        GETBYID("SELECT codigoOperario,Nombre,Apellidos,Login,Password FROM operarios Where codigoOperario=?"),
        GETALL("SELECT  codigoOperario,Nombre,Apellidos,Login,Password FROM operarios"),
        GETOPERARIOBYCREDENTIALS("select codigoOperario,Nombre,Apellidos,Login,Password from operarios where Login like ? and  Password like ?");

        private String q;

        queriesOperario(String q) {
            this.q = q;
        }

        public String getQ() {
            return this.q;
        }
    }

    enum queriesCuenta {
        INSERT("INSERT INTO Cuenta (codigoCuenta, saldo, fechahoracreacion, fechahoraultimamodificacion) VALUES (NULL,?,?,?)"),
        UPDATE("UPDATE Cuenta SET saldo=?,fechahoracreacion=?,fechahoraultimamodificacion=? WHERE codigoCuenta=?"),
        DELETE("DELETE FROM Cuenta WHERE codigoCuenta=?"),
        GETBYID("SELECT * FROM Cuenta WHERE codigoCuenta=?"),
        GETALL("SELECT * FROM Cuenta"),
        INSERTCLIENTC("INSERT INTO cliente_cuenta (codigoCliente,codigoCuenta,fechahoraultimoacceso) VALUES(?,?,?)"),
        GETCLIENTBYIDC("SELECT c.* FROM cliente as c INNER JOIN cliente_cuenta as CC on CC.codigoCliente=c.codigoCliente WHERE CC.codigoCuenta=?"),
        GETCOUNTBYIDCLIENT("SELECT c.* FROM cuenta as c INNER JOIN cliente_cuenta as CC on CC.codigoCuenta=c.codigoCuenta WHERE CC.codigoCliente=?");

        private String q;

        queriesCuenta(String q) {
            this.q = q;
        }

        public String getQ() {
            return this.q;
        }
    }

    public synchronized void insertOperario(Operario a) {
        int result = -1;
        try {
            conn = ConnectionUtils.getConnection();
            if (a.getCodigoOperario() > 0) {
                editOperario(a);
            } else {
                PreparedStatement stat = conn.prepareStatement(queriesOperario.INSERT.getQ(), Statement.RETURN_GENERATED_KEYS);
                stat.setString(1, a.getNombre());
                stat.setString(2, a.getApellidos());
                stat.setString(3, a.getLogin());
                stat.setString(4, a.getPassword());

                stat.executeUpdate();
                try ( ResultSet generatedKeys = stat.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getInt(1);
                    }
                }
                a.setCodigoOperario(result);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void editOperario(Operario a) {
        try {
            conn = ConnectionUtils.getConnection();
            PreparedStatement stat = conn.prepareStatement(queriesOperario.UPDATE.getQ());
            stat.setString(1, a.getNombre());
            stat.setString(2, a.getApellidos());
            stat.setString(3, a.getLogin());
            stat.setString(4, a.getPassword());
            stat.setInt(5, a.getCodigoOperario());
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void remove(Operario a) {
        PreparedStatement ps = null;
        try {
            conn = ConnectionUtils.getConnection();
            ps = conn.prepareStatement(queriesOperario.DELETE.getQ());
            ps.setInt(1, a.getCodigoOperario());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("No se ha borrado correctamente");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Metodo que convierte un ResultSet en Cancion
     *
     * @param rs Recibe un ResultSet
     * @return Devuelve una Subscripcion
     * @throws SQLException lanza una SQLException
     */
    protected synchronized Operario convertOperario(ResultSet rs) throws SQLException {

        int id = rs.getInt("codigoOperario");
        String nombre = rs.getString("Nombre");
        String apellidos = rs.getString("Apellidos");
        String login = rs.getString("Login");
        String password = rs.getString("Password");

        Operario c = new Operario(id, nombre, apellidos, login, password);
        return c;
    }

    public synchronized List<Operario> getAllOperario() {
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Operario> listS = new ArrayList<>();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesOperario.GETALL.getQ());
            rs = stat.executeQuery();
            while (rs.next()) {
                listS.add(convertOperario(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return listS;
    }

    /**
     * Metodo que devuelve una Cancion por id pasado
     *
     * @param id identificador de cada Cancion
     * @return Devuelve una Cancion
     */
    public synchronized Operario getByIDOperario(int codigoOperario) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Operario c = new Operario();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesOperario.GETBYID.getQ());
            stat.setInt(1, codigoOperario);
            rs = stat.executeQuery();
            if (rs.next()) {
                c = convertOperario(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return c;
    }

    public synchronized Operario getOperarioByCredentials(String login, String pass) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Operario a = new Operario();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesOperario.GETOPERARIOBYCREDENTIALS.getQ());
            stat.setString(1, login);
            stat.setString(2, pass);
            rs = stat.executeQuery();
            if (rs.next()) {
                a = convertOperario(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return a;
    }

    /**
     * Metodo que comprueba si existe el ID en la tabla
     *
     * @param id recibe un entero
     * @return devuelve un boolean, si existe devuelve true y false si no
     */
    public synchronized boolean searchByIDOperario(int codigoOperario) {
        boolean result = false;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesOperario.GETBYID.getQ());
            stat.setInt(1, codigoOperario);
            rs = stat.executeQuery();
            if (rs.next()) {
                Operario c = convertOperario(rs);
                if (c.getCodigoOperario() != -1) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public synchronized void insertCliente(Cliente a) {
        int result = -1;
        try {
            conn = ConnectionUtils.getConnection();
            if (a.getCodigoCliente() > 0) {
                editCliente(a);
            } else {
                PreparedStatement stat = conn.prepareStatement(queriesCliente.INSERT.getQ(), Statement.RETURN_GENERATED_KEYS);
                stat.setString(1, a.getDni());
                stat.setString(2, a.getLogin());
                stat.setString(3, a.getNombre());
                stat.setString(4, a.getApellidos());
                stat.setString(5, a.getPassword());
                stat.setDate(6, a.getFecha_nac());
                stat.setString(7, a.getTelefono());
                stat.setString(8, a.getEmail());

                stat.executeUpdate();
                try ( ResultSet generatedKeys = stat.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getInt(1);
                    }
                }
                a.setCodigoCliente(result);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void editCliente(Cliente a) {
        try {
            conn = ConnectionUtils.getConnection();
            PreparedStatement stat = conn.prepareStatement(queriesCliente.UPDATE.getQ());
            stat.setString(1, a.getDni());
            stat.setString(2, a.getLogin());
            stat.setString(3, a.getNombre());
            stat.setString(4, a.getApellidos());
            stat.setString(5, a.getPassword());
            stat.setDate(6, a.getFecha_nac());
            stat.setString(7, a.getTelefono());
            stat.setString(8, a.getEmail());
            stat.setInt(9, a.getCodigoCliente());
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void removeCliente(Cliente a) {
        PreparedStatement ps = null;
        try {
            conn = ConnectionUtils.getConnection();
            ps = conn.prepareStatement(queriesCliente.DELETE.getQ());
            ps.setInt(1, a.getCodigoCliente());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("No se ha borrado correctamente");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized Cliente convertCliente(ResultSet rs) throws SQLException {
        int codigoCliente = rs.getInt("codigoCliente");
        String dni = rs.getString("dni");
        String login = rs.getString("login");
        String nombre = rs.getString("nombre");
        String apellidos = rs.getString("apellidos");
        String password = rs.getString("password");
        Date fecha_nac = rs.getDate("fecha_nac");
        String telefono = rs.getString("telefono");
        String email = rs.getString("email");
        Cliente a = new Cliente(codigoCliente, dni, login, nombre, apellidos, password, fecha_nac, telefono, email);
        return a;
    }

    public synchronized List<Cliente> getAllCliente() {
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Cliente> listA = new ArrayList<>();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCliente.GETALL.getQ());
            rs = stat.executeQuery();
            while (rs.next()) {
                listA.add(convertCliente(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return listA;
    }

    public synchronized Cliente getByIDCliente(int id) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Cliente a = new Cliente();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCliente.GETBYID.getQ());
            stat.setInt(1, id);
            rs = stat.executeQuery();
            if (rs.next()) {
                a = convertCliente(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return a;
    }

    public synchronized Cliente getClienteByCredentials(String login, String pass) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Cliente a = new Cliente();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCliente.GETCLIENTBYCREDENTIALS.getQ());
            stat.setString(1, login);
            stat.setString(2, pass);
            rs = stat.executeQuery();
            if (rs.next()) {
                a = convertCliente(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return a;
    }

    public synchronized boolean searchByIDCliente(int id) {
        boolean result = false;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCliente.GETBYID.getQ());
            stat.setInt(1, id);
            rs = stat.executeQuery();
            if (rs.next()) {
                Cliente c = convertCliente(rs);
                if (c.getCodigoCliente() != -1) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public synchronized int insertCuenta(Cuenta a) {
        int result = -1;
        try {
            conn = ConnectionUtils.getConnection();
            if (a.getCodigoCuenta() > 0) {
                editCuenta(a);
            } else {
                PreparedStatement stat = conn.prepareStatement(queriesCuenta.INSERT.getQ(), Statement.RETURN_GENERATED_KEYS);
                stat.setFloat(1, a.getSaldo());
                stat.setTimestamp(2, a.getFechaHoraC());
                stat.setTimestamp(3, a.getFechaHoraUM());

                stat.executeUpdate();
                try ( ResultSet generatedKeys = stat.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getInt(1);
                    }
                }
                a.setCodigoCuenta(result);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a.getCodigoCuenta();
    }

    public synchronized void editCuenta(Cuenta a) {
        try {
            conn = ConnectionUtils.getConnection();
            PreparedStatement stat = conn.prepareStatement(queriesCuenta.UPDATE.getQ());
            stat.setFloat(1, a.getSaldo());
            stat.setTimestamp(2, (java.sql.Timestamp) a.getFechaHoraC());
            stat.setTimestamp(3, (java.sql.Timestamp) a.getFechaHoraUM());
            stat.setInt(4, a.getCodigoCuenta());
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void removeCuenta(Cuenta a) {
        PreparedStatement ps = null;
        try {
            conn = ConnectionUtils.getConnection();
            ps = conn.prepareStatement(queriesCuenta.DELETE.getQ());
            ps.setInt(1, a.getCodigoCuenta());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("No se ha borrado correctametne");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected synchronized Cuenta convertCuenta(ResultSet rs) throws SQLException {
        int codigoCuenta = rs.getInt("codigoCuenta");
        Float saldo = rs.getFloat("saldo");
        Timestamp fechaHoraC = rs.getTimestamp("fechahoracreacion");
        Timestamp fechaHoraUM = rs.getTimestamp("fechahoraultimamodificacion");
        Cuenta a = new Cuenta(codigoCuenta, saldo, fechaHoraC, fechaHoraUM);
        return a;
    }

    public synchronized List<Cuenta> getAllCuenta() {
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Cuenta> listA = new ArrayList<>();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCuenta.GETALL.getQ());
            rs = stat.executeQuery();
            while (rs.next()) {
                listA.add(convertCuenta(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return listA;
    }

    public synchronized Cuenta getByIDCuenta(int codigoCuenta) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Cuenta a = new Cuenta();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCuenta.GETBYID.getQ());
            stat.setInt(1, codigoCuenta);
            rs = stat.executeQuery();
            if (rs.next()) {
                a = convertCuenta(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return a;
    }

    public synchronized boolean searchByIDCuenta(int codigoCuenta) {
        boolean result = false;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCuenta.GETBYID.getQ());
            stat.setInt(1, codigoCuenta);
            rs = stat.executeQuery();
            if (rs.next()) {
                Cuenta c = convertCuenta(rs);
                if (c.getCodigoCuenta() != -1) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public synchronized boolean insertClienC(int a, int c, Timestamp f) {
        boolean result = false;

        try {
            PreparedStatement stat = null;
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCuenta.INSERTCLIENTC.getQ());
            if (getByIDCuenta(a) != null && getByIDCliente(c) != null) {
                stat.setInt(1, a);
                stat.setInt(2, c);
                stat.setTimestamp(3, f);
                stat.executeUpdate();
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public synchronized List<Cliente> getListClienteCount(int id) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCuenta.GETCLIENTBYIDC.getQ());
            stat.setInt(1, id);
            rs = stat.executeQuery();
            while (rs.next()) {
                clientes.add(convertCliente(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return clientes;
    }

    public synchronized Cuenta getCountByClient(int codigoCliente) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        Cuenta a = new Cuenta();
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCuenta.GETCOUNTBYIDCLIENT.getQ());
            stat.setInt(1, codigoCliente);
            rs = stat.executeQuery();
            if (rs.next()) {
                a = convertCuenta(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return a;
    }

    public synchronized boolean searchCountByClient(int codigoCliente) {
        boolean result = false;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = ConnectionUtils.getConnection();
            stat = conn.prepareStatement(queriesCuenta.GETCOUNTBYIDCLIENT.getQ());
            stat.setInt(1, codigoCliente);
            rs = stat.executeQuery();
            if (rs.next()) {
                Cuenta c = convertCuenta(rs);
                if (c.getCodigoCuenta() != -1) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
}
