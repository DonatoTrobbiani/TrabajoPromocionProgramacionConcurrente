package AreaDeJuegosDePremios;

import java.util.concurrent.Exchanger;

/**
 * Clase que representa al encargado de juegos de premios, implementa
 * {@link Runnable}.
 * <p>
 * Cuenta con un exchanger para recibir y entregar fichas de los jugadores y una
 * variable booleana para controlar si está ocupado o no.
 * 
 * @author Gianfranco Gallucci FAI-3824
 * @author Donato Trobbiani Perales FAI-4492
 */
public class EncargadoJuegos implements Runnable {
    private Exchanger<String> exchanger = new Exchanger<>();
    private boolean ocupado = false;
    private int puntos;

    public EncargadoJuegos(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    /**
     * Método run de la clase EncargadoJuegos.
     * <p>
     * Se encarga de recibir la ficha del jugador, simular el juego y entregar el
     * premio al visitante.
     * <p>
     * Obtiene una ficha del jugador (que contiene su nombre para uso posterior),
     * simula el juego, seleccionando un premio según la cantidad de puntos que
     * calculó al azar.
     * Luego, entrega el premio al visitante.
     */
    public void run() {
        try {
            while (true) {
                // 1. El encargado recibe la ficha
                String jugadorFicha = exchanger.exchange(null);
                this.cambiarEstado();
                Thread.sleep(500);
                System.out.println("[JU] Encargado: Recibí una ficha de: " + jugadorFicha + ".");

                // 2. Simular el juego
                Thread.sleep(5000);
                String premio = this.calcularPremio();

                // 3. Entregar el premio al visitante
                System.out.println(
                        "[JU] " + jugadorFicha + ": Conseguí " + puntos + " puntos y recibió un " + premio + ".");
                exchanger.exchange(premio);
                this.cambiarEstado();
            }
        } catch (Exception e) {
            System.out.println("[JU] Error en el encargado de juegos:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que simula el juego de premios.
     * <p>
     * Calcula la cantidad de puntos que obtuvo el jugador y selecciona un premio
     * según la cantidad de puntos.
     * 
     * @return String
     */
    private String calcularPremio() {
        String[] premios = { "Premio pequeño", "Premio mediano", "Premio grande" };
        puntos = (int) (Math.random() * 100);
        return premios[(puntos < 30) ? 0 : (puntos < 70) ? 1 : 2];
    }

    // Getters y Setters

    public Exchanger<String> getExchanger() {
        return exchanger;
    }

    private void cambiarEstado() {
        ocupado = !ocupado;
    }

    public boolean getEstado() {
        return ocupado;
    }
}