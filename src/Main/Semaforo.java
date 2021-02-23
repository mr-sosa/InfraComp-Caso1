package Main;

public class Semaforo {
	
	// Recursos disponibles
    private int contador;

    // Constructor
    public Semaforo(int contador) {
        this.contador = contador;
    }

    public synchronized void p() {
        contador--;
        if (contador < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public synchronized void v() {
        contador++;
        if (contador <= 0) {
            notify();
        }
    }
}
