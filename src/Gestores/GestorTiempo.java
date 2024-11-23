package Gestores;

public class GestorTiempo implements Runnable {
    private int hora;
    private int minuto;
    
    private GestorParque gestorParque;

    public GestorTiempo(int hora, int minuto, GestorParque gestorParque) {
        this.hora = hora;
        this.minuto = minuto;
        this.gestorParque = gestorParque;
    }

    /*
     * GestorTiempo y GestorParque
     * GestorTiempo corre siempre
     * y avisa a GestorParque en los horarios especiales para que abra, cierra.
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
                Thread.sleep(100);
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
                    System.out.println("Abriendo el parque");
                    //gestorParque.abrirParque();
                }
                if (hora == 18 && minuto == 0) {
                    // Cierra el parque
                    System.out.println("Cerrando el parque");
                    //gestorParque.cerrarParque();
                }
                if (hora == 19 && minuto == 0) {
                    // Cierra las atracciones
                    System.out.println("Cerrando las atracciones");
                    //gestorParque.cerrarAtracciones();
                }
                if (hora == 23 && minuto == 0) {
                    // No debería quedar nadie en el parque
                    System.out.println("El parque está vacío");
                    //gestorParque.vaciarParque();
                }
                System.out.println("Hora: " + hora + ":" + minuto);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
