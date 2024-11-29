package Gestores;

import java.util.concurrent.atomic.AtomicInteger;

import App.*;

public class GestorTiempo implements Runnable {
    private AtomicInteger hora;
    private AtomicInteger minutos;
    private Parque parque;
    // ? puede mejorar con atomic boolean

    public GestorTiempo(Parque parque) {
        this.hora = new AtomicInteger(8);
        this.minutos = new AtomicInteger(50);
        this.parque = parque;
    }

    /*
     * GestorTiempo corre siempre
     * y realiza operaciones en horarios especiales para que abra, cierra.
     * El complejo se encuentra abierto para el ingreso de 09:00 a 18:00hs.
     * Considere que las actividades cierran a las 19.00 hrs. Y a las 23hs no
     * debería quedar nadie en el parque.
     */

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(500); // 1 segundo IRL, 2 minutos en el programa;
                // Una hora en el programa es 30 segundos IRL.
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
                }
                if (hora.get() == 18 && minutos.get() == 0) {
                    // Cierra el parque
                    this.cerrarParque();
                }
                if (hora.get() == 19 && minutos.get() == 0) {
                    // Cierra las atracciones
                    System.out.println(
                            "[GestorTiempo] Las atracciones han cerrado.");
                }
                if (hora.get() == 23 && minutos.get() == 0) {
                    System.out.println("[GestorTiempo] El parque ha cerrado, todos los visitantes deben retirarse.");
                }
                // System.out.println(hora + ":" + minutos);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void abrirParque() {
        System.out.println("[GestorTiempo] Abriendo el parque");
        parque.getSemaforoMolinetes().release(parque.getCantMolinetes());
    }

    private void cerrarParque() throws InterruptedException {
        System.out.println("[GestorTiempo] Cerrando el parque");
        parque.getSemaforoMolinetes().acquire(parque.getCantMolinetes());
    }

    public int getHora() {
        return hora.get();
    }

    public void setParque(Parque parque2) {
        this.parque = parque2;
    }
}
