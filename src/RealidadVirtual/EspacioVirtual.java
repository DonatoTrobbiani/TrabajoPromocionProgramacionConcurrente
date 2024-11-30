package RealidadVirtual;

import App.Persona;
import Gestores.GestorTiempo;

/**
 * Clase que representa el Espacio Virtual del parque.
 * <p>
 * Cuenta con un método para que las personas puedan entrar a la Realidad
 * Virtual y otro para el momento de salida.
 * <p>
 * Como mecanismo de sincronización, se utiliza un monitor para controlar la 
 * cantidad de visores, manoplas y bases disponibles. Y un gestor de tiempo para
 * verificar si la actividad dejó de estar disponible en el periodo de espera.
 * 
 * @author Gianfranco Gallucci, FAI-3824
 * @author Donato Trobbiani Perales, FAI-4492
 */
public class EspacioVirtual {
    private int cantidadVisores;
    private int cantManoplas;
    private int cantBases;
    private final GestorTiempo gestorTiempo;

    public EspacioVirtual(int cantVR, int cantManoplas, int cantBases, GestorTiempo gestorTiempo) {
        this.cantidadVisores = cantVR;
        this.cantManoplas = cantManoplas;
        this.cantBases = cantBases;
        this.gestorTiempo = gestorTiempo;
    }

    /**
     * Método sincronizado que simula la entrada a la Realidad Virtual.
     * <p>
     * Una persona puede entrar si hay al menos un casco, dos manoplas y una base.
     * Inicialmente se observa si está dentro del horario apto para la actividad.
     * Si no hay suficiente equipamiento, la persona debe esperar.
     * <p>
     * Si la persona puede entrar, se decrementa la cantidad de cascos, manoplas y
     * bases y se muestra un mensaje de ingreso.
     * 
     * @return boolean
     */
    public synchronized boolean entrarVR(Persona p) throws InterruptedException {
        boolean tieneEquipamiento = false, continuarActividad = true;
        while (continuarActividad) {
            //1. Verificar si está dentro del horario de la actividad.
            if (gestorTiempo.getHora() >= 19) {
                System.out.println(
                        "[VR] " + p.getNombre()
                                + " no puede entrar a la Realidad Virtual porque cerraron las actividades.");
                continuarActividad = false;
            } else if (cantidadVisores < 1 || cantManoplas < 2 || cantBases < 1) {
                //1.1. Verificar si hay equipamiento disponible.
                System.out.println("[VR] " + p.getNombre()
                        + " debe esperar en el espacio virtual, no hay equipos suficientes.");
                wait();
            } else {
                //1.2. Si hay equipamiento disponible, la persona entra.
                cantidadVisores--;
                cantManoplas -= 2;
                cantBases--;
                System.out.println("[VR] " + p.getNombre() + " entró a la Realidad Virtual");
                tieneEquipamiento = true;
                continuarActividad = false;
            }
        }
        return tieneEquipamiento;
    }

    /**
     * Método sincronizado que simula la salida de la Realidad Virtual.
     * <p>
     * Se incrementa la cantidad de cascos, manoplas
     * y bases disponibles, se muestra un mensaje de salida
     * y se notifica a las personas que esperan.
     * 
     * @param Persona
     */
    public synchronized void salirVR(Persona p) throws InterruptedException {
        cantidadVisores++;
        cantBases++;
        cantManoplas += 2;
        System.out.println("[VR] " + p.getNombre() + " salió de la Realidad Virtual.");
        notify();
    }
}
