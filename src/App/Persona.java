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
            System.out.println(nombre + " entró al parque");
            parque.salirMolinete();

            // 2. Mientras esté en horario habilitado y queden atracciones por visitar:
            int atraccionAnterior = -1;
            boolean salida = false;
            while (!salida && parque.estaAbiertoParque() && !atracciones.isEmpty()) {
                if (parque.puedeUsarActividades()) {
                    // Simular dar vueltas por el parque
                    System.out.println(nombre + " está paseando por el parque.");
                    Thread.sleep((int) Math.random() * 3000);

                    int atraccionActual;

                    // Asegura no repetir actividades
                    do {
                        atraccionActual = (int) (Math.random() * atracciones.size());
                    } while (atraccionActual == atraccionAnterior);

                    // Intenta realizar la actividad
                    boolean exito = this.hacerSiguienteActividad(atracciones.get(atraccionActual));

                    // Si fue exitoso, quita la actividad de la lista
                    if (exito) {
                        System.out.println(
                                nombre + " completó la atracción: " + atracciones.get(atraccionActual));
                        atracciones.remove(atraccionActual);
                    } else {
                        // Si no fue exitoso, vuelve a intentar
                        System.out.println(
                                nombre + " no pudo completar la atracción: "
                                        + atracciones.get(atraccionActual));
                        atraccionAnterior = atraccionActual; // Actualiza la actividad anterior para no repetirla
                    }
                } else {
                    System.out.println(nombre + " no puede realizar actividades, el parque está cerrado.");
                    salida = true;
                }
            }
            // 3. Si completa todas las atracciones o se acaba el horario, sale del parque
            System.out.println(nombre + " sale del parque");
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
                res = this.areaComedor();
                break;
            case "Trenes":
                res = this.entrarAreaTrenes();
                break;
            case "Realidad Virtual":
                res = this.jugarRealidadVirtual();
                break;
            case "Area de Premios":
                res = parque.areaDeJuegos(this);
                break;
            default:
                throw new InterruptedException("Actividad no encontrada.");
        }
        return res;
    }

    private boolean jugarRealidadVirtual() throws InterruptedException {
        System.out.println(nombre + " quiere pobrar la realidad virtual.");
        boolean res = parque.entrarRealidadVirtual(this);
        if (res) {
            System.out.println(nombre + " comienza la simulación.");
            Thread.sleep((int) Math.random() * 2000);
            System.out.println((nombre) + " terminó la simulación.");
            Thread.sleep(1000);
            System.out.println(nombre + " abandona la atracción de realidad virtual.");
            parque.salirRealidadVirtual();
        }
        return res;
    }

    private boolean areaComedor() throws InterruptedException {
        boolean respuesta = parque.entrarAreaComedor(this);
        if (respuesta) {
            System.out.println(nombre + " está comiendo.");
            Thread.sleep((int) Math.random() * 2000);
            System.out.println(nombre + " terminó de comer.");
        }
        return respuesta;
    }

    private boolean entrarAreaTrenes() throws InterruptedException {
        return parque.entrarAreaTrenes(this);
    }

    public String getNombre() {
        return nombre;
    }
}