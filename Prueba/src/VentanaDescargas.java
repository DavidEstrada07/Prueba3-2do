import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaDescargas extends JFrame {

    private static final int TOTAL_ARCHIVOS = 3;

    private JProgressBar[] barras;

    private JButton botonIniciar;
    private JButton botonCancelar;

    private JTextArea bitacora;

    public VentanaDescargas() {

        setTitle("Descarga Múltiple");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
}