package TrenesDelParque;

import java.util.concurrent.*;

public class Persona implements Runnable {
    private String nombre;
    private final BlockingQueue<Persona> queue;

    public Persona(String nombre, BlockingQueue<Persona> queue) {
        this.nombre = nombre;
        this.queue = queue;
    }

    public void run() {
        try {
            if (queue.offer(this, 2, TimeUnit.SECONDS)) {
                System.out.println("Persona " + nombre + " entró a la cola para subirse al tren");
            } else {
                System.out.println("Persona " + nombre + " vio que la cola para el tren estaba llena y se fue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return nombre;
    }
}
