package Gestores;

import java.awt.Color;
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
    private int escalaTiempoMS;

    public VentanaTiempo(AtomicInteger hora, AtomicInteger minutos, GestorTiempo gestorTiempo) {
        this.hora = hora;
        this.minutos = minutos;
        this.escalaTiempoMS = gestorTiempo.getEscalaTiempoMS();

        SwingUtilities.invokeLater(() -> {
            marco = new JFrame("Tiempo");
            marco.setSize(300, 80);
            marco.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            marco.setResizable(false);
            areaTexto = new JTextArea();
            areaTexto.setEditable(false);
            areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 16));
            areaTexto.setForeground(Color.WHITE);
            areaTexto.setBackground(Color.BLACK);
            marco.add(areaTexto);

            marco.setVisible(true);
            marco.setAlwaysOnTop(true);
            marco.toFront();
            marco.repaint();
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(escalaTiempoMS);
                SwingUtilities.invokeLater(() -> {
                    areaTexto.setText(String.format("   %02d:%02d\n", hora.get(), minutos.get()));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
