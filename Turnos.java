public class Turnos {
    private int turno = 0; // 0 -> A, 1 -> B, 2 -> C

    public synchronized void esperarTurno(int miTurno) throws InterruptedException {
        while (turno != miTurno) {
            wait();
        }
    }

    public synchronized void siguiente() {
        turno = (turno + 1) % 3;
        notifyAll();
    }
    
    
}