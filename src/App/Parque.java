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
    private final Encargado[] encargado;
    private final EspacioVirtual espacioVirtual;
    private final Comedor comedor;
    private final Tren trencito;

    private final Semaphore semaforoMolinetes;
    private final Semaphore mutex = new Semaphore(1);

    public Parque(int cantMolinetes, GestorTiempo gestorTiempo, Encargado[] encargado, EspacioVirtual ev,
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
        Encargado encargadoDisponible = encargadoLibre();
        Exchanger<String> exchanger = encargadoDisponible.getExchanger();
        String ficha = "Ficha";

        mutex.acquire();
        // El visitante da una ficha
        System.out.println(p.getNombre() + " entregó una " + ficha);
        // Intercambia la ficha por el premio
        ficha = exchanger.exchange(ficha);
        p.setPremio(exchanger.exchange(null));
        System.out.println(p.getNombre() + " recibí un " + p.getPremio());
        mutex.release();
        return res;
    }

    private Encargado encargadoLibre() {
        Encargado encargadoLibre = null;
        for (Encargado encargado : this.encargado) {
            if (!encargado.isOcupado()) {
                encargadoLibre = encargado;
            }
        }
        if (encargadoLibre == null) {
            encargadoLibre = encargado[(int) Math.random() * 3];
        }
        return encargadoLibre;
    }

    public boolean entrarRealidadVirtual(Persona p) throws InterruptedException {
        return espacioVirtual.entrarVR(p);
    }

    public void salirRealidadVirtual() throws InterruptedException {
        espacioVirtual.salirVR();
    }

    public boolean entrarAreaComedor(Persona p) throws InterruptedException {
        return comedor.entrarComedor(p);
    }

    public BlockingQueue<Persona> getQueue() {
        return trencito.getQueue();
    }

    public boolean entrarAreaTrenes(Persona p) throws InterruptedException {
        boolean respuesta = false;
        BlockingQueue<Persona> queue = this.getQueue();
        if (queue.offer(p, 2, TimeUnit.SECONDS)) {
            System.out.println(p.getNombre() +
                    " entró a la cola para subirse al tren");
            respuesta = true;
        } else {
            System.out.println(p.getNombre() +
                    " vio que la cola para el tren estaba llena y se fue");
        }
        return respuesta;
    }

    public boolean puedeUsarActividades() {
        return gestorTiempo.getHora() <= 19;
    }
}
