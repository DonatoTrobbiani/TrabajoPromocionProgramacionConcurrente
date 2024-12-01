package TrenesDelParque;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import App.Persona;

public class Conductor implements Runnable {
    private final Tren tren;
    private final BlockingQueue<Persona> colaPasajeros;

    public Conductor(Tren tren) {
        this.tren = tren;
        colaPasajeros = tren.getQueue();
    }

    /**
     * Método run de la clase Conductor.
     * Se encarga de llevar el tren por el recorrido del parque.
     * <p>
     * 1. Espera a que llegue un pasajero.
     * <p>
     * 2. Empieza a contar el tiempo de espera.
     * <p>
     * 3. Si llegan más pasajeros, los acepta hasta que se llene el tren o pase el
     * tiempo de espera.
     * <p>
     * 4. Inicia el recorrido y espera a que termine.
     * <p>
     * 5. Despierta a los pasajeros, modificando el estado del tren y vuelve a
     * esperar.
     */
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("[TR_C]: Esperando pasajeros");
                colaPasajeros.take(); // se bloquea hasta que llegue un pasajero
                int cantidadDePasajeros = 1;
                // Comienza a contar el tiempo de partida
                long tiempoDeEspera = System.currentTimeMillis() + 10000;
                boolean esperandoPasajeros = true;
                // Acepta pasajeros hasta que se llene el tren o pase el tiempo de espera
                while (esperandoPasajeros && cantidadDePasajeros < 10) {
                    long tiempoRestante = tiempoDeEspera - System.currentTimeMillis();
                    if (tiempoRestante <= 0) {
                        // Se acabó el tiempo de espera
                        esperandoPasajeros = false;
                    } else {
                        // Espera a que llegue un pasajero o se acabe el tiempo
                        Persona pasajero = colaPasajeros.poll(tiempoRestante, TimeUnit.MILLISECONDS);
                        if (pasajero != null) {
                            // System.out.println("[TR_C]: Aceptando pasajero " + pasajero.getNombre());
                            cantidadDePasajeros++;
                        } else {
                            // Timeout, no llegaron más pasajeros
                            esperandoPasajeros = false;
                        }
                    }
                }
                tren.setEstadoTren(true);
                // Simula el recorrido
                System.out.println("[TR_C]: Iniciando recorrido, no acepta más pasajeros.");
                Thread.sleep(10000);
                System.out.println("[TR_C]: Finalizando recorrido, pasajeros a desbordar en unos minutos.");
                // llegó al recorrido, despierta a todos los pasajeros, limpia la cola por
                // seguridad y se pone en espera.
                Thread.sleep(1250);// 5 minutos
                tren.reiniciarTren();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
