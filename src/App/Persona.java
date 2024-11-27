package App;

import java.util.ArrayList;
import java.util.List;

public class Persona implements Runnable {
    private final Parque parque;
    private final int id;
    private List<String> atracciones;
    private String premio = "Ninguno";

    public Persona(Parque parque, int id) {
        this.parque = parque;
        this.id = id;
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
            System.out.println("Persona " + id + " entró al parque");
            parque.salirMolinete();

            // 2. Mientras esté en horario habilitado y queden atracciones por visitar:
            int atraccionAnterior = -1;
            while (parque.estaAbiertoParque() && !atracciones.isEmpty()) {
                int atraccionActual;

                // Asegura no repetir actividades
                do {
                    atraccionActual = (int) (Math.random() * atracciones.size());
                } while (atraccionActual == atraccionAnterior);

                // Intenta realizar la actividad
                boolean exito = this.hacerSiguienteActividad(atracciones.get(atraccionActual));

                // Si fue exitoso, quita la actividad de la lista
                if (exito) {
                    System.out.println("Persona " + id + " completó la atracción: " + atracciones.get(atraccionActual));
                    atracciones.remove(atraccionActual);
                } else {
                    // Si no fue exitoso, vuelve a intentar
                    System.out.println(
                            "Persona " + id + " no pudo completar la atracción: " + atracciones.get(atraccionActual));
                    atraccionAnterior = atraccionActual; // Actualiza la actividad anterior para no repetirla
                }
                // Sleep para testear si sigue cuando se cierra el parque
                Thread.sleep(10000);
            }
            // 3. Si completa todas las atracciones o se acaba el horario, sale del parque
            System.out.println("Persona " + id + " sale del parque");
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

                break;
            case "Trenes":

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
        // ! Si son las 19 no se puede jugar
        System.out.println(Thread.currentThread().getName() + " quiere pobrar la realidad virtual.");
        boolean respuesta = parque.entrarRealidadVirtual();
        System.out.println(Thread.currentThread().getName() + " comienza la simulación.");
        Thread.sleep((int) Math.random() * 2000);
        System.out.println(Thread.currentThread().getName() + " terminó la simulación.");
        respuesta = respuesta && parque.salirRealidadVirtual();
        System.out.println(Thread.currentThread().getName() + " abandona la atracción de realidad virtual.");
        return respuesta;
    }
}