package Comedor.Comedor2;

public class Persona2 implements Runnable {
    private String nombre;
    private Comedor2 comedor;
    private boolean entro = true;

    public Persona2(String nombre, Comedor2 comedor) {
        this.nombre = nombre;
        this.comedor = comedor;
    }

    public void run() {
        try {
            comedor.entrarComedor();
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " salió del comedor.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return nombre;
    }

    public void noEntro() {
        entro = false;
    }
}
