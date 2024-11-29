package TrenesDelParque;

public class Conductor implements Runnable {
    private final Tren tren;

    // buto
    public Conductor(Tren tren) {
        this.tren = tren;
    }

    @Override
    public void run() {
        while (true) {
            try {
                tren.salirTren();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
