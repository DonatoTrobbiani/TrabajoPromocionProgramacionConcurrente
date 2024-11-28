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
        EspacioVirtual espacioVirtual = new EspacioVirtual(2, 2, 2);
        Comedor comedor = new Comedor();
        Exchanger<String> exchanger = new Exchanger<>();
        Encargado encargado = new Encargado(exchanger);
        Thread hiloEncargado = new Thread(encargado);
        hiloEncargado.start();

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
            hilos[i] = new Thread(new Persona(parque, i), "Persona " + i);
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
