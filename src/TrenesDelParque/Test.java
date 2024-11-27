package TrenesDelParque;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {
    public static void main(String[] args) {
        BlockingQueue<Persona> queue = new ArrayBlockingQueue<>(10);
        Tren tren = new Tren(queue);
        Thread trenThread = new Thread(tren);

        trenThread.start();

        for (int i = 0; i < 15; i++) {
            Persona persona = new Persona("Persona " + i, queue);
            Thread personaThread = new Thread(persona);
            personaThread.start();
            try {
                Thread.sleep((long) (Math.random() * 1000));// espera 1 segundo
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}