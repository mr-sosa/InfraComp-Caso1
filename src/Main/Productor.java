package Main;

public class Productor extends Thread {
	
	private String tipo;
	
	private Main ref;
	
	public Productor(String pTipo, Main refMain) {
		this.tipo = pTipo;
		this.ref = refMain;
	}
	
	public void run() {
		int total = 0;
		int n = ref.numProductos;
		while(total < n) {
			ref.addProd(tipo);
			total++;
		}
	}
}
