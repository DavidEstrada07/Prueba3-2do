public class ContadorDescargas {

    private int completadas;

    public ContadorDescargas() {
        completadas = 0;
    }

    public synchronized void reiniciar() {
        completadas = 0;
    }

    public synchronized int registrarCompletada() {
        completadas++;
        return completadas;
    }
}