package AreaDeJuegosDePremios;

import java.util.concurrent.Exchanger;

public class Persona implements Runnable {
    private String nombre;
    private String ficha = "Ficha de juego";
    private String premio;
    private Exchanger <String> exchanger;

    public Persona(String nombre, Exchanger<String> exchanger) {
        this.nombre = nombre;
        this.exchanger = exchanger;
    }
    
    public void run() {
        try {
            // El visitante da una ficha
            System.out.println("Visitante: Entrego una " + ficha);

            // Intercambia la ficha por el premio
            ficha = exchanger.exchange(ficha);
            premio = exchanger.exchange(ficha);
            System.out.println("Visitante: Recibí un " + premio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
