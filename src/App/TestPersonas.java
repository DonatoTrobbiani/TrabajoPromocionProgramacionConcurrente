package App;

import java.util.concurrent.Exchanger;

import AreaDeJuegosDePremios.Encargado;
import Gestores.GestorTiempo;
import RealidadVirtual.EspacioVirtual;

public class TestPersonas {
    public static void main(String[] args) {
        GestorTiempo gestorTiempo = new GestorTiempo(null);
        Exchanger<String> exchanger = new Exchanger<>();
        Encargado encargado = new Encargado(exchanger);
        Thread hiloEncargado = new Thread(encargado);
        hiloEncargado.start();
        EspacioVirtual espacioVirtual = new EspacioVirtual(2, 2, 2);
        Parque parque = new Parque(1, gestorTiempo, encargado, espacioVirtual);
        gestorTiempo.setParque(parque);
        Persona persona = new Persona(parque, 1);
        Thread hiloPersona = new Thread(persona);
        Thread hiloGestorTiempo = new Thread(gestorTiempo);

        hiloGestorTiempo.start();
        hiloPersona.start();

        try {
            hiloPersona.join();
            hiloGestorTiempo.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
