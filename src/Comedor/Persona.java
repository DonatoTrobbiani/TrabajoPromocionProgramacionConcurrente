package Comedor;

public class Persona implements Runnable {
    private String nombre;
    private Comedor comedor;

    public Persona(String nombre, Comedor comedor) {
        this.nombre = nombre;
        this.comedor = comedor;
    }
    
    public void run() {
        try {
            comedor.entrarAComer(this);
            Thread.sleep((int) Math.random() * 1000);
            comedor.salirDelComedor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return nombre;
    }
}
