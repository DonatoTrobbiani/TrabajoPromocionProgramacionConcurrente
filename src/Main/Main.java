package Main;

import java.util.Scanner;

import App.Parque;
import App.Persona;
import Gestores.GestorTiempo;
import RealidadVirtual.EspacioVirtual;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Valores por defecto
        int cantVR = 1;
        int cantEncargadosJuegos = 3;
        int cantPersonas = 5;
        int cantMolinetes = 3;

        System.out.println("Bienvenido al simulador de parque de diversiones.");
        System.out.println("Desea utilizar los valores por defecto? (1: Sí, 0: No)");
        System.out.println("Equipos de readlidad virtual: " + cantVR + "\nEncargados de juegos: "
                + cantEncargadosJuegos + "\nPersonas: " + cantPersonas + "\nMolinetes: " + cantMolinetes);
        char opcion = scanner.next().charAt(0);

        if (opcion == '0') {
            // Ingresa los nuevos valores
            System.out.println("Ingrese la cantidad de equipos del espacio virtual (por defecto 1):");
            cantVR = scanner.nextInt();

            System.out.println("Ingrese la cantidad de encargados de juegos (por defecto 3):");
            cantEncargadosJuegos = scanner.nextInt();

            System.out.println("Ingrese la cantidad de personas (por defecto 5):");
            cantPersonas = scanner.nextInt();

            System.out.println("Ingrese la cantidad de molinetes del parque (por defecto 3):");
            cantMolinetes = scanner.nextInt();
        }
        scanner.close();

        // Inicializa el parque

        GestorTiempo gestorTiempo = new GestorTiempo(null);
        EspacioVirtual espacioVirtual = new EspacioVirtual(cantVR, cantVR * 2, cantVR, gestorTiempo);
        Parque parque = new Parque(cantMolinetes, gestorTiempo, cantEncargadosJuegos, espacioVirtual);
        gestorTiempo.setParque(parque);
        Thread hiloGestorTiempo = new Thread(gestorTiempo);
        Thread[] hilos = new Thread[cantPersonas];

        hiloGestorTiempo.start();
        for (int i = 0; i < cantPersonas; i++) {
            hilos[i] = new Thread(new Persona(parque, "Persona " + i));
            hilos[i].start();
        }

        try {
            for (Thread thread : hilos) {
                thread.join();
            }
            hiloGestorTiempo.interrupt();
        } catch (InterruptedException e) {
            System.out.println("Simulación terminada.");
        }
    }
}
