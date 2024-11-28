package AreaDeJuegosDePremios;

import java.util.concurrent.Exchanger;

public class Encargado implements Runnable {
    private String fichaRecibida;
    private String premio;
    private Exchanger<String> exchanger = new Exchanger<>();
    private boolean ocupado = false;

    public Encargado(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    // ! son mas de un encargado
    public void run() {
        try {
            while (true) {
                // El encargado recibe la ficha
                fichaRecibida = exchanger.exchange(null);
                cambiarEstado();
                System.out.println("Encargado: Recibí una " + fichaRecibida);
                // ! HACER SLEEP PARA SIMULAR JUEGO
                // Simular el cálculo de puntos en el juego
                int puntos = (int) (Math.random() * 100);
                System.out.println("Encargado: El jugador obtuvo " + puntos + " puntos");

                // Determinar el premio basado en los puntos
                if (puntos < 30) {
                    premio = "Premio pequeño";
                } else if (puntos < 70) {
                    premio = "Premio mediano";
                } else {
                    premio = "Premio grande";
                }

                // Entregar el premio al visitante
                System.out.println("Encargado: Entregué un " + premio);
                exchanger.exchange(premio);
                cambiarEstado();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public Exchanger<String> getExchanger() {
        return exchanger;
    }

    private void cambiarEstado() {
        ocupado = !ocupado;
    }

    public boolean isOcupado() {
        return ocupado;
    }
}