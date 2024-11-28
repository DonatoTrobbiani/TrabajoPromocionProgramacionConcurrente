package Gestores;

import java.util.concurrent.atomic.AtomicInteger;

import App.*;

public class GestorTiempo implements Runnable {
    private AtomicInteger hora;
    private AtomicInteger minutos;
    private Parque parque;

    public GestorTiempo(Parque parque) {
        this.hora = new AtomicInteger(8);
        this.minutos = new AtomicInteger(0);
        this.parque = parque;
    }

    /*
     * GestorTiempo corre siempre
     * y realiza operaciones en horarios especiales para que abra, cierra.
     * El complejo se encuentra abierto para el ingreso de 09:00 a 18:00hs.
     * Considere que las actividades cierran a las 19.00 hrs. Y a las 23hs no
     * debería quedar nadie en el parque.
     * 
     * Hay que resolver el cuello de los k molinetes, con semaforos entra, toma su
     * tiempo y libera su permiso.
     */

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(200);
                minutos.incrementAndGet();
                if (minutos.get() == 60) {
                    hora.incrementAndGet();
                    minutos.set(0);
                }
                if (hora.get() == 24) {
                    hora.set(0);
                }
                if (hora.get() == 9 && minutos.get() == 0) {
                    // Abre el parque
                    this.abrirParque();
                    System.out.println("Permisos molinetes abrir: " + parque.getSemaforoMolinetes().availablePermits());
                }
                if (hora.get() == 18 && minutos.get() == 0) {
                    // Cierra el parque
                    System.out.println("Cerrando el parque...");
                    this.cerrarParque();
                    System.out
                            .println("Permisos molinetes cerrar: " + parque.getSemaforoMolinetes().availablePermits());
                }
                if (hora.get() == 19 && minutos.get() == 0) {
                    // Cierra las atracciones
                    System.out.println("Cerrando las atracciones");
                    // this.cerrarAtracciones();
                }
                if (hora.get() == 23 && minutos.get() == 0) {
                    // No debería quedar nadie en el parque
                    System.out.println("El parque está vacío");
                    // this.vaciarParque();
                }
                System.out.println("Hora: " + hora + ":" + minutos);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void abrirParque() {
        System.out.println("Abriendo el parque");
        parque.getSemaforoMolinetes().release(parque.getCantMolinetes());
    }

    private void cerrarParque() throws InterruptedException {
        System.out.println("Cerrando el parque");
        parque.getSemaforoMolinetes().acquire(parque.getCantMolinetes());
    }

    public int getHora() {
        return hora.get();
    }

    public void setParque(Parque parque2) {
        this.parque = parque2;
    }
}
