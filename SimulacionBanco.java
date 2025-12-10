import java.util.Random;
import java.util.concurrent.Semaphore;

class CuentaBancaria {
    private int saldo = 0;
    private final Semaphore semaforo = new Semaphore(2, true); // máximo 2 clientes simultáneos
    private final Random random = new Random();

    // Método para ingresar dinero
    public void ingresar(String cliente, int cantidad) {
        try {
            semaforo.acquire();
            System.out.println(cliente + " intenta ingresar " + cantidad + "€");
            Thread.sleep((1 + random.nextInt(3)) * 1000L); // entre 1 y 3 segundos
            saldo += cantidad;
            System.out.println(cliente + " ha ingresado " + cantidad + "€. Saldo actual: " + saldo + "€");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
    }

    // Método para retirar dinero
    public void retirar(String cliente, int cantidad) {
        try {
            semaforo.acquire();
            System.out.println(cliente + " intenta retirar " + cantidad + "€");
            Thread.sleep((1 + random.nextInt(3)) * 1000L); // entre 1 y 3 segundos
            if (cantidad <= saldo) {
                saldo -= cantidad;
                System.out.println(cliente + " ha retirado " + cantidad + "€. Saldo actual: " + saldo + "€");
            } else {
                System.out.println(cliente + " no puede retirar " + cantidad + "€. Saldo insuficiente: " + saldo + "€");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
    }

    // Método para consultar saldo
    public void consultarSaldo(String cliente) {
        try {
            semaforo.acquire();
            System.out.println(cliente + " consulta el saldo: " + saldo + "€");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
    }
}

class Cliente extends Thread {
    private final String nombre;
    private final CuentaBancaria cuenta;
    private final Random random = new Random();

    public Cliente(String nombre, CuentaBancaria cuenta) {
        this.nombre = nombre;
        this.cuenta = cuenta;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) { // cada cliente realiza 3 operaciones
            int cantidad = 10 + random.nextInt(991); // entre 10 y 1000
            boolean ingresar = random.nextBoolean(); // decide aleatoriamente si ingresa o retira
            if (ingresar) {
                cuenta.ingresar(nombre, cantidad);
            } else {
                cuenta.retirar(nombre, cantidad);
            }
            cuenta.consultarSaldo(nombre);
        }
        System.out.println(nombre + " ha terminado sus operaciones.");
    }
}

public class SimulacionBanco {
    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria();
        for (int i = 1; i <= 5; i++) {
            new Cliente("Cliente-" + i, cuenta).start();
        }
    }
}

