package Comedor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import App.Persona;

/**
 * Clase que representa el comedor del parque.
 * <p>
 * Cuenta con un método para que las personas puedan entrar al comedor, un
 * método para asignarles una mesa junto a otras personas y un método para que
 * puedan salir del comedor.
 * <p>
 * Como mecanismo de sincronización, se utiliza un CyclicBarrier para que las
 * personas comiencen a comer en la mesa una vez que estén todas sentadas. A
 * esto se le suma un ArrayList para almacenar las personas que se encuentran en
 * la mesa y un AtomicInteger para llevar la cuenta de las personas en el
 * comedor.
 * 
 * @author Gianfranco Gallucci, FAI-3824
 * @author Donato Trobbiani Perales, FAI-4492
 */
public class Comedor {
    private static final int NUM_PERSONAS_POR_MESA = 4;
    private static final int NUM_CAPCACIDAD_COMEDOR = NUM_PERSONAS_POR_MESA * 3;
    private AtomicInteger personasEnComedor = new AtomicInteger(0);
    private List<Persona> personasEnMesa = new ArrayList<>(4);
    private final CyclicBarrier mesasCyclicBarrier = new CyclicBarrier(NUM_PERSONAS_POR_MESA, new Runnable() {
        @Override
        public void run() {
            // Método run del Runnable que se ejecuta cuando se completa una mesa.
            System.out.println("Mesa completa. Todos comienzan a comer.");
            System.out.println("Personas en la mesa: " + personasEnMesa.toString());
        }
    });

    /**
     * Método que permite saber si una persona puede entrar al comedor.
     * Funcion incrementando el contador de personas en el comedor y comparando si
     * la cantidad de personas es menor o igual a la capacidad del comedor.
     * 
     * @return boolean
     */
    public boolean entrarComedor() {
        return personasEnComedor.incrementAndGet() <= NUM_CAPCACIDAD_COMEDOR;
    }

    /**
     * Método que utiliza una persona para actualizar la cantidad de personas en el
     * comedor.
     */
    public void salirComedor() {
        personasEnComedor.decrementAndGet();
    }

    /**
     * Método que permite a una persona sentarse en una mesa del comedor.
     * 
     * @param p
     * @return boolean
     */
    public boolean sentarseEnMesa(Persona p) {
        boolean exito = false; // Indica si la persona logró sentarse en la mesa.
        int intentos = 0; // Cantidad de intentos de sentarse en la mesa.
        while (!exito && intentos < 3) {
            try {
                // 1. Se agrega la persona a la mesa actual.
                personasEnMesa.add(p);
                System.out.println("[COM] " + p.getNombre() + " se ha sentado en la mesa.");
                // 2. Se espera a que se complete la mesa.
                mesasCyclicBarrier.await(5, TimeUnit.SECONDS);
                exito = true;
            } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                if (e instanceof TimeoutException) {
                    // 2.1 Si se produce un TimeoutException, se indica que la persona se cansó de
                    // esperar.
                    System.out.println("[COM] " + p.getNombre() + " se cansó de esperar.");
                } else if (e instanceof BrokenBarrierException) {
                    // 2.2 Si se produce una BrokenBarrierException, se indica que la persona se
                    // cansó de esperar.
                    // Ocurre cuando un hilo que espera en la barrera es interrumpido.
                    System.out.println("[COM] " + p.getNombre() + " se cansó de esperar.");
                } else {
                    e.printStackTrace();
                }
                intentos++;
                mesasCyclicBarrier.reset();
                // Se resetea la barrera (todos los hilos esperando en la barrera son
                // liberados).
                System.out.println("[COM] " + p.getNombre() + " está intentando nuevamente.");
            } finally {
                personasEnMesa.remove(p); // Se quita a la persona de la mesa.
            }
        }
        return exito;
    }
}
