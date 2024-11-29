package Comedor;

import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private final int numero;
    private final List<String> personas;

    public Mesa(int numero) {
        this.numero = numero;
        this.personas =new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public List<String> getPersonas() {
        return personas;
    }

    public void agregarPersona(String nombrePersona) {
        personas.add(nombrePersona);
    }

    public void limpiarMesa() {
        personas.clear();
    }
}
