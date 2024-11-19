package AreaDeJuegosDePremios;

import java.util.concurrent.Exchanger;

public class Encargado implements Runnable {
    private String nombre;
    private String fichaRecibida;
    private String premio;
    private Exchanger <String> exchanger = new Exchanger<>();

    public Encargado(String nombre, Exchanger<String> exchanger) {
        this.nombre = nombre;
        this.exchanger = exchanger;
    }

    public void run() {
        try {
            // El encargado recibe la ficha
            fichaRecibida = exchanger.exchange(null);
            System.out.println("Encargado: Recibí una " + fichaRecibida);

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
            exchanger.exchange(premio);
            System.out.println("Encargado: Entregué un " + premio);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}