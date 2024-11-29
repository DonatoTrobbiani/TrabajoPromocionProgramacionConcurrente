package TrenesDelParque;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;

import App.Persona;

public class Tren {
    private final BlockingQueue<Persona> queue = new ArrayBlockingQueue<>(CAPACIDAD);
    private AtomicBoolean trenEnMovimiento = new AtomicBoolean(false);
    private static final int CAPACIDAD = 10;
    private static final long TIEMPO_ESPERA = 10000;

    public BlockingQueue<Persona> getQueue() {
        return queue;
    }

    public void saleTren() {
        long tiempoDeSalida = System.currentTimeMillis() + TIEMPO_ESPERA;

    }

    public boolean isTrenEnMovimiento() {
        return trenEnMovimiento.get();
    }

    public BlockingQueue<Persona> getCola() {
        return queue;
    }

    public void setEstadoTren(boolean estado) {
        this.trenEnMovimiento.set(estado);
    }

    public synchronized boolean subirTren(Persona p) throws InterruptedException {
        boolean respuesta = false;
        if (this.isTrenEnMovimiento()) {
            System.out.println("[TR] El tren está en movimiento, " +
                    p.getNombre() + " no pudo subirse.");
        } else {
            if (queue.offer(p)) {
                System.out.println("[TR] " + p.getNombre() +
                        " entró a la cola para subirse al tren.-----------------");
                // Logró subirse, se duerme en el cjto. de espera
                wait();
                respuesta = true;
            } else {
                System.out.println("[TR]" + p.getNombre() +
                        " vio que la cola para el tren estaba llena y se fue.");
            }
        }
        return respuesta;
    }

    public synchronized void reiniciarTren() {
        notifyAll();
    }

    /*
     * @Override
     * public void run() {
     * while (true) {
     * long tiempoDeSalida = System.currentTimeMillis() + TIEMPO_ESPERA;
     * List<Persona> pasajeros = new ArrayList<>();
     * try {
     * Persona primerPasajero = queue.poll(TIEMPO_ESPERA, TimeUnit.MILLISECONDS);
     * if (primerPasajero != null) {
     * pasajeros.add(primerPasajero);
     * System.out.println(primerPasajero.getNombre() + " se subió al tren");
     * while (System.currentTimeMillis() < tiempoDeSalida && pasajeros.size() <
     * CAPACIDAD) {
     * Persona siguientePasajero = queue.poll(tiempoDeSalida -
     * System.currentTimeMillis(),
     * TimeUnit.MILLISECONDS);
     * if (siguientePasajero != null) {
     * pasajeros.add(siguientePasajero);
     * System.out.println(siguientePasajero.getNombre() + " se subió al tren");
     * }
     * }
     * }
     * if (!pasajeros.isEmpty()) {
     * System.out.println("El tren se va con " + pasajeros.size() + " pasajeros");
     * this.salirTren(pasajeros);
     * }
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     * }
     */

    public boolean abordarTren(Persona p) throws InterruptedException {
        boolean respuesta = false;
        if (queue.offer(p, 2, TimeUnit.SECONDS)) {
            System.out.println(p.getNombre() +
                    " entró a la cola para subirse al tren");
            respuesta = true;
        } else {
            System.out.println(p.getNombre() +
                    " vio que la cola para el tren estaba llena y se fue");
        }
        return respuesta;
    }

    /*
     * private void salirTren(List<Persona> pasajeros) throws InterruptedException {
     * Thread.sleep(10000);
     * }
     */
}
