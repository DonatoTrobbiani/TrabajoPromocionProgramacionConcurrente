package tests;

import Gestores.*;

public class testGestorTiempo {
    public static void main(String[] args) {
        GestorTiempo gestor = new GestorTiempo(12, 0,null);
        Thread hilo = new Thread(gestor);
        hilo.start();
        try {
            hilo.join();
        } catch (Exception e) {
        }
    }
}