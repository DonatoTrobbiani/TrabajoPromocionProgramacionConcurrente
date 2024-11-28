package Comedor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import App.Persona;

public class Comedor {
    private static final int CAPACIDAD_MESA = 4;
    private static final int CAPACIDAD_COMEDOR = 12;
    private final CyclicBarrier barreraMesa;
    private final AtomicInteger personasEnMesa;
    private final AtomicInteger personasEnComedor;

    public Comedor() {
        this.barreraMesa = new CyclicBarrier(CAPACIDAD_MESA, () -> {
            System.out.println("La mesa está llena. Comiendo...");
        });
        this.personasEnMesa = new AtomicInteger(0);
        this.personasEnComedor = new AtomicInteger(0);
    }

    public boolean entrarComedor(Persona p) {
        boolean respuesta = true;
        // ! Podemos hacer clase mesa para denotar en cuál mesa se sientan las personas.
        if (personasEnComedor.incrementAndGet() > CAPACIDAD_COMEDOR) {
            System.out.println(p.getNombre() + " no retiró al comedor, está lleno.");
            personasEnComedor.decrementAndGet();
        } else {
            System.out.println(p.getNombre() + " entró al comedor");
            try {
                System.out.println(p.getNombre() + " se sentó en la mesa.");

                if (personasEnMesa.incrementAndGet() == CAPACIDAD_MESA) {
                    personasEnMesa.set(0);
                }
                barreraMesa.await(5, TimeUnit.SECONDS);
            } catch (TimeoutException | BrokenBarrierException e) {
                System.out.println(p.getNombre() + " se retiró del comedor, se cansó de esperar.");
                respuesta = false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                personasEnComedor.decrementAndGet();
            }
        }
        return respuesta;
    }
}
