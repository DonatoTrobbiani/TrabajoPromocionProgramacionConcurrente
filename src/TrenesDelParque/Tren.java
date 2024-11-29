package TrenesDelParque;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import App.Persona;

public class Tren implements Runnable {
    private final BlockingQueue<Persona> queue;
    private static final int CAPACIDAD = 10;
    private static final long TIEMPO_ESPERA = 10000;

    public Tren(BlockingQueue<Persona> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            long tiempoDeSalida = System.currentTimeMillis() + TIEMPO_ESPERA;
            List<Persona> pasajeros = new ArrayList<>();
            try {
                Persona primerPasajero = queue.poll(TIEMPO_ESPERA, TimeUnit.MILLISECONDS);
                if (primerPasajero != null) {
                    pasajeros.add(primerPasajero);
                    System.out.println(primerPasajero.getNombre() + " se subió al tren");
                    while (System.currentTimeMillis() < tiempoDeSalida && pasajeros.size() < CAPACIDAD) {
                        Persona siguientePasajero = queue.poll(tiempoDeSalida - System.currentTimeMillis(),
                                TimeUnit.MILLISECONDS);
                        if (siguientePasajero != null) {
                            pasajeros.add(siguientePasajero);
                            System.out.println(siguientePasajero.getNombre() + " se subió al tren");
                        }
                    }
                }
                if (!pasajeros.isEmpty()) {
                    System.out.println("El tren se va con " + pasajeros.size() + " pasajeros");
                    this.salirTren(pasajeros);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BlockingQueue<Persona> getQueue() {
        return queue;
    }

    private void salirTren(List<Persona> pasajeros) throws InterruptedException {
        Thread.sleep(10000);
    }
}
