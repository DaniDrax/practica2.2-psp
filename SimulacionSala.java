import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SalaConferencias {
    private final int capacidad;
    private int ocupacion = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition hayEspacio = lock.newCondition();

    public SalaConferencias(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean entrar(String nombreEmpleado, int intentos) {
        for (int i = 1; i <= intentos; i++) {
            lock.lock();
            try {
                while (ocupacion >= capacidad) {
                    System.out.println(nombreEmpleado + " intenta entrar pero la sala está llena. Intento " + i);
                    if (i == intentos) {
                        System.out.println(nombreEmpleado + " se rinde después de " + intentos + " intentos.");
                        return false;
                    }
                    hayEspacio.awaitNanos(12_000_000_000L); // 12 segundos
                }
                ocupacion++;
                System.out.println(nombreEmpleado + " entra en la sala. Ocupación actual: " + ocupacion);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public void salir(String nombreEmpleado) {
        lock.lock();
        try {
            ocupacion--;
            System.out.println(nombreEmpleado + " sale de la sala. Ocupación actual: " + ocupacion);
            hayEspacio.signalAll(); // Notifica a los empleados que esperan
        } finally {
            lock.unlock();
        }
    }
}

class Empleado extends Thread {
    private final String nombre;
    private final SalaConferencias sala;

    public Empleado(String nombre, SalaConferencias sala) {
        this.nombre = nombre;
        this.sala = sala;
    }

    @Override
    public void run() {
        int intentos = 3;
        if (sala.entrar(nombre, intentos)) {
            try {
                Thread.sleep(10_000); // 10 segundos en la sala
                System.out.println(nombre + " está trabajando en la sala...");
                Thread.sleep(1_500); // 1,5 segundos para salir
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            sala.salir(nombre);
        }
    }
}

public class SimulacionSala {
    public static void main(String[] args) {
        SalaConferencias sala = new SalaConferencias(5);
        for (int i = 1; i <= 30; i++) {
            new Empleado("Empleado-" + i, sala).start();
        }
    }
}
