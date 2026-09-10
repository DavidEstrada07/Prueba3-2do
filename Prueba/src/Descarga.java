import java.util.Random;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Descarga implements Runnable {

    private String nombre;
    private JProgressBar barra;
    private int pausa;

    public Descarga(String nombre, JProgressBar barra) {

        this.nombre = nombre;
        this.barra = barra;

        Random random = new Random();
        pausa = 50 + random.nextInt(151);
    }

    private void actualizarBarra(final int progreso) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                barra.setValue(progreso);
            }
        });
    }

    @Override
    public void run() {

        int progreso = 0;

        while (progreso < 100) {

            try {
                Thread.sleep(pausa);
            } catch (InterruptedException e) {
                return;
            }

            progreso++;

            actualizarBarra(progreso);
        }
    }
}