import java.util.Random;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Descarga implements Runnable {

    private String nombre;
    private JProgressBar barra;
    private VentanaDescargas ventana;
    private int pausa;

    private volatile boolean cancelado;

    public Descarga(String nombre, JProgressBar barra, VentanaDescargas ventana) {

        this.nombre = nombre;
        this.barra = barra;
        this.ventana = ventana;

        cancelado = false;

        Random random = new Random();
        pausa = 50 + random.nextInt(151);
    }

    public void cancelar() {
        cancelado = true;
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
                cancelado = true;
            }

            if (cancelado) {
                ventana.agregarMensaje(nombre + ": descarga cancelada");
                ventana.avisarDescargaCancelada();
                return;
            }

            progreso++;

            actualizarBarra(progreso);

            if (progreso % 10 == 0 && progreso < 100) {
                ventana.agregarMensaje(
                        nombre + ": descarga al " + progreso + "%"
                );
            }
        }

        ventana.agregarMensaje(nombre + ": descarga completada");
        ventana.avisarDescargaCompletada();
    }
}