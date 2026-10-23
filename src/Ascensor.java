import java.util.LinkedList;

public class Ascensor extends Thread {
    private int pisoActual = 0;
    private final LinkedList<Usuario> colaUsuarios = new LinkedList<>();

    // Estados para coordinar subidas y bajadas
    private Usuario usuarioActual = null;
    private boolean usuarioDentro = false;
    private boolean todasPeticionesEnviadas = false;

    /**
     * Encola la petición de un usuario y espera a completar el viaje.
     */
    public synchronized void solicitarViaje(Usuario usuario) {
        colaUsuarios.add(usuario);
        notifyAll(); // Despierta al ascensor si estaba esperando peticiones

        // 1. Espera a que el ascensor llegue al origen y sea su turno
        while (usuarioActual != usuario || pisoActual != usuario.getPisoOrigen() || usuarioDentro) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // 2. Subir al ascensor
        usuarioDentro = true;
        System.out.println(usuario.getNombre() + " sube al ascensor.");
        notifyAll(); // Notifica al ascensor para que inicie el viaje al destino

        // 3. Esperar dentro hasta llegar al piso destino
        while (pisoActual != usuario.getPisoDestino()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // 4. Bajar del ascensor
        System.out.println(usuario.getNombre() + " baja en piso " + pisoActual);
        usuarioDentro = false;
        usuarioActual = null;
        notifyAll(); // Notifica al ascensor para que atienda al siguiente
    }

    /**
     * Notifica al ascensor que ya no se crearán más usuarios en el sistema.
     */
    public synchronized void finalizarRecepcionPeticiones() {
        todasPeticionesEnviadas = true;
        notifyAll();
    }

    @Override
    public void run() {
        while (true) {
            Usuario siguiente;
            synchronized (this) {
                // Si no hay usuarios en la cola, espera
                while (colaUsuarios.isEmpty() && !todasPeticionesEnviadas) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                // Si ya no hay usuarios y se terminaron las peticiones, salimos del bucle
                if (colaUsuarios.isEmpty() && todasPeticionesEnviadas) {
                    break;
                }

                siguiente = colaUsuarios.poll();
                usuarioActual = siguiente;
            }

            // Desplazarse al piso origen del usuario
            moverHacia(siguiente.getPisoOrigen());

            // Esperar a que el usuario suba
            synchronized (this) {
                notifyAll(); // Avisa al usuario de que el ascensor llegó a su origen
                while (!usuarioDentro) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            // Desplazarse con el usuario al piso destino
            moverHacia(siguiente.getPisoDestino());

            // Esperar a que el usuario baje
            synchronized (this) {
                notifyAll(); // Avisa al usuario de que ha llegado al destino
                while (usuarioActual != null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }

        // Movimiento de vuelta al piso -1 cuando ya no hay usuarios pendientes
        moverHacia(-1);
        System.out.println("Ascensor llegó a piso -1. Fin del programa.");
    }

    /**
     * Simula el movimiento piso por piso hacia el piso objetivo.
     */
    private void moverHacia(int pisoObjetivo) {
        while (pisoActual != pisoObjetivo) {
            if (pisoActual < pisoObjetivo) {
                pisoActual++;
            } else {
                pisoActual--;
            }

            System.out.println("Ascensor en piso " + pisoActual);

            try {
                // Pequeña pausa para simular el trayecto físico entre pisos
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}