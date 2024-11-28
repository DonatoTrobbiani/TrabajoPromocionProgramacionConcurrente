package App;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;

import AreaDeJuegosDePremios.Encargado;
import Comedor.Comedor;
import Gestores.GestorTiempo;
import RealidadVirtual.EspacioVirtual;
import TrenesDelParque.Tren;

public class TestPersonas {
    public static void main(String[] args) {
        GestorTiempo gestorTiempo = new GestorTiempo(null);
        int cantRecursos = 5;
        EspacioVirtual espacioVirtual = new EspacioVirtual(cantRecursos,cantRecursos*2,cantRecursos, gestorTiempo);
        Comedor comedor = new Comedor();

        int cantEncargados = 3;
        Thread[] hilosEncargados = new Thread[cantEncargados];
        Encargado[] encargado = new Encargado[cantEncargados];
        for (int i = 0; i < cantEncargados; i++) {
            Exchanger<String> exchanger = new Exchanger<>();
            encargado[i] = new Encargado(exchanger);
            hilosEncargados[i] = new Thread(encargado[i], "Encargado " + i);
            hilosEncargados[i].start();
        }

        BlockingQueue<Persona> queue = new ArrayBlockingQueue<>(10);
        Tren tren = new Tren(queue);
        Thread hiloTren = new Thread(tren);
        hiloTren.start();
        Parque parque = new Parque(1, gestorTiempo, encargado, espacioVirtual, comedor, tren);
        gestorTiempo.setParque(parque);
        Thread hiloGestorTiempo = new Thread(gestorTiempo);

        hiloGestorTiempo.start();

        int cantPersonas = 20;
        Thread[] hilos = new Thread[cantPersonas];

        for (int i = 0; i < cantPersonas; i++) {
            hilos[i] = new Thread(new Persona(parque, "Persona " + i), "Persona " + i);
            hilos[i].start();
        }

        try {
            for (Thread thread : hilos) {
                thread.join();
            }
            hiloGestorTiempo.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
