package AreaDeJuegosDePremios;

import java.util.concurrent.Exchanger;

public class Test {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        Encargado encargado = new Encargado("Encargado", exchanger);
        Persona persona = new Persona("Persona", exchanger);
        Thread hiloEncargado = new Thread(encargado);
        Thread hiloPersona = new Thread(persona);
        hiloEncargado.start();
        hiloPersona.start();
        try {
        hiloEncargado.join();
        hiloPersona.join();
        } catch (Exception e) {

        }
    }
}
