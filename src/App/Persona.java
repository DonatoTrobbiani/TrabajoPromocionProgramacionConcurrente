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
        this.atracciones = new ArrayList<>(List.of("Comedor", "Trenes", "Realidad Virtual", "Area de Premios"));
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
        boolean salida = false;
        int atraccionAnterior = -1;
        while (!salida) {
            // 0. Evalúa si puede seguir en el parque
            if (!parque.estaAbiertoParque()) {
                System.out.println("[Persona] " + nombre + " se va del parque...");
                salida = true;
            } else {
                // 1. Recorre un poco el parque
                System.out.println("[Persona] " + nombre + " está paseando por el parque.\n Actividades Pendientes: "
                        + atracciones.toString());
                Thread.sleep(2000);
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
                Thread.sleep(10000);
                res = true;
                // res = this.entrarAreaComedor();
                break;
            case "Trenes":
                Thread.sleep(10000);
                res = true;
                // res = this.entrarAreaTrenes();
                break;
            case "Realidad Virtual":
                // Thread.sleep(10000);
                res = true;
                res = this.entrarAreaVR();
                break;
            case "Area de Premios":
                // ? Cambiar carteles si es posible
                Thread.sleep(10000);
                res = true;
                // res = parque.areaDeJuegos(this);
                break;
            case "Shopping":
                res = true;
                break;
            default:
                throw new InterruptedException("Actividad no encontrada.");
        }
        return res;
    }

    private boolean entrarShopping() throws InterruptedException {
        boolean res = parque.entrarShopping(this);
        if (res) {
            System.out.println("[SH] " + nombre + " está comprando.");
            Thread.sleep(3000);
            System.out.println("[SH] " + nombre + " terminó de comprar.");
            comproShopping = true;
        }
        return res;
    }

    private boolean entrarAreaVR() throws InterruptedException {
        System.out.println("[VR] " + nombre + " quiere pobrar la realidad virtual.");
        boolean res = parque.entrarRealidadVirtual(this);
        if (res) {
            System.out.println("[VR] " + nombre + " comienza la simulación.");
            Thread.sleep(11000);
            System.out.println("[VR] " + nombre + " terminó la simulación.");
            Thread.sleep(11000);
            System.out.println("[VR] " + nombre + " abandona la atracción de realidad virtual.");
            parque.salirRealidadVirtual(this);
        }
        return res;
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

    private boolean entrarAreaTrenes() throws InterruptedException {
        boolean res = parque.entrarAreaTrenes(this);
        if (res) {
            System.out.println("[TR] " + nombre + " se bajó del tren.--------------");
        }
        return res;
    }

    public String getNombre() {
        return nombre;
    }
}