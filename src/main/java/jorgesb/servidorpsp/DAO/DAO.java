package jorgesb.servidorpsp.DAO;

/**
 *
 * @author jorog
 */
public class DAO {
     enum queriesCliente {
        INSERT("INSERT INTO cliente (codigoCliente, dni, login, nombre,apellidos,password,fecha_nac,telefono,email) VALUES (NULL,?,?,?,?,?,?,?,?)"),
        UPDATE("UPDATE cliente SET dni=?,login=?,nombre=?,apellidos=?,password=?,fecha_nac=?,telefono=?,email=? WHERE codigoCliente=?"),
        DELETE("DELETE FROM cliente WHERE codigoCliente=?"),
        GETBYID("SELECT codigoCliente, dni, login, nombre,apellidos,password,fecha_nac,telefono,email FROM cliente Where codigoCliente=?"),
        GETALL("SELECT codigoCliente, dni, login, nombre,apellidos,password,fecha_nac,telefono,email FROM cliente");
        //   GETDISCOLISTBYID("SELECT d.ID, d.Nombre, d.Foto, d.fechap, d.IDArtista FROM disco as d INNER JOIN artista as art on art.ID=d.IDArtista WHERE art.ID=?");

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
        GETALL("SELECT  codigoOperario,Nombre,Apellidos,Login,Password FROM operarios");

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
    
            
}
