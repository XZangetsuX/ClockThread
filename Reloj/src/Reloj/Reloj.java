package Reloj;

import java.io.*;
import java.text.*;
import java.util.*;

public class Reloj extends Thread {
	private boolean encendido;
	private boolean detenido;
	private SimpleDateFormat formato;

	public Reloj() {
		super();
		encendido = true;
		detenido = false;
		formato = new SimpleDateFormat("HH:mm:ss");
	}

	public void parar() {
		encendido = false;
		detenido = true;
		try {
			join(); // Espera a que el hilo muera...
		} catch (InterruptedException ex) {
			System.err.println("Ups! No me puedo detener " + ex.getMessage());
		}
	}

	public void detener() {
		detenido = true;
	}

	public void run() { // Código ejecutable por el hilo
		while (encendido) {
			if (detenido) {
				synchronized (this) {
					try {
						wait(); // Bloquear
					} catch (InterruptedException ex) {
					}
				}
			}
			Calendar tiempo = Calendar.getInstance();
			System.out.println(formato.format(tiempo.getTime()));
			try {
				sleep(1000); // Suspender por 1 segundo
			} catch (InterruptedException ex) {
			}
		}
	}

	public void reanudar() {
		detenido = false;
		synchronized (this) {
			notify(); // Desbloquear, pasar a estado de ejecución
		}
	}

	public static void main(String[] args) {
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		Reloj reloj = new Reloj();
		int opcion;
		do {
			System.out.println("1. Arrancar");
			System.out.println("2. Detener");
			System.out.println("3. Reanudar");
			System.out.println("4. Salir");
			System.out.print("> ");
			try {
				opcion = Integer.parseInt(cin.readLine());
			} catch (IOException | NumberFormatException ex) {
				opcion = 0;
			}
			switch (opcion) {
			case 1:
				reloj.start();
				break;
			case 2:
				reloj.detener();
				break;
			case 3:
				reloj.reanudar();
				break;
			case 4:
				reloj.parar();
				System.exit(0);
				break;
			}
		} while (opcion != 4);
		System.exit(0);
	}
}
