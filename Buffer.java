// Buffer.java
public class Buffer {
    private int capacity = 10;  // Capacidad máxima del buffer
    private int count = 0;      // Elementos actuales en el buffer

    // Método para producir un elemento
    public synchronized void producir() throws InterruptedException {
        while (count == capacity) {
            System.out.println("El búfer está lleno, esperando...");
            wait(); // Espera hasta que haya espacio
        }
        count++;
        System.out.println("Producido 1 elemento. Elementos en el búfer: " + count);
        notifyAll(); // Despierta a los hilos que están esperando
    }

    // Método para consumir un elemento
    public synchronized void consumir() throws InterruptedException {
        while (count == 0) {
            System.out.println("El búfer está vacío, esperando...");
            wait(); // Espera hasta que haya elementos
        }
        count--;
        System.out.println("Consumido 1 elemento. Elementos en el búfer: " + count);
        notifyAll(); // Despierta a los hilos que están esperando
    }
}
