package Comedor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import App.Persona;

public class Comedor {
    private static final int CAPACIDAD_MESA = 4;
    private static final int CAPACIDAD_COMEDOR = CAPACIDAD_MESA * 3;
    private final CyclicBarrier barreraMesa;
    private final AtomicInteger personasEnComedor;
    private final AtomicInteger mesaActual;
    private final List<Mesa> mesas;

    public Comedor() {
        this.personasEnComedor = new AtomicInteger(0);
        this.mesas = new ArrayList<>();
        this.mesaActual = new AtomicInteger(0);
        for (int i = 0; i < CAPACIDAD_COMEDOR / CAPACIDAD_MESA; i++) {
            mesas.add(new Mesa(i));
        }
        this.barreraMesa = new CyclicBarrier(CAPACIDAD_MESA, () -> {
            Mesa mesa = mesas.get(mesaActual.get());
            System.out.println("La mesa " + mesa.getNumero() + " está llena. Comiendo...");
            System.out.println("Personas en la mesa " + mesa.getNumero() + ": " + mesa.getPersonas());
            mesa.limpiarMesa();
            mesaActual.set((mesaActual.get() + 1) % mesas.size());// Rotamos las mesas
        });
    }

    public boolean entrarComedor(Persona p) {
        boolean respuesta = true;
        if (personasEnComedor.incrementAndGet() > CAPACIDAD_COMEDOR) {
            System.out.println(p.getNombre() + " no retiró al comedor, está lleno.");
            personasEnComedor.decrementAndGet();
        } else {
            System.out.println(p.getNombre() + " entró al comedor");
            try {
                int mesaIndex = mesaActual.get() % mesas.size();
                Mesa mesa = mesas.get(mesaIndex);
                mesa.agregarPersona(p.getNombre());
                System.out.println(p.getNombre() + " se sentó en la mesa " + mesa.getNumero());
                barreraMesa.await(15000, TimeUnit.MILLISECONDS);
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
