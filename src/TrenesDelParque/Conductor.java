package TrenesDelParque;

import java.util.concurrent.BlockingQueue;

import App.Persona;

public class Conductor implements Runnable {
    private final Tren tren;
    private final BlockingQueue<Persona> colaPasajeros;

    // buto
    public Conductor(Tren tren) {
        this.tren = tren;
        colaPasajeros = tren.getCola();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("[TR_C]: Esperando pasajeros");
                colaPasajeros.take(); // se bloquea hasta que haya un pasajero
                // espera 5 minutos o 10 pasajeros
                long tiempoDeEspera = System.currentTimeMillis() + 10000;
                int cantidadDePasajeros = 0;
                while (System.currentTimeMillis() < tiempoDeEspera && cantidadDePasajeros < 9) {
                    if (colaPasajeros.size() > 0) {
                        colaPasajeros.take();
                        //Persona pasajero = colaPasajeros.take();
                        //System.out.println("[TR_C]: Pasajero subiendo al tren: " + pasajero.getNombre());
                        cantidadDePasajeros++;
                    }
                }
                tren.setEstadoTren(true);
                // Simula el recorrido
                System.out.println("[TR_C]: Iniciando recorrido");
                Thread.sleep(10000);
                System.out.println("[TR_C]: Finalizando recorrido");
                // llegó al recorrido, despierta a todos los pasajeros, limpia la cola por
                // seguridad y se pone en espera.
                Thread.sleep(1000);
                System.out.println("[TR_C]: Despertando pasajeros");
                tren.reiniciarTren();
                colaPasajeros.clear();
                tren.setEstadoTren(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
