package Comedor;

public class Persona implements Runnable {
    private String nombre;
    private Comedor comedor;
    private boolean entro = true;

    public Persona(String nombre, Comedor comedor) {
        this.nombre = nombre;
        this.comedor = comedor;
    }
    
    public void run() {
        try {
            comedor.entrarAComer(this);
            if (entro) {
                Thread.sleep((int) Math.random() * 1000);
                comedor.salirDelComedor(this);
            }
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
