package Gestores;

import App.*;

public class GestorTiempo implements Runnable {
    private int hora;
    private int minuto;
    private Parque parque;

    public GestorTiempo(int hora, int minuto, Parque parque) {
        if (hora < 0 || hora > 23 || minuto < 0 || minuto > 59) {
            this.hora = 0;
            this.minuto = 0;
        } else {
            this.hora = hora;
            this.minuto = minuto;
        }
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
                minuto++;
                if (minuto == 60) {
                    hora++;
                    minuto = 0;
                }
                if (hora == 24) {
                    hora = 0;
                }
                if (hora == 9 && minuto == 0) {
                    // Abre el parque
                    this.abrirParque();
                    System.out.println("Permisos molinetes abrir: " + parque.getSemaforoMolinetes().availablePermits());
                }
                if (hora == 18 && minuto == 0) {
                    // Cierra el parque
                    this.cerrarParque();
                    System.out
                            .println("Permisos molinetes cerrar: " + parque.getSemaforoMolinetes().availablePermits());
                }
                if (hora == 19 && minuto == 0) {
                    // Cierra las atracciones
                    System.out.println("Cerrando las atracciones");
                    // gestorParque.cerrarAtracciones();
                }
                if (hora == 23 && minuto == 0) {
                    // No debería quedar nadie en el parque
                    System.out.println("El parque está vacío");
                    // gestorParque.vaciarParque();
                }
                System.out.println("Hora: " + hora + ":" + minuto);
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

}
