import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class VentanaDescargas extends JFrame {

    private static final int TOTAL_ARCHIVOS = 3;

    private JProgressBar[] barras;

    private JButton botonIniciar;
    private JButton botonCancelar;

    private JTextArea bitacora;

    private ArrayList<Descarga> descargas;

    private ContadorDescargas contador;

    private int hilosFinalizados;

    public VentanaDescargas() {

        setTitle("Descarga Múltiple");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        descargas = new ArrayList<Descarga>();
        contador = new ContadorDescargas();

        crearComponentes();
    }

    private void crearComponentes() {

        JPanel panelBarras =
                new JPanel(new GridLayout(TOTAL_ARCHIVOS, 2, 5, 5));

        panelBarras.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        barras = new JProgressBar[TOTAL_ARCHIVOS];

        for (int i = 0; i < TOTAL_ARCHIVOS; i++) {

            barras[i] = new JProgressBar(0, 100);
            barras[i].setStringPainted(true);

            panelBarras.add(
                    new JLabel("Archivo " + (i + 1))
            );

            panelBarras.add(barras[i]);
        }

        botonIniciar = new JButton("Iniciar descargas");
        botonCancelar = new JButton("Cancelar");

        botonCancelar.setEnabled(false);

        botonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarDescargas();
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarDescargas();
            }
        });

        JPanel panelBotones = new JPanel();

        panelBotones.add(botonIniciar);
        panelBotones.add(botonCancelar);

        JPanel panelSuperior = new JPanel(new BorderLayout());

        panelSuperior.add(panelBarras, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        bitacora = new JTextArea();
        bitacora.setEditable(false);

        JScrollPane scroll = new JScrollPane(bitacora);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void agregarMensaje(final String mensaje) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                bitacora.append(mensaje + "\n");

                bitacora.setCaretPosition(
                        bitacora.getDocument().getLength()
                );
            }
        });
    }

    private void cambiarEstadoBotones(boolean enCurso) {

        botonIniciar.setEnabled(!enCurso);
        botonCancelar.setEnabled(enCurso);
    }

    private void iniciarDescargas() {

        descargas.clear();

        contador.reiniciar();

        hilosFinalizados = 0;

        bitacora.setText("");

        cambiarEstadoBotones(true);

        for (int i = 0; i < TOTAL_ARCHIVOS; i++) {

            barras[i].setValue(0);

            Descarga descarga = new Descarga(
                    "Archivo " + (i + 1),
                    barras[i],
                    this
            );

            descargas.add(descarga);
        }

        agregarMensaje("Descargas iniciadas");

        for (Descarga descarga : descargas) {

            Thread hilo = new Thread(descarga);

            hilo.start();
        }
    }

    private void cancelarDescargas() {

        for (Descarga descarga : descargas) {
            descarga.cancelar();
        }

        agregarMensaje("Cancelando descargas...");

        botonCancelar.setEnabled(false);
    }

    public synchronized void avisarDescargaCompletada() {

        int completadas = contador.registrarCompletada();

        hilosFinalizados++;

        if (completadas == TOTAL_ARCHIVOS) {

            agregarMensaje(
                    "Todas las descargas han finalizado."
            );
        }

        verificarFinalizacion();
    }

    public synchronized void avisarDescargaCancelada() {

        hilosFinalizados++;

        verificarFinalizacion();
    }

    private void verificarFinalizacion() {

        if (hilosFinalizados == TOTAL_ARCHIVOS) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    cambiarEstadoBotones(false);
                }
            });
        }
    }
}