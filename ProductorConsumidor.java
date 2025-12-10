// ProductorConsumidor.java
public class ProductorConsumidor {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        // Hilo productor
        Thread productor = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    buffer.producir();
                    Thread.sleep(500); // Simula tiempo de producción
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Hilo consumidor
        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    buffer.consumir();
                    Thread.sleep(1000); // Simula tiempo de consumo
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Iniciar ambos hilos
        productor.start();
        consumidor.start();

        // Esperar a que terminen ambos hilos
        try {
            productor.join();
            consumidor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Fin de la simulación productor-consumidor.");
    }
}

