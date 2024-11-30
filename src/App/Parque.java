package App;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

import AreaDeJuegosDePremios.EncargadorJuegos;
import Comedor.Comedor;
import Gestores.*;
import RealidadVirtual.EspacioVirtual;
import TrenesDelParque.Tren;

public class Parque {
    // podemos agregar un semaforo mutex para chequear la disponibilidad del parque.
    private final int cantMolinetes;
    private final GestorTiempo gestorTiempo;
    private final EncargadorJuegos[] encargados;
    private final EspacioVirtual espacioVirtual;
    private final Comedor comedor;
    private final Tren trencito;

    private final Semaphore semaforoMolinetes;
    private final Semaphore mutex = new Semaphore(1);

    public Parque(int cantMolinetes, GestorTiempo gestorTiempo, EncargadorJuegos[] encargado, EspacioVirtual ev,
            Comedor comedor, Tren trencito) {
        this.cantMolinetes = cantMolinetes;
        this.semaforoMolinetes = new Semaphore(0);// empieza en 0 para que no se pueda entrar al parque hasta que
                                                  // abra
        this.gestorTiempo = gestorTiempo;
        this.encargados = encargado;
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
        return gestorTiempo.getHora() >= 9 && gestorTiempo.getHora() < 23;
    }

    /**
     * Método que simula la entrada al área de juegos de premios.
     * <p>
     * Obtiene un encargado libre, o uno al azar si están todos ocupados, y le
     * entrega una ficha al encargado. Luego espera a que el encargado le devuelva
     * un premio en otro intercambio para agregarlo a su inventario.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    public boolean entrarAreaJuegos(Persona p) throws InterruptedException {
        // ! SE PUEDE MEJORAR CON MUTEX EXCLUSIVOS DE CADA ENCARGADO
        EncargadorJuegos encargadoDisponible = buscarEncargadoLibre();
        Exchanger<String> exchanger = encargadoDisponible.getExchanger();
        String ficha = p.getNombre();
        mutex.acquire();

        // Intercambia la ficha y espera.
        System.out.println(
                "[JU] " + p.getNombre() + " entregó una " + ficha + " al encargado.");
        ficha = exchanger.exchange(ficha);
        p.agregarItem(exchanger.exchange(null));

        mutex.release();
        return true;
    }

    /**
     * Devuelve un encargado libre, si
     * no hay ninguno devuelve uno al azar.
     * 
     * @return EncaargadorJuegos
     */

    private EncargadorJuegos buscarEncargadoLibre() {
        int i = 0;
        // Busca un encargado libre
        while (i < encargados.length && encargados[i].getEstado()) {
            i++;
        }
        // Si está dentro del rango de encargados, lo devuelve
        // sino devuelve uno al azar.
        return i < encargados.length ? encargados[i] : encargados[(int) (Math.random() * encargados.length)];
    }

    /**
     * Método que simula la entrada al área de realidad virtual.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    public boolean entrarRealidadVirtual(Persona p) throws InterruptedException {
        return espacioVirtual.entrarVR(p);
    }

    /**
     * Método que simula la salida de la realidad virtual.
     * 
     * @throws InterruptedException
     */
    public void salirRealidadVirtual(Persona p) throws InterruptedException {
        espacioVirtual.salirVR(p);
    }

    public boolean entrarAreaComedor(Persona p) throws InterruptedException {
        return comedor.entrarComedor(p);
    }

    public BlockingQueue<Persona> getQueue() {
        return trencito.getQueue();
    }

    /**
     * Método que simula la entrada al área de trenes.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    public boolean entrarAreaTrenes(Persona p) throws InterruptedException {
        return trencito.subirTren(p);
    }

    /**
     * Método para saber si las atracciones están abiertas.
     * 
     * @return boolean
     */
    public boolean estanAbiertasAtracciones() {
        return gestorTiempo.getHora() <= 19;
    }

    /**
     * Método que simula la compra de una prenda en el shopping.
     * 
     * @return String
     */
    public String comprarPrenda(Persona persona) {
        Random random = new Random();
        String[] colores = { "Rojo", "Azul", "Verde", "Amarillo", "Blanco" };
        String[] tiposRopa = { "Pantalón", "Camisa", "Zapatos", "Bufanda", "Gorro" };
        String prenda = tiposRopa[random.nextInt(tiposRopa.length)] + " " + colores[random.nextInt(colores.length)];
        return prenda;
    }
}
