package App;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import AreaDeJuegosDePremios.Encargado;
import Comedor.Comedor;
import Gestores.*;
import RealidadVirtual.EspacioVirtual;
import TrenesDelParque.Tren;

public class Parque {
    // podemos agregar un semaforo mutex para chequear la disponibilidad del parque.
    private final int cantMolinetes;
    private final GestorTiempo gestorTiempo;
    private final Encargado encargado;
    private final EspacioVirtual espacioVirtual;
    private final Comedor comedor;
    private final Tren trencito;

    private final Semaphore semaforoMolinetes;

    public Parque(int cantMolinetes, GestorTiempo gestorTiempo, Encargado encargado, EspacioVirtual ev,
            Comedor comedor, Tren trencito) {
        this.cantMolinetes = cantMolinetes;
        this.semaforoMolinetes = new Semaphore(0);// empieza en 0 para que no se pueda entrar al parque hasta que
                                                  // abra
        this.gestorTiempo = gestorTiempo;
        this.encargado = encargado;
        this.espacioVirtual = ev;
        this.comedor = comedor;
        this.trencito = trencito;
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

    public boolean entrarAreaComedor() throws InterruptedException {
        return comedor.entrarComedor();
    }

    public BlockingQueue<Persona> getQueue() {
        return trencito.getQueue();
    }

    public boolean entrarAreaTrenes(Persona p) throws InterruptedException {
        boolean respuesta = false;
        BlockingQueue<Persona> queue = this.getQueue();
        if (queue.offer(p, 2, TimeUnit.SECONDS)) {
            System.out.println("Persona " + Thread.currentThread().getName() +
                    " entró a la cola para subirse al tren");
            respuesta = true;
        } else {
            System.out.println("Persona " + Thread.currentThread().getName() +
                    " vio que la cola para el tren estaba llena y se fue");
        }
        return respuesta;
    }

    public boolean puedeUsarActividades() {
        return gestorTiempo.getHora() <= 19;
    }
}
