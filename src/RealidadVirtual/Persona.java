package RealidadVirtual;

public class Persona implements Runnable {
    private String nombre;
    private EspacioVirtual espacio;

    public Persona(String nombre, EspacioVirtual espacio) {
        this.nombre = nombre;
        this.espacio = espacio;
    }
    
    public void run() {
        try {
            System.out.println(nombre + " quiere pobrar la realidad virtual.");
            espacio.entrarVR(nombre);
            System.out.println("Comienza la simulación.");
            Thread.sleep((int)Math.random() * 2000);
            System.out.println("Terminó la simulación.");
            espacio.salirVR();
            System.out.println(nombre + " abandona la atracción de realidad virtual.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
