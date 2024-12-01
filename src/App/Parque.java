package App;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

import AreaDeJuegosDePremios.EncargadoJuegos;
import Comedor.Comedor;
import Gestores.GestorTiempo;
import RealidadVirtual.EspacioVirtual;
import TrenesDelParque.Tren;

/**
 * Clase que representa el parque de diversiones.
 * <p>
 * Cuenta con un semáforo para controlar la cantidad de personas que
 * pueden entrar simultáneamente.
 * <p>
 * Al parque lo conforman un gestor de tiempo, un conjunto de Encargados de
 * Juegos, un Espacio Virtual, un Comedor y un Tren.
 * <p>
 * 
 * @author Gianfranco Gallucci, FAI-3824
 * @author Donato Trobbiani Perales, FAI-4492
 * 
 */
public class Parque {
    private final Semaphore semaforoMolinetes;
    private final int cantMolinetes;

    private final GestorTiempo gestorTiempo;

    private final EncargadoJuegos[] encargados;
    private final Semaphore mutexJuegos = new Semaphore(1);

    private final EspacioVirtual espacioVirtual;

    private final Comedor comedor;

    private final Tren tren;

    public Parque(int cantMolinetes, GestorTiempo gestorTiempo, int cantidadEncargados, EspacioVirtual ev) {
        this.cantMolinetes = cantMolinetes;
        this.semaforoMolinetes = new Semaphore(0);// empieza en 0 para que no se pueda entrar al parque hasta que
                                                  // abra
        this.gestorTiempo = gestorTiempo;
        this.encargados = new EncargadoJuegos[cantidadEncargados];
        for (int i = 0; i < cantidadEncargados; i++) {
            encargados[i] = new EncargadoJuegos(new Exchanger<>());
            new Thread(encargados[i]).start();
        }
        this.espacioVirtual = ev;
        this.comedor = new Comedor();
        this.tren = new Tren();
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
        EncargadoJuegos encargadoDisponible = buscarEncargadoLibre();
        Exchanger<String> exchanger = encargadoDisponible.getExchanger();
        String ficha = p.getNombre();
        mutexJuegos.acquire();

        // Intercambia la ficha y espera.
        System.out.println(
                "[JU] " + p.getNombre() + " entregó una ficha al encargado.");
        ficha = exchanger.exchange(ficha);
        p.agregarItem(exchanger.exchange(null));

        mutexJuegos.release();
        return true;
    }

    /**
     * Devuelve un encargado libre, si
     * no hay ninguno devuelve uno al azar.
     * 
     * @return EncaargadorJuegos
     */

    private EncargadoJuegos buscarEncargadoLibre() {
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
        return this.comedor.entrarComedor();
    }

    public boolean sentarseEnMesa(Persona p) {
        return this.comedor.sentarseEnMesa(p);
    }

    public void salirAreaComedor() {
        this.comedor.salirComedor();
    }

    public BlockingQueue<Persona> getQueue() {
        return tren.getQueue();
    }

    /**
     * Método que simula la entrada al área de trenes.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    public boolean entrarAreaTrenes(Persona p) throws InterruptedException {
        return tren.subirTren(p);
    }

    /**
     * Método para saber si las atracciones están abiertas.
     * 
     * @return boolean
     */
    public boolean estanAbiertasAtracciones() {
        return gestorTiempo.getHora() >= 9 && gestorTiempo.getHora() < 19;
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

    public int getHora() {
        return gestorTiempo.getHora();
    }
}
