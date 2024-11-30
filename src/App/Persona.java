package App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Persona implements Runnable {
    private final Parque parque;
    private final String nombre;
    private List<String> atracciones;
    private String premio = "Ninguno";
    private final Random random = new Random();// Para elegir atracciones al azar
    private final List<String> inventarioPremios = new ArrayList<>(); // Para guardar los premios

    public Persona(Parque parque, String nombrePersona) {
        this.parque = parque;
        this.nombre = nombrePersona;
        // Atracciones son Comedor, Trenes, Realidad Virtual y Area de Premios.
        this.atracciones = new ArrayList<>(
                List.of("Comedor", "Trenes", "Realidad Virtual", "Area de Premios", "Shopping"));
    }

    @Override
    public void run() {
        try {
            // 1. Entra al parque (tomando un semáforo. Si no puede tomarlo, se bloquea).
            // 2. Mientras esté en horario abilitado y queden atracciones por visitar:
            // 2.1 Toma una atracción al azar y la realiza (o no, dependiendo de la
            // atracción puede volver a esperar o hacer otra cosa).
            // 2.2 Se guarda la atracción anterior para no repetirla, y se elige otra.
            // 2.3 Si se realiza con éxito, se quita de la lista de atracciones pendientes.
            // 3. Si completa todas las atracciones, o se acaba el horario, sale del parque.

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

        // 1.2 Simula el tiempo que tarda en llegar al molinete
        Thread.sleep(1500);

        // 1.3 Pasa el molinete
        System.out.println("[Persona] " + nombre + " entró al parque.");
        parque.salirMolinete();
    }

    public List<String> getInventarioPremios() {
        return inventarioPremios;
    }

    /**
     * Método que simula el recorrido por el parque.
     * 
     * @throws InterruptedException
     */
    private void recorrerParque() throws InterruptedException {
        // ! FALTA HACER QUE NO EJECUTE EL CÓDIGO SI COMPLETÓ TODAS LAS ACTIVIDADES.
        // Y SOLO RECORRA EL PARQUE
        boolean salida = false;
        int atraccionAnterior = -1;
        while (!salida) {
            // 0. Evalúa si puede seguir en el parque
            if (!parque.estaAbiertoParque()) {
                System.out.println("[Persona] " + nombre + " se va del parque...");
                salida = true;
            } else {
                if (atracciones.isEmpty()) {
                    System.out.println("[Persona] " + nombre + " completó todas las actividades.");
                    salida = true;
                } else {
                    // 1. Recorre un poco el parque
                    System.out
                            .println("[Persona] " + nombre + " está paseando por el parque.\n Actividades Pendientes: "
                                    + atracciones.toString());
                    // Simual el tiempo que tarda en recorrer el parque (max 20min)
                    Thread.sleep((int) Math.random() * 5000 + 5000);
                    // 2. Elige una actividad al azar
                    if (parque.estanAbiertasAtracciones()) {
                        // 2.1 Asegura no repetir actividades
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

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    private boolean hacerSiguienteActividad(String atraccion) throws InterruptedException {
        boolean res = false;
        switch (atraccion) {
            case "Comedor":
                res = this.entrarAreaComedor();
                break;
            case "Trenes": // * CARTELES CORREGIDOS, TIEMPOS CORREGIDOS
                res = this.entrarAreaTrenes();
                break;
            case "Realidad Virtual": // * CARTELES CORREGIDOS, TIEMPOS CORREGIDOS
                res = this.entrarAreaVR();
                break;
            case "Area de Premios": // * CARTELES CORREGIDOS, TIEMPOS CORREGIDOS
                res = this.entrarAreaJuegos();
                break;
            case "Shopping": // * CARTELES CORREGIDOS, TIEMPOS CORREGIDOS
                res = this.entrarShopping();
                break;
            default:
                throw new InterruptedException("Actividad no encontrada.");
        }
        return res;
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

    private boolean entrarAreaComedor() throws InterruptedException {
        boolean respuesta = parque.entrarAreaComedor(this);
        if (respuesta) {
            System.out.println(nombre + " está comiendo.");
            Thread.sleep(3000);
            System.out.println(nombre + " terminó de comer.");
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

    public String getNombre() {
        return nombre;
    }

    public void agregarItem(String item) {
        inventarioPremios.add(item);
    }
}