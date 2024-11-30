package Gestores;

import java.util.concurrent.atomic.AtomicInteger;

import App.*;

/**
 * Clase que se encarga de llevar el tiempo del parque.
 * <p>
 * GestorTiempo es un hilo que corre siempre y realizar operanciones en horarios
 * especiales para que abra el parque, lo cierre, etc.
 * <p>
 * El complejo se encuentra abierto para el ingreso de 09:00 a 18:00hs.
 * Las actividades cierran a las 19.00 hrs. Y a las 23hs no debería quedar nadie
 * en el parque.
 * 
 * @author Gianfranco Gallucci, FAI-3824
 * @author Donato Trobbiani Perales, FAI-4492
 * 
 */
public class GestorTiempo implements Runnable {
    private AtomicInteger hora;
    private AtomicInteger minutos;
    private Parque parque;
    private VentanaTiempo ventanaTiempo;

    /**
     * Constructor de la clase GestorTiempo.
     * Inicializa la hora en la que comenzará el ciclo del parque.
     * 
     * @param parque Parque al que pertenece el gestor de tiempo.
     */
    public GestorTiempo(Parque parque) {
        this.hora = new AtomicInteger(8);
        this.minutos = new AtomicInteger(50);
        this.parque = parque;

        this.ventanaTiempo = new VentanaTiempo(hora, minutos);
        this.ventanaTiempo.start();
    }

    /**
     * Método run de la clase GestorTiempo.
     * Se encarga de llevar el tiempo del parque y realizar las acciones necesarias.
     * Un segundo en la vida real equivale a 2 minutos en el programa (1000ms).
     * Una hora en el programa equivale a 30 segundos en la vida real (30000ms).
     */
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(500);
                minutos.incrementAndGet();
                this.avanzaHora();// Avanza la hora si es necesario
                this.avanzarDia();// Avanza el día si es necesario
                this.abrirParque();// Abre el parque si es necesario
                this.cerrarParque();// Cierra el parque si es necesario
                this.mensajesRestantes(); // Muestra mensajes restantes
                // System.out.println(hora + ":" + minutos); // Debug
            }
        } catch (Exception e) {
            // TODO: Implementar manejo de excepción
        }
    }

    /**
     * Método que avanza la hora si los minutos llegan a 60.
     */
    private void avanzaHora() {
        if (minutos.get() == 60) {
            hora.incrementAndGet();
            minutos.set(0);
        }
    }

    /**
     * Método que avanza el día si la hora llega a 24.
     */
    private void avanzarDia() {
        if (hora.get() == 24) {
            hora.set(0);
        }
    }

    /**
     * Método que abre el parque a las 9:00.
     * Libera los molinetes (semáforos) para que los visitantes puedan ingresar.
     */
    private void abrirParque() {
        if (hora.get() == 9 && minutos.get() == 0) {
            System.out.println("[GestorTiempo] Abriendo el parque");
            parque.getSemaforoMolinetes().release(parque.getCantMolinetes());
        }
    }

    /**
     * Método que cierra el parque a las 18:00.
     * Bloquea los molinetes (semáforos) para que los visitantes no puedan ingresar.
     * 
     * @throws InterruptedException
     */
    private void cerrarParque() throws InterruptedException {
        if (hora.get() == 18 && minutos.get() == 0) {
            System.out.println("[GestorTiempo] Cerrando el parque");
            parque.getSemaforoMolinetes().acquire(parque.getCantMolinetes());
        }
    }

    /**
     * Método que muestra mensajes restantes de las 19 y 23 horas.
     */
    private void mensajesRestantes() {
        if (hora.get() == 19 && minutos.get() == 0) {
            // Cierra las atracciones
            System.out.println(
                    "[GestorTiempo] Las atracciones han cerrado.");
        }
        if (hora.get() == 23 && minutos.get() == 0) {
            // Avisa que se retiren los visitantes
            System.out.println("[GestorTiempo] El parque ha cerrado, todos los visitantes deben retirarse.");
        }
    }

    /*
     * Getters y Setters
     */
    public int getHora() {
        return hora.get();
    }

    public void setHora(AtomicInteger hora) {
        this.hora = hora;
    }

    public AtomicInteger getMinutos() {
        return minutos;
    }

    public void setMinutos(AtomicInteger minutos) {
        this.minutos = minutos;
    }

    public Parque getParque() {
        return parque;
    }

    public void setParque(Parque parque2) {
        this.parque = parque2;
    }
}
