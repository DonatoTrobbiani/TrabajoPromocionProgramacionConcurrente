package App;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

import AreaDeJuegosDePremios.Encargado;
import Gestores.*;
import RealidadVirtual.EspacioVirtual;

public class Parque {
    // podemos agregar un semaforo mutex para chequear la disponibilidad del parque.
    private final int cantMolinetes;
    private final GestorTiempo gestorTiempo;
    private final Encargado encargado;
    private final EspacioVirtual espacioVirtual;

    private final Semaphore semaforoMolinetes;

    public Parque(int cantMolinetes, GestorTiempo gestorTiempo, Encargado encargado, EspacioVirtual ev) {
        this.cantMolinetes = cantMolinetes;
        this.semaforoMolinetes = new Semaphore(0);// empieza en 0 para que no se pueda entrar al parque hasta que
                                                  // abra
        this.gestorTiempo = gestorTiempo;
        this.encargado = encargado;
        this.espacioVirtual = ev;
    }

    public int getCantMolinetes() {
        return cantMolinetes;
    }

    public Semaphore getSemaforoMolinetes() {
        return semaforoMolinetes;
    }

    public void entrarMolinete() throws InterruptedException {
        semaforoMolinetes.acquire();
    }

    public void salirMolinete() {
        semaforoMolinetes.release();
    }

    public boolean estaAbiertoParque() {
        return gestorTiempo.getHora() >= 9 && gestorTiempo.getHora() < 18;
    }

    public boolean areaDeJuegos(Persona p) throws InterruptedException {
        // ! SI SON LAS 19 NO SE PUEDE JUGAR
        boolean res = true;
        Exchanger<String> exchanger = encargado.getExchanger();
        String ficha = "Ficha";

        // El visitante da una ficha
        System.out.println("Visitante: Entrego una " + ficha);
        // Intercambia la ficha por el premio
        ficha = exchanger.exchange(ficha);
        p.setPremio(exchanger.exchange(null));
        System.out.println("Visitante: Recibí un " + p.getPremio());
        return res;
    }

    public boolean entrarRealidadVirtual() throws InterruptedException {
        boolean res = true;
        espacioVirtual.entrarVR(Thread.currentThread().getName());
        return res;
    }

    public boolean salirRealidadVirtual() throws InterruptedException {
        boolean res = true;
        espacioVirtual.salirVR();
        return res;
    }
}
