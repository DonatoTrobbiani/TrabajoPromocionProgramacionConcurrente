package Comedor.Comedor2;

public class Test {
    public static void main(String[] args) {
        Comedor2 comedor = new Comedor2();
        Persona2[] personas = new Persona2[15];
        for (int i = 0; i < personas.length; i++) {
            personas[i] = new Persona2("Persona " + i, comedor);
            new Thread(personas[i]).start();
        }
    }
}
