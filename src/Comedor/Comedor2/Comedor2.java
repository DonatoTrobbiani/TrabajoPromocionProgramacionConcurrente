package Comedor.Comedor2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Comedor2 {
    private static final int CAPACIDAD_MESA = 4;
    private static final int CAPACIDAD_COMEDOR = 12;
    private final CyclicBarrier barreraMesa;
    private final AtomicInteger personasEnMesa;
    private final AtomicInteger personasEnComedor;

    public Comedor2() {
        this.barreraMesa = new CyclicBarrier(CAPACIDAD_MESA, () -> {
            System.out.println("La mesa está llena. Comiendo...");
        });
        this.personasEnMesa = new AtomicInteger(0);
        this.personasEnComedor = new AtomicInteger(0);
    }

    public void entrarComedor() {
        if (personasEnComedor.incrementAndGet() > CAPACIDAD_COMEDOR) {
            System.out.println(Thread.currentThread().getName() + " no retiró al comedor, está lleno.");
            personasEnComedor.decrementAndGet();
        } else {
            System.out.println(Thread.currentThread().getName() + " entró al comedor");
            try {
                int genteEnMesaActual = personasEnMesa.incrementAndGet();
                System.out.println(Thread.currentThread().getName() + " se sentó en la mesa.");

                if (genteEnMesaActual == CAPACIDAD_MESA) {
                    personasEnMesa.set(0);
                }
                barreraMesa.await(5, TimeUnit.SECONDS);
            } catch (TimeoutException | BrokenBarrierException e) {
                System.out.println(Thread.currentThread().getName() + " se retiró del comedor, se cansó de esperar.");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                personasEnComedor.decrementAndGet();
            }
        }
    }

    public boolean sentarseEnMesa() {

    }
}
