package Comedor;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Comedor {
    private int cantMesas;
    private CyclicBarrier[] mesas;
    private AtomicInteger cantPersonas = new AtomicInteger(0);

    public Comedor(int cantMesas) {
        this.cantMesas = cantMesas;
        this.mesas = new CyclicBarrier[cantMesas];
        for (int i = 0; i < cantMesas; i++) {
            mesas[i] = new CyclicBarrier(4);
        }
    }

    public void entrarAComer(Persona persona) {
        if (cantPersonas.get() < cantMesas * 4) {
            int mesaDisponible = obtenerMesaDisponible();
            if (mesaDisponible != -1) {
                try {
                    System.out.println(persona.getName() + " entró al comedor y se sienta en una mesa.");
                    cantPersonas.incrementAndGet();
                    mesas[mesaDisponible].await();
                    System.out.println(persona.getName() + " empieza a comer.");
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else {
                System.out.println("No hay mesas disponibles para " + persona.getName());
                persona.noEntro();
            }
        } else {
            System.out.println("El comedor estaba lleno asi que " + persona.getName() + " no entra al comedor y se va.");
            persona.noEntro();
        }
    }

    public void salirDelComedor(Persona persona) {
        try {
            cantPersonas.decrementAndGet();
            System.out.println(persona.getName() + " terminó de comer y se va del comedor.");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public int obtenerMesaDisponible() {
        int mesa = -1;
        for (int i = 0; i < cantMesas; i++) {
            if (mesas[i].getNumberWaiting() < 4) {
                mesa = i;
            }
        }
        return mesa;
    }
}
