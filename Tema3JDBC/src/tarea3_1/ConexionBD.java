package tarea3_1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

	public static Connection conectar() throws SQLException {
		return DriverManager.getConnection(Constantes.URL, Constantes.USUARIO, Constantes.CONTRASEÑA);
	}
}
