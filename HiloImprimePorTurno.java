
public class HiloImprimePorTurno extends Thread {
    private Turnos turno;
    private int miTurno;
    private int inicio;
    private int paso;

    public HiloImprimePorTurno(Turnos turno, int miTurno, int inicio, int paso) {
        this.turno = turno;
        this.miTurno = miTurno;
        this.inicio = inicio;
        this.paso = paso;
    }


    public void run() {
        for (int i = inicio; i <= 10; i += 3) {
            try {
                turno.esperarTurno(miTurno);
                System.out.println(Thread.currentThread().getName() + " imprime: " + i);
                turno.siguiente();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        Turnos turno = new Turnos();

        HiloImprimePorTurno a = new HiloImprimePorTurno(turno, 0, 1, 3);
        HiloImprimePorTurno b = new HiloImprimePorTurno(turno, 1, 2, 3);
        HiloImprimePorTurno c = new HiloImprimePorTurno(turno, 2, 3, 3);

        a.setName("A"); 
        b.setName("B");
        c.setName("C");

        a.start(); 
        b.start(); 
        c.start();

        a.join();
        b.join(); 
        c.join();
        System.out.println("Fin impresión ordenada.");
    }

}
