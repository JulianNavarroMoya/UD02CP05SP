# Caso Práctico 5: Simulación de Ascensor Concurrente

Simula el funcionamiento de un ascensor que traslada usuarios de un piso a otro atendiendo una cola de peticiones de manera ordenada y sincronizada.

---

### El Monitor y Motor (`Ascensor.java`)

* **Cola de espera:** Usa una `LinkedList<Usuario>` para guardar las solicitudes y atenderlas en orden de llegada.
* **Movimiento piso a piso:** Modifica `pisoActual` paso a paso hasta llegar al piso de recogida o destino.
* **Sincronización (`wait` / `notifyAll`):**
    * El ascensor duerme con `wait()` si no hay usuarios en la cola.
    * Al llegar a un piso, avisa con `notifyAll()` para que el usuario correspondiente suba o baje.
    * Cuando se completan todos los viajes, desciende automáticamente al **piso -1** y finaliza la ejecución.

---

Los Pasajeros (`Usuario.java`)

* Cada usuario corre en su propio hilo (`Thread`).
* Tiene un piso de origen y un piso de destino.
* **Paso 1:** Espera dormido en su piso de origen hasta que el ascensor llega y le cede el turno.
* **Paso 2:** Sube, viaja y espera dentro hasta que el ascensor alcanza su piso de destino para bajarse.