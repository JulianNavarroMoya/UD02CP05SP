public class Main {
    public static void main(String[] args) {
        Ascensor ascensor = new Ascensor();
        ascensor.start();

        Thread u1 = new Thread(new Usuario("Usuario 1", 0, 3, ascensor));
        Thread u2 = new Thread(new Usuario("Usuario 2", 2, -1, ascensor));
        Thread u3 = new Thread(new Usuario("Usuario 3", 1, 5, ascensor));

        // Lanzamiento escalonado para reproducir el orden de llegada del caso práctico
        try {
            u1.start();
            Thread.sleep(100);

            u2.start();
            Thread.sleep(100);

            u3.start();

            // Esperar a que todos los hilos de usuarios finalicen
            u1.join();
            u2.join();
            u3.join();

            // Notificar al ascensor que no habrá más peticiones para que baje a -1 y termine
            ascensor.finalizarRecepcionPeticiones();
            ascensor.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}