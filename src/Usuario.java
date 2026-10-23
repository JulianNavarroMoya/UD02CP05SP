public class Usuario implements Runnable {
    private final String nombre;
    private final int pisoOrigen;
    private final int pisoDestino;
    private final Ascensor ascensor;

    public Usuario(String nombre, int pisoOrigen, int pisoDestino, Ascensor ascensor) {
        this.nombre = nombre;
        this.pisoOrigen = pisoOrigen;
        this.pisoDestino = pisoDestino;
        this.ascensor = ascensor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPisoOrigen() {
        return pisoOrigen;
    }

    public int getPisoDestino() {
        return pisoDestino;
    }

    @Override
    public void run() {
        System.out.println(nombre + " espera en piso " + pisoOrigen);
        ascensor.solicitarViaje(this);
    }
}