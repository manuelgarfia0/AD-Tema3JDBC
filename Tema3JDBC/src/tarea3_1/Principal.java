package tarea3_1;

import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int menu;

		do {
			System.out.println("\nIntroduce una opción: ");
			System.out.println("1. Crear");
			System.out.println("2. Insertar");
			System.out.println("3. Mostrar");
			System.out.println("4. Borrar");
			System.out.println("5. Salir");
			System.out.print("Opción: ");
			menu = sc.nextInt();
			sc.nextLine();

			switch (menu) {
			case 1 -> GestorTablas.menuCrear(sc);
			case 2 -> GestorInsertar.menuInsertar(sc);
			case 3 -> GestorTablas.menuMostrar(sc);
			case 4 -> GestorTablas.menuBorrar(sc);
			case 5 -> System.out.println("Saliendo...");
			default -> System.out.println("Opción no válida.");
			}
		} while (menu != 5);

		sc.close();
	}
}
