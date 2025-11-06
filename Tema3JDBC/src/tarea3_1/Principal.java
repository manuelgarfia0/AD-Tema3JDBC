package tarea3_1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {

		// Creación del escaner
		Scanner sc = new Scanner(System.in);

		// Variable para introducir opción del menú
		int menu;

		// Mostrar menú
		System.out.println("Introduce una opción: ");
		System.out.println("1. Crear");
		System.out.println("2. Insertar");
		System.out.println("3. Mostrar");
		System.out.println("4. Borrar");

		// El usuario introduce una opción
		menu = sc.nextInt();

		// Limpiar buffer
		sc.nextLine();

		// Switch para cada caso
		switch (menu) {
		// Creación de tablas
		case 1 -> {
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
		// Insercción de datos
		case 2 -> {

		}
		// Mostrar tablas
		// Mostrar tablas
		case 3 -> {
			System.out.println("¿Qué tabla deseas mostrar?");
			System.out.println("1. Mesa");
			System.out.println("2. Factura");
			System.out.println("3. Productos");
			System.out.println("4. Pedido");
			System.out.println("5. Todas las tablas");
			System.out.print("Opción: ");
			int opcionMostrar = sc.nextInt();

			switch (opcionMostrar) {
			case 1 -> {
				if (tablaExiste("Mesa"))
					mostrarTabla("Mesa");
				else
					System.out.println("La tabla 'Mesa' no existe.");
			}
			case 2 -> {
				if (tablaExiste("Factura"))
					mostrarTabla("Factura");
				else
					System.out.println("La tabla 'Factura' no existe.");
			}
			case 3 -> {
				if (tablaExiste("Productos"))
					mostrarTabla("Productos");
				else
					System.out.println("La tabla 'Productos' no existe.");
			}
			case 4 -> {
				if (tablaExiste("Pedido"))
					mostrarTabla("Pedido");
				else
					System.out.println("La tabla 'Pedido' no existe.");
			}
			case 5 -> {
				if (tablaExiste("Mesa"))
					mostrarTabla("Mesa");
				if (tablaExiste("Factura"))
					mostrarTabla("Factura");
				if (tablaExiste("Productos"))
					mostrarTabla("Productos");
				if (tablaExiste("Pedido"))
					mostrarTabla("Pedido");
			}
			default -> System.out.println("Opción no válida.");
			}
		}

		// Borrar tablas
		case 4 -> {

		}

		default -> {
			System.out.println("Opción no valida");
		}

		}

		// Cerrar scanner
		sc.close();
	}

	// Método general para ejecutar sentencias CREATE
	public static boolean crearTabla(String sql) {
		boolean creado = false;

		try (Connection con = DriverManager.getConnection(Constantes.URL, Constantes.USUARIO, Constantes.CONTRASEÑA);
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.executeUpdate();
			creado = true;
			System.out.println("Tabla creada correctamente.");

		} catch (SQLException e) {
			System.err.println("Error al crear la tabla: " + e.getMessage());
		}

		return creado;
	}

	public static boolean crearMesa() {
		System.out.println("Creando tabla Mesa...");
		String sql = """
				CREATE TABLE IF NOT EXISTS Mesa (
				    idMesa INT PRIMARY KEY AUTO_INCREMENT,
				    numComensales INT,
				    reserva TINYINT
				);
				""";
		boolean creado = crearTabla(sql);
		return creado;
	}

	public static boolean crearFactura() {
		System.out.println("Creando tabla Factura...");
		boolean creado = false;

		if (tablaExiste("Mesa")) {
			String sql = """
					CREATE TABLE IF NOT EXISTS Factura (
					    idFactura INT PRIMARY KEY AUTO_INCREMENT,
					    idMesa INT,
					    tipoPago VARCHAR(45),
					    Importe DECIMAL(10,2),
					    FOREIGN KEY (idMesa) REFERENCES Mesa(idMesa)
					);
					""";
			creado = crearTabla(sql);
		} else {
			System.out.println("No se puede crear 'Factura' porque depende de 'Mesa'.");
		}

		return creado;
	}

	public static boolean crearProductos() {
		boolean creado = false;
		System.out.println("Creando tabla Productos...");
		String sql = """
				CREATE TABLE IF NOT EXISTS Productos (
				    idProducto INT PRIMARY KEY AUTO_INCREMENT,
				    Denominacion VARCHAR(45),
				    Precio DECIMAL(10,2)
				);
				""";
		creado = crearTabla(sql);
		return creado;
	}

	public static boolean crearPedido() {
		System.out.println("Creando tabla Pedido...");
		boolean creado = false;

		if (tablaExiste("Factura") && tablaExiste("Productos")) {
			String sql = """
					CREATE TABLE IF NOT EXISTS Pedido (
					    idPedido INT PRIMARY KEY AUTO_INCREMENT,
					    idFactura INT,
					    idProducto INT,
					    cantidad INT,
					    FOREIGN KEY (idFactura) REFERENCES Factura(idFactura),
					    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
					);
					""";
			creado = crearTabla(sql);
		} else {
			System.out.println("No se puede crear 'Pedido' porque depende de 'Factura' y 'Productos'.");
		}

		return creado;
	}

	public static boolean tablaExiste(String nombreTabla) {
		boolean existe = false;
		String sql = "SHOW TABLES LIKE ?";

		try (Connection con = DriverManager.getConnection(Constantes.URL, Constantes.USUARIO, Constantes.CONTRASEÑA);
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, nombreTabla);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					existe = true;
				}
			}

		} catch (SQLException e) {
			System.err.println("Error al comprobar existencia de tabla: " + e.getMessage());
		}

		return existe;
	}

	public static void mostrarTabla(String tabla) {
		try (Connection con = DriverManager.getConnection(Constantes.URL, Constantes.USUARIO, Constantes.CONTRASEÑA)) {

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM " + tabla);

			ResultSetMetaData metaData = rs.getMetaData();
			int numColumns = metaData.getColumnCount();

			for (int i = 1; i <= numColumns; i++) {
				System.out.print(metaData.getColumnName(i) + "\t");
			}
			System.out.println();

			while (rs.next()) {
				for (int i = 1; i <= numColumns; i++) {
					System.out.print(rs.getString(i) + "\t");
				}
				System.out.println();
			}

		} catch (SQLException e) {
			System.err.println("Error al mostrar la tabla: " + e.getMessage());
		}
	}
}