package Comedor;

import java.util.concurrent.CyclicBarrier;

public class Comedor {
    private int cantMesas;
    private CyclicBarrier mesa = new CyclicBarrier(4);
    private int cantPersonas = 0;

    public Comedor(int cantMesas) {
        this.cantMesas = cantMesas;
    }

    public void entrarAComer(Persona persona) {
        if (cantPersonas < cantMesas*4) {
            try {
                System.out.println(persona.getName() + " entró al comedor y se sienta en una mesa.");
                mesa.await();
                cantPersonas++;
                System.out.println(persona.getName() + " empieza a comer.");
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            System.out.println("El comedor estaba lleno asi que "+ persona.getName() + " no entra al comedor y se va.");
            persona.noEntro();
        }
    }

    public void salirDelComedor(Persona persona) {
        cantPersonas--;
        System.out.println(persona.getName() + " terminó de comer y se va del comedor.");
    }
}
