package App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase que representa a una persona que visita el parque, implementa
 * {@link Runnable}.
 * <p>
 * Cada persona tiene un nombre y una lista de atracciones que debe visitar.
 * Las atracciones son: Comedor, Trenes, Realidad Virtual, Area de Premios y
 * Shopping.
 * <p>
 * En su rutina, una persona intenta entrar al parque, recorre las atracciones y
 * luego sale.
 * 
 * @author Gianfranco Gallucci, FAI-3824
 * @author Donato Trobbiani Perales, FAI-4492
 * 
 */
public class Persona implements Runnable {
    private final Parque parque;
    private final String nombre;
    private List<String> atracciones;
    private final Random random = new Random();// Para elegir atracciones al azar
    private final List<String> inventarioPremios = new ArrayList<>(); // Inventario para guardar premios

    public Persona(Parque parque, String nombrePersona) {
        this.parque = parque;
        this.nombre = nombrePersona;
        // Atracciones son Comedor, Trenes, Realidad Virtual y Area de Premios.
        this.atracciones = new ArrayList<>(
                List.of("Comedor", "Trenes", "Realidad Virtual", "Area de Premios", "Shopping"));
    }

    /**
     * Método run de la clase Persona.
     * <p>
     * 1. Ingresa al parque haciendo uso de {@code ingresoParque()}.
     * <p>
     * 2. Recorre el parque haciendo uso de {@code recorrerParque()}.
     */
    @Override
    public void run() {
        try {
            // 1. Entra al parque
            this.ingresoParque();
            // 2. Mientras esté en horario habilitado y queden atracciones por visitar:
            this.recorrerParque();
        } catch (InterruptedException e) {
            System.out.println("[Persona] " + nombre + " fue interrumpida:");
            e.printStackTrace();
        }
    }

    /**
     * Método que simula el ingreso al parque.
     * 
     * @throws InterruptedException
     */
    private void ingresoParque() throws InterruptedException {
        // 1. Entra al parque (tomando un semáforo. Si no puede tomarlo, se bloquea).
        System.out.println("[Persona] " + nombre + " quiere entrar al parque.");
        parque.entrarMolinete();
        System.out.println("[Persona] " + nombre + " llegó a los molinetes.");

        // 2. Simula el tiempo que tarda en llegar al molinete
        Thread.sleep(1500);

        // 3. Pasa el molinete
        System.out.println("[Persona] " + nombre + " entró al parque.");
        parque.salirMolinete();
    }

    /**
     * Método que simula el recorrido por el parque.
     * 
     * @throws InterruptedException
     */
    private void recorrerParque() throws InterruptedException {
        boolean salida = false;
        int atraccionAnterior = -1;
        while (!salida) {
            // 0. Evalúa si puede estar en el parque.
            if (!parque.estaAbiertoParque()) {
                System.out.println("[Persona] " + nombre + " se va del parque...");
                salida = true;
            } else {
                // 1. Evalúa si quedan atracciones por visitar
                if (atracciones.isEmpty()) {
                    // 1.1. Si no quedan atracciones, se queda un rato más en el parque
                    System.out.println("[Persona] " + nombre
                            + " completó todas las actividades. Va a recorrer el parque por un tiempo.");
                    if (parque.getHora() < 22) {
                        Thread.sleep((int) Math.random() * 30000); // Máx una hora.
                    }
                    salida = true;
                } else {
                    // 1.1. Recorre un poco el parque
                    System.out
                            .println("[Persona] " + nombre + " está paseando por el parque.\n Actividades Pendientes: "
                                    + atracciones.toString());
                    Thread.sleep((int) Math.random() * 5000 + 5000); // (min 10min, max 20min)

                    // 2. Elige una actividad al azar
                    if (parque.estanAbiertasAtracciones()) {
                        // 2.1 Se asegura de no repetir actividades
                        int atraccionActual;
                        do {
                            atraccionActual = random.nextInt(atracciones.size());
                        } while (atraccionActual == atraccionAnterior && atracciones.size() > 1);

                        // 2.2 Intenta realizar la actividad
                        boolean exito = this.hacerSiguienteActividad(atracciones.get(atraccionActual));

                        // 2.3 Si fue exitoso, quita la actividad de la lista
                        if (exito) {
                            atracciones.remove(atraccionActual);
                        }
                        atraccionAnterior = atraccionActual; // Actualiza la actividad anterior para no repetirla
                    }
                }
            }
        }
        System.out.println("[Persona] " + nombre + " salió del parque. Se lleva: " + inventarioPremios.toString());
    }

