package Main;

public class Intermediario extends Thread {
	
	private int tipo;
	
	private Main ref;
	
	public Intermediario(int pTipo, Main refMain) {
		this.tipo = pTipo;
		this.ref = refMain;
	}
	
	public void run() {
		if(tipo == 1) ref.inter1();
		if(tipo == 2) ref.inter2();
	}
}
