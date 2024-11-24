package App;
import java.util.concurrent.CyclicBarrier;

public class App {
    public static void main(String[] args) throws Exception {
        int numeroHilos = 10; //10 hilos
        final CyclicBarrier barreraInicio = new CyclicBarrier(numeroHilos + 1); //barrera de 11 hilos
        final CyclicBarrier barreraFin = new CyclicBarrier(numeroHilos + 1); //barrera de 11 hilos
        for (int i = 0; i < numeroHilos; i++) {
            Thread hilo = new Thread() {
            public void run() {
                try {
                    barreraInicio.await();
                    System.out.println(this.getName());
                    barreraFin.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            };
            hilo.setName(i + "");
            hilo.start();
        }
        try {
            System.out.println("levanto barrera");
            barreraInicio.await();
            barreraFin.await();
            System.out.println("todo acabado");
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}
