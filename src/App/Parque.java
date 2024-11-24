package App;

import java.util.concurrent.Semaphore;

import Gestores.*;

public class Parque {
    private final int cantMolinetes;

    private final Semaphore semaforoMolinetes;

    public Parque(int cantMolinetes) {
        this.cantMolinetes = cantMolinetes;
        this.semaforoMolinetes = new Semaphore(0);// empieza en 0 para que no se pueda entrar al parque hasta que
                                                  // abra
    }

    public int getCantMolinetes() {
        return cantMolinetes;
    }

    public Semaphore getSemaforoMolinetes() {
        return semaforoMolinetes;
    }

}
