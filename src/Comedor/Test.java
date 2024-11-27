package Comedor;

public class Test {
    public static void main(String[] args) {
        Comedor comedor = new Comedor(2);
        Persona[] personas = new Persona[10];
        for (int i = 0; i < personas.length; i++) {
            personas[i] = new Persona("Persona " + i, comedor);
            new Thread(personas[i]).start();
        }
    }
}