    /**
     * Método que simula la elección y ejecución de una actividad.
     * 
     * @param String atraccion
     * @return boolean
     * @throws InterruptedException
     */
    private boolean hacerSiguienteActividad(String atraccion) throws InterruptedException {
        boolean respuesta = false;
        switch (atraccion) {
            case "Comedor":
                respuesta = this.entrarAreaComedor();
                break;
            case "Trenes":
                respuesta = this.entrarAreaTrenes();
                break;
            case "Realidad Virtual":
                respuesta = this.entrarAreaVR();
                break;
            case "Area de Premios":
                respuesta = this.entrarAreaJuegos();
                break;
            case "Shopping":
                respuesta = this.entrarShopping();
                break;
            default:
                throw new InterruptedException("Actividad no encontrada.");
        }
        return respuesta;
    }

    /**
     * Método que simula la entrada al área de juegos.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    private boolean entrarAreaJuegos() throws InterruptedException {
        System.out.println("[JU] " + nombre + " va a probar su suerte en los juegos.");
        return parque.entrarAreaJuegos(this);
    }

    /**
     * Método que simula la entrada al shopping.
     * <p>
     * Cuando una persona logra entrar, compra una prenda (calculada al azar) y
     * luego sale.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    private boolean entrarShopping() throws InterruptedException {
        System.out.println("[SH] " + nombre + " está comprando en el shopping.");
        String item = parque.comprarPrenda(this);
        if (item != null) {
            Thread.sleep(3000);
            System.out.println("[SH] " + nombre + " compró " + item + ".");
            Thread.sleep(1000);
            this.agregarItem(item);
            System.out.println("[SH] " + nombre + " terminó de comprar en el shopping.");
        }
        return item != null;
    }

    /**
     * Método que simula la entrada al área de realidad virtual.
     * <p>
     * Cuando una persona logra entrar, comienza la simulación y luego de un tiempo
     * sale.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    private boolean entrarAreaVR() throws InterruptedException {
        System.out.println("[VR] " + nombre + " quiere pobrar la realidad virtual.");
        boolean respuesta = parque.entrarRealidadVirtual(this);
        if (respuesta) {
            System.out.println("[VR] " + nombre + " comienza la simulación.");
            Thread.sleep(9000);// Simula la duración de la simulación
            System.out.println("[VR] " + nombre + " terminó la simulación.");
            Thread.sleep(1000);// Simula el tiempo que tarda en salir
            parque.salirRealidadVirtual(this);
        }
        return respuesta;
    }

    /**
     * Método que simula la entrada al área de comedor.
     * <p>
     * Cuando una persona logra entrar, se sienta en una mesa y come algo.
     * Si no logra entrar al comedor o no consigue otras 3 personas para comer, no
     * come.
     * 
     * @throws InterruptedException
     */
    private boolean entrarAreaComedor() throws InterruptedException {
        boolean respuesta = parque.entrarAreaComedor(this);
        if (respuesta) {
            System.out.println("[COM] " + nombre + " quiere comer algo.");
            respuesta = parque.sentarseEnMesa(this);
            if (respuesta) {
                System.out.println("[COM] " + nombre + " está comiendo.");
                Thread.sleep((int) Math.random() * 10000 + 10000);// (Min 20min, Max 30min)
                System.out.println("[COM] " + nombre + " terminó de comer.");
                parque.salirAreaComedor();
            }
        }
        return respuesta;
    }

    /**
     * Método que simula la entrada al área de trenes.
     * Si una persona logra entrar, luego de un tiempo se baja del tren.
     * 
     * @return boolean
     * @throws InterruptedException
     */
    private boolean entrarAreaTrenes() throws InterruptedException {
        boolean respuesta = parque.entrarAreaTrenes(this);
        if (respuesta) {
            System.out.println("[TR_P] " + nombre + " se bajó del tren.--------------");
        }
        return respuesta;
    }

    // * Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public List<String> getInventarioPremios() {
        return inventarioPremios;
    }

    public void agregarItem(String item) {
        inventarioPremios.add(item);
    }

    // * Métodos de Object

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        boolean respuesta = false;
        if (this == obj) {
            respuesta = true;
        } else if (obj instanceof Persona) {
            Persona p = (Persona) obj;
            respuesta = this.nombre.equals(p.nombre);
        }
        return respuesta;
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}