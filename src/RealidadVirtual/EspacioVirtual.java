package RealidadVirtual;

public class EspacioVirtual {
    private int cantVR;
    private int cantManoplas;
    private int cantBases;

    public EspacioVirtual(int cantVR, int cantManoplas, int cantBases) {
        this.cantVR = cantVR;
        this.cantManoplas = cantManoplas;
        this.cantBases = cantBases;
    }

    public synchronized void entrarVR(String nombre) {
        try {

            if (cantVR < 1 || cantManoplas < 2 || cantBases < 1) {
                System.out.println(
                        nombre + " no puedo empezar la simulación ya que no hay equipos suficientes. Esperando...");
                wait();
            }

            cantVR--;
            cantBases--;
            cantManoplas--;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void salirVR() {
        cantVR++;
        cantBases++;
        cantManoplas++;
        notifyAll();
    }
}
