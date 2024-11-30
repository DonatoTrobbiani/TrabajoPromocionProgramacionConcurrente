package Gestores;

import java.awt.Font;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class VentanaTiempo extends Thread {
    private JFrame marco;
    private JTextArea areaTexto;
    private AtomicInteger hora;
    private AtomicInteger minutos;

    public VentanaTiempo(AtomicInteger hora, AtomicInteger minutos) {
        this.hora = hora;
        this.minutos = minutos;

        SwingUtilities.invokeLater(() -> {
            marco = new JFrame("Tiempo");
            marco.setSize(300, 80);
            marco.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            marco.setResizable(false);
            areaTexto = new JTextArea();
            areaTexto.setEditable(false);
            areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 16));
            marco.add(areaTexto);

            marco.setVisible(true);
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                SwingUtilities.invokeLater(() -> {
                    areaTexto.setText(String.format("   %02d:%02d\n", hora.get(), minutos.get()));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
