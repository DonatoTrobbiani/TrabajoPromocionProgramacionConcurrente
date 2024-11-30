package App;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;

import AreaDeJuegosDePremios.EncargadorJuegos;
import Comedor.Comedor;
import Gestores.GestorTiempo;
import RealidadVirtual.EspacioVirtual;
import TrenesDelParque.Conductor;
import TrenesDelParque.Tren;

public class TestPersonas {
    public static void main(String[] args) {
        GestorTiempo gestorTiempo = new GestorTiempo(null);
        int cantRecursos = 1;
        EspacioVirtual espacioVirtual = new EspacioVirtual(cantRecursos, cantRecursos * 2, cantRecursos, gestorTiempo);
        Comedor comedor = new Comedor();

        int cantEncargados = 3;
        Thread[] hilosEncargados = new Thread[cantEncargados];
        EncargadorJuegos[] encargado = new EncargadorJuegos[cantEncargados];
        for (int i = 0; i < cantEncargados; i++) {
            Exchanger<String> exchanger = new Exchanger<>();
            encargado[i] = new EncargadorJuegos(exchanger);
            hilosEncargados[i] = new Thread(encargado[i], "Encargado " + i);
            hilosEncargados[i].start();
        }

        Tren tren = new Tren();
        Parque parque = new Parque(5, gestorTiempo, encargado, espacioVirtual, comedor, tren);
        gestorTiempo.setParque(parque);
        Thread hiloGestorTiempo = new Thread(gestorTiempo);

        Conductor conductor = new Conductor(tren);
        Thread hiloConductor = new Thread(conductor, "Conductor");
        hiloConductor.start();
        hiloGestorTiempo.start();

        int cantPersonas = 8;
        Thread[] hilos = new Thread[cantPersonas];

        for (int i = 0; i < cantPersonas; i++) {
            hilos[i] = new Thread(new Persona(parque, "Persona " + i), "Persona " + i);
            hilos[i].start();
        }

        try {
            for (Thread thread : hilos) {
                thread.join();
            }
            System.out.println("Simulación terminada.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
