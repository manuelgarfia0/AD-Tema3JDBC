package tarea3_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class GestorInsertar {

	public static void menuInsertar(Scanner sc) {
		System.out.println("¿En qué tabla deseas insertar datos?");
		System.out.println("1. Mesa");
		System.out.println("2. Factura");
		System.out.println("3. Productos");
		System.out.println("4. Pedido");
		System.out.print("Opción: ");
		int opcion = sc.nextInt();
		sc.nextLine(); // limpiar buffer

		switch (opcion) {
		case 1 -> insertarMesa(sc);
		case 2 -> insertarFactura(sc);
		case 3 -> insertarProducto(sc);
		case 4 -> insertarPedido(sc);
		default -> System.out.println("Opción no válida.");
		}
	}

	// ===== INSERTAR EN MESA =====
	public static void insertarMesa(Scanner sc) {
		if (!GestorTablas.tablaExiste("Mesa")) {
			System.out.println("La tabla 'Mesa' no existe.");
			return;
		}

		System.out.print("Número de comensales: ");
		int numComensales = sc.nextInt();
		System.out.print("¿Está reservada? (1 = Sí, 0 = No): ");
		int reserva = sc.nextInt();

		String sql = "INSERT INTO Mesa (numComensales, reserva) VALUES (?, ?)";

		try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, numComensales);
			ps.setInt(2, reserva);
			ps.executeUpdate();
			System.out.println("Mesa insertada correctamente.");

		} catch (SQLException e) {
			System.err.println("Error al insertar mesa: " + e.getMessage());
		}
	}

	// ===== INSERTAR EN FACTURA =====
	public static void insertarFactura(Scanner sc) {
		if (!GestorTablas.tablaExiste("Factura")) {
			System.out.println("La tabla 'Factura' no existe.");
			return;
		}

		System.out.print("ID de la mesa: ");
		int idMesa = sc.nextInt();
		sc.nextLine(); // limpiar
		System.out.print("Tipo de pago: ");
		String tipoPago = sc.nextLine();
		System.out.print("Importe: ");
		double importe = sc.nextDouble();

		String sql = "INSERT INTO Factura (idMesa, tipoPago, Importe) VALUES (?, ?, ?)";

		try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idMesa);
			ps.setString(2, tipoPago);
			ps.setDouble(3, importe);
			ps.executeUpdate();
			System.out.println("Factura insertada correctamente.");

		} catch (SQLException e) {
			System.err.println("Error al insertar factura: " + e.getMessage());
		}
	}

	// ===== INSERTAR EN PRODUCTOS =====
	public static void insertarProducto(Scanner sc) {
		if (!GestorTablas.tablaExiste("Productos")) {
			System.out.println("La tabla 'Productos' no existe.");
			return;
		}

		System.out.print("Denominación del producto: ");
		String denominacion = sc.nextLine();
		System.out.print("Precio: ");
		double precio = sc.nextDouble();

		String sql = "INSERT INTO Productos (Denominacion, Precio) VALUES (?, ?)";

		try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, denominacion);
			ps.setDouble(2, precio);
			ps.executeUpdate();
			System.out.println("Producto insertado correctamente.");

		} catch (SQLException e) {
			System.err.println("Error al insertar producto: " + e.getMessage());
		}
	}

	// ===== INSERTAR EN PEDIDO =====
	public static void insertarPedido(Scanner sc) {
		if (!GestorTablas.tablaExiste("Pedido")) {
			System.out.println("La tabla 'Pedido' no existe.");
			return;
		}

		System.out.print("ID de factura: ");
		int idFactura = sc.nextInt();
		System.out.print("ID de producto: ");
		int idProducto = sc.nextInt();
		System.out.print("Cantidad: ");
		int cantidad = sc.nextInt();

		String sql = "INSERT INTO Pedido (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";

		try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idFactura);
			ps.setInt(2, idProducto);
			ps.setInt(3, cantidad);
			ps.executeUpdate();
			System.out.println("Pedido insertado correctamente.");

		} catch (SQLException e) {
			System.err.println("Error al insertar pedido: " + e.getMessage());
		}
	}
}
