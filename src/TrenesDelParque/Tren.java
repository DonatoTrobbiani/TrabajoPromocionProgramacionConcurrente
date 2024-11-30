package TrenesDelParque;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import App.Persona;

public class Tren {
    private final BlockingQueue<Persona> queue = new ArrayBlockingQueue<>(CAPACIDAD);
    private AtomicBoolean trenEnMovimiento = new AtomicBoolean(false);
    private static final int CAPACIDAD = 10;

    /**
     * Método que utiliza una persona para subir al tren.
     * Consulta inicialmente si el tren está en movimiento, si no lo está, intenta
     * subir a la cola. Si la cola está llena, no puede subir.
     * 
     * @param p
     * @return boolean
     * @throws InterruptedException
     */
    public synchronized boolean subirTren(Persona p) throws InterruptedException {
        boolean respuesta = false;
        if (this.isTrenEnMovimiento()) {
            System.out.println("[TR_P] " +
                    p.getNombre() + " quiso subir al tren, pero estaba en movimiento.");
        } else {
            if (queue.offer(p)) {
                System.out.println("[TR_P] " + p.getNombre() +
                        " se subió al tren.");
                // Logró subirse, se duerme en el cjto. de espera
                wait();
                respuesta = true;
            } else {
                System.out.println("[TR_P] " + p.getNombre() +
                        " quiso subir al tren, pero la cola estaba llena.");
            }
        }
        return respuesta;
    }

    /**
     * Método que utiliza el condutor para reiniciar el tren una vez que llega al
     * final del recorrido.
     * Notifica a todos los pasajeros que llegaron al final del recorrido,
     * reinicia la cola y pone el tren en estado de espera.
     */
    public synchronized void reiniciarTren() {
        notifyAll();
        queue.clear();
        this.setEstadoTren(false);
    }

    public BlockingQueue<Persona> getQueue() {
        return queue;
    }

    public boolean isTrenEnMovimiento() {
        return trenEnMovimiento.get();
    }

    public void setEstadoTren(boolean estado) {
        this.trenEnMovimiento.set(estado);
    }
}
