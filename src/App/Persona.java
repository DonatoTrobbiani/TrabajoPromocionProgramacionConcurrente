package App;

import java.util.ArrayList;
import java.util.List;

public class Persona implements Runnable {
    private final Parque parque;
    private final String nombre;
    private List<String> atracciones;
    private String premio = "Ninguno";

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
            parque.entrarMolinete();
            System.out.println("[PE] " + nombre + " entró al parque");
            Thread.sleep(3000);// Simula el tiempo que tarda en llegar al molinete
            System.out.println("[PE] " + nombre + " llegó al molinete");
            parque.salirMolinete();

            // 2. Mientras esté en horario habilitado y queden atracciones por visitar:
            int atraccionAnterior = -1;
            boolean salida = false;
            while (!salida && parque.estaAbiertoParque() && !atracciones.isEmpty()) {
                // Simula el tiempo que tarda en dar una vuelta por el parque
                // ! AGREGAR SHOPPING, DAR VUELTAS INDEFINIDAMENTE CON PROBABILIDAD DE SALIR (SI
                // ! ES ANTES DE LAS 23 CLARO).
                System.out.println("[PE] " + nombre + " está paseando por el parque.\n Actividades Pendientes: "
                        + atracciones.toString());
                Thread.sleep((int) (Math.random() * 3000 + 3000));
                if (parque.estanAbiertasAtracciones()) {
                    // Asegura no repetir actividades
                    int atraccionActual;
                    do {
                        atraccionActual = (int) (Math.random() * atracciones.size());
                    } while (atraccionActual == atraccionAnterior && atracciones.size() > 1);
                    // Intenta realizar la actividad
                    boolean exito = this.hacerSiguienteActividad(atracciones.get(atraccionActual));
                    // Si fue exitoso, quita la actividad de la lista
                    if (exito) {
                        atracciones.remove(atraccionActual);
                    }
                    atraccionAnterior = atraccionActual; // Actualiza la actividad anterior para no repetirla
                } else {
                    System.out.println("[PE] " + nombre + " no puede realizar actividades, el parque está cerrado.");
                    salida = true;
                }
            }
            // 3. Si completa todas las atracciones o se acaba el horario, sale del parque
            System.out.println("[PE] " + nombre + " sale del parque.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            default:
                throw new InterruptedException("Actividad no encontrada.");
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