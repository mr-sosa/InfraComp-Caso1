package Main;

public class Consumidor extends Thread {
	
	private String tipo;
	
	private Main ref;
	
	public Consumidor(String pTipo, Main refMain) {
		this.tipo = pTipo;
		this.ref = refMain;
	}
	
	public void run() {
		int total = 0;
		int n = ref.numProductos;
		while(total < n) {
			ref.getProd(tipo);
			total++;
		}
	}
}
