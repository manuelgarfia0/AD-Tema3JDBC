package tarea3_1;

import java.sql.*;
import java.util.Scanner;

public class GestorTablas {

	public static void menuCrear(Scanner sc) {
		System.out.println("¿Qué deseas crear?");
		System.out.println("1. Mesa");
		System.out.println("2. Factura");
		System.out.println("3. Productos");
		System.out.println("4. Pedido");
		System.out.println("5. Todas las tablas");
		System.out.print("Opción: ");
		int opcion = sc.nextInt();

		switch (opcion) {
		case 1 -> crearMesa();
		case 2 -> crearFactura();
		case 3 -> crearProductos();
		case 4 -> crearPedido();
		case 5 -> {
			crearMesa();
			crearFactura();
			crearProductos();
			crearPedido();
		}
		default -> System.out.println("Opción no válida.");
		}
	}

	public static void menuMostrar(Scanner sc) {
		System.out.println("¿Qué tabla deseas mostrar?");
		System.out.println("1. Mesa");
		System.out.println("2. Factura");
		System.out.println("3. Productos");
		System.out.println("4. Pedido");
		System.out.println("5. Todas las tablas");
		System.out.print("Opción: ");
		int opcionMostrar = sc.nextInt();

		switch (opcionMostrar) {
		case 1 -> mostrarSiExiste("Mesa");
		case 2 -> mostrarSiExiste("Factura");
		case 3 -> mostrarSiExiste("Productos");
		case 4 -> mostrarSiExiste("Pedido");
		case 5 -> {
			mostrarSiExiste("Mesa");
			mostrarSiExiste("Factura");
			mostrarSiExiste("Productos");
			mostrarSiExiste("Pedido");
		}
		default -> System.out.println("Opción no válida.");
		}
	}

	public static void menuBorrar(Scanner sc) {
		System.out.println("¿Qué tabla deseas borrar?");
		System.out.println("1. Mesa");
		System.out.println("2. Factura");
		System.out.println("3. Productos");
		System.out.println("4. Pedido");
		System.out.println("5. Todas las tablas");
		System.out.print("Opción: ");
		int opcionBorrar = sc.nextInt();

		switch (opcionBorrar) {
		case 1 -> borrarMesa();
		case 2 -> borrarFactura();
		case 3 -> borrarProductos();
		case 4 -> borrarPedido();
		case 5 -> borrarTodas();
		default -> System.out.println("Opción no válida.");
		}
	}

	public static boolean tablaExiste(String nombreTabla) {
		String sql = "SHOW TABLES LIKE ?";
		try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nombreTabla);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			System.err.println("Error al comprobar la tabla: " + e.getMessage());
			return false;
		}
	}

	private static void mostrarSiExiste(String tabla) {
		if (tablaExiste(tabla))
			mostrarTabla(tabla);
		else
			System.out.println("La tabla '" + tabla + "' no existe.");
	}

	public static boolean crearTabla(String sql) {
		try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.executeUpdate();
			System.out.println("Tabla creada correctamente.");
			return true;
		} catch (SQLException e) {
			System.err.println("Error al crear la tabla: " + e.getMessage());
			return false;
		}
	}

	public static void crearMesa() {
		System.out.println("Creando tabla Mesa...");
		crearTabla("""
				    CREATE TABLE IF NOT EXISTS Mesa (
				        idMesa INT PRIMARY KEY AUTO_INCREMENT,
				        numComensales INT,
				        reserva TINYINT
				    );
				""");
	}

	public static void crearFactura() {
		if (!tablaExiste("Mesa")) {
			System.out.println("No se puede crear Factura: depende de Mesa.");
			return;
		}
		System.out.println("Creando tabla Factura...");
		crearTabla("""
				    CREATE TABLE IF NOT EXISTS Factura (
				        idFactura INT PRIMARY KEY AUTO_INCREMENT,
				        idMesa INT,
				        tipoPago VARCHAR(45),
				        Importe DECIMAL(10,2),
				        FOREIGN KEY (idMesa) REFERENCES Mesa(idMesa)
				    );
				""");
	}

	public static void crearProductos() {
		System.out.println("Creando tabla Productos...");
		crearTabla("""
				    CREATE TABLE IF NOT EXISTS Productos (
				        idProducto INT PRIMARY KEY AUTO_INCREMENT,
				        Denominacion VARCHAR(45),
				        Precio DECIMAL(10,2)
				    );
				""");
	}

	public static void crearPedido() {
		if (!tablaExiste("Factura") || !tablaExiste("Productos")) {
			System.out.println("No se puede crear Pedido: depende de Factura y Productos.");
			return;
		}
		System.out.println("Creando tabla Pedido...");
		crearTabla("""
				    CREATE TABLE IF NOT EXISTS Pedido (
				        idPedido INT PRIMARY KEY AUTO_INCREMENT,
				        idFactura INT,
				        idProducto INT,
				        cantidad INT,
				        FOREIGN KEY (idFactura) REFERENCES Factura(idFactura),
				        FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
				    );
				""");
	}

	public static void mostrarTabla(String tabla) {
		try (Connection con = ConexionBD.conectar();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM " + tabla)) {

			ResultSetMetaData meta = rs.getMetaData();
			int cols = meta.getColumnCount();

			for (int i = 1; i <= cols; i++)
				System.out.print(meta.getColumnName(i) + "\t");
			System.out.println();

			while (rs.next()) {
				for (int i = 1; i <= cols; i++)
					System.out.print(rs.getString(i) + "\t");
				System.out.println();
			}

		} catch (SQLException e) {
			System.err.println("Error al mostrar tabla: " + e.getMessage());
		}
	}

	private static void borrarTabla(String nombreTabla) {
		try (Connection con = ConexionBD.conectar();
				PreparedStatement ps = con.prepareStatement("DROP TABLE IF EXISTS " + nombreTabla)) {
			ps.executeUpdate();
			System.out.println("Tabla '" + nombreTabla + "' borrada correctamente.");
		} catch (SQLException e) {
			System.err.println("Error al borrar la tabla: " + e.getMessage());
		}
	}

	public static void borrarPedido() {
		if (tablaExiste("Pedido"))
			borrarTabla("Pedido");
		else
			System.out.println("La tabla 'Pedido' no existe.");
	}

	public static void borrarFactura() {
		if (tablaExiste("Pedido")) {
			System.out.println("Primero borra 'Pedido' (depende de Factura).");
		} else if (tablaExiste("Factura")) {
			borrarTabla("Factura");
		} else {
			System.out.println("La tabla 'Factura' no existe.");
		}
	}

	public static void borrarProductos() {
		if (tablaExiste("Pedido")) {
			System.out.println("Primero borra 'Pedido' (depende de Productos).");
		} else if (tablaExiste("Productos")) {
			borrarTabla("Productos");
		} else {
			System.out.println("La tabla 'Productos' no existe.");
		}
	}

	public static void borrarMesa() {
		if (tablaExiste("Factura")) {
			System.out.println("Primero borra 'Factura' (depende de Mesa).");
		} else if (tablaExiste("Mesa")) {
			borrarTabla("Mesa");
		} else {
			System.out.println("La tabla 'Mesa' no existe.");
		}
	}

	public static void borrarTodas() {
		System.out.println("Borrando todas las tablas...");
		if (tablaExiste("Pedido"))
			borrarPedido();
		if (tablaExiste("Factura"))
			borrarFactura();
		if (tablaExiste("Productos"))
			borrarProductos();
		if (tablaExiste("Mesa"))
			borrarMesa();
	}
}
