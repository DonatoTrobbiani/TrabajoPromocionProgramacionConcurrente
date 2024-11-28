package RealidadVirtual;

import App.Persona;
import Gestores.GestorTiempo;

public class EspacioVirtual {
    private int cantVR;
    private int cantManoplas;
    private int cantBases;
    private final GestorTiempo gestorTiempo;

    public EspacioVirtual(int cantVR, int cantManoplas, int cantBases, GestorTiempo gestorTiempo) {
        this.cantVR = cantVR;
        this.cantManoplas = cantManoplas;
        this.cantBases = cantBases;
        this.gestorTiempo = gestorTiempo;
    }

    public synchronized boolean entrarVR(Persona p) {
        boolean puedeEntrar = false, actividadValida = true;
        try {
            while (actividadValida) {
                if (gestorTiempo.getHora() >= 19) {
                    System.out.println(p.getNombre() + " no puede entrar a la Realidad Virtual porque ya es tarde.");
                    actividadValida = false;
                } else if (cantVR < 1 || cantManoplas < 2 || cantBases < 1) {
                    System.out.println(p.getNombre()
                            + " no puede entrar a la Realidad Virtual porque no hay suficientes recursos.");
                    wait();
                } else {
                    cantVR--;
                    cantManoplas -= 2;
                    cantBases--;
                    System.out.println(p.getNombre() + " entró a la Realidad Virtual");
                    puedeEntrar = true;
                    actividadValida = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return puedeEntrar;
    }

    public synchronized void salirVR() {
        cantVR++;
        cantBases++;
        cantManoplas+=2;
        notifyAll();
    }
}
