package Main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;


public class Main {

	private Semaforo[] semaforos;

	private ArrayList<String> buzonesProd;

	public int limitBuzonProd;

	private ArrayList<String> buzonesCons;

	public int limitBuzonCons;

	private String buzonInter;

	public int numProductos;

	private static int nProdCons;

	public ArrayList<String> getBuzonProd() {
		return buzonesProd;
	}

	public ArrayList<String> getBuzonCons() {
		return buzonesCons;
	}

	public synchronized void addProd(String pProducto) {
		boolean continuar = true;
		while (continuar) {
			if(buzonesProd.size() < limitBuzonProd) {
				buzonesProd.add(pProducto);
				continuar = false;
				System.out.println("Producto "+pProducto+" creado");
			}else {
				Productor.yield();
			}
		}		
	}

	public void getProd(String pProducto) {
		boolean continuar = true;
		while (continuar) {			
			synchronized(buzonesCons) {
				if(buzonesCons.contains(pProducto)) {					
					for(int i=0; i<buzonesCons.size() && continuar; i++) {
						if(buzonesCons.get(i).equals(pProducto)) {
							buzonesCons.remove(i);
							semaforos[2].v();
							continuar = false;
							System.out.println("Producto "+pProducto+" consumido");
						}
					}				
				}else {
					Consumidor.yield();
				}
			}
		}
	}

	public void inter1() {
		int count = 0;		
		while(count < (numProductos * nProdCons)) {
			synchronized(buzonesProd) {
				if(buzonInter.equals("") && buzonesProd.size() > 0) {
					buzonInter = buzonesProd.remove(0);
					System.out.println("In "+ buzonInter);
					semaforos[1].v();
					count ++;			
				}
			}
			semaforos[0].p();
		}
		System.out.println("fn in");
	}

	public void inter2() {

		int count = 0;		
		while(count < (numProductos * nProdCons)) {
			semaforos[1].p();
			synchronized(buzonesCons) {
				if(buzonesCons.size() <= limitBuzonCons) {
					buzonesCons.add(buzonInter);
					System.out.println("Out "+buzonInter);
					buzonInter = "";
					count ++;
					semaforos[0].v();
					semaforos[3].v();
				}else {
					semaforos[2].p();
				}
			}
		}
		System.out.println("fn out");
	}

	public Main(int pNumProductos, int pBuzonesProd, int pBuzonesCons) {
		this.semaforos = new Semaforo[] {new Semaforo(0), new Semaforo(0), new Semaforo(0), new Semaforo(0)};
		this.buzonesProd = new ArrayList<String>();
		this.buzonesCons = new ArrayList<String>();
		this.buzonInter = "";
		this.limitBuzonProd = pBuzonesProd;
		this.limitBuzonCons = pBuzonesCons;
		this.numProductos = pNumProductos; 
	}

	public static void main(String[] args) {
		Properties concurrencia = new Properties();
		InputStream configInput = null;	    
		try{
			configInput = new FileInputStream("data/concurrencia.properties");
			concurrencia.load(configInput);
			System.out.println(concurrencia.getProperty("concurrencia.numProdCons")+" productores y consumidores");
			System.out.println(concurrencia.getProperty("concurrencia.numProductos")+" productos");
			System.out.println(concurrencia.getProperty("concurrencia.buzonesProd")+" buzones productores");
			System.out.println(concurrencia.getProperty("concurrencia.buzonesCons")+" buzones consumidores");
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error cargando configuración\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}		
		int numProductos = Integer.parseInt (concurrencia.getProperty("concurrencia.numProductos"));
		int numBuzProd = Integer.parseInt (concurrencia.getProperty("concurrencia.buzonesProd"));
		int numBuzCons = Integer.parseInt (concurrencia.getProperty("concurrencia.buzonesCons"));
		nProdCons = Integer.parseInt (concurrencia.getProperty("concurrencia.numProdCons"));
		Main main = new Main(numProductos,numBuzProd,numBuzCons);

		for(int i=0; i<nProdCons;i++) {
			if(i<(nProdCons/2)) {
				new Productor("A"+i, main).start();
				new Consumidor("A"+i, main).start();
			}else {
				new Productor("B"+i, main).start();
				new Consumidor("B"+i, main).start();
			}
		}

		new Intermediario(1, main).start();
		new Intermediario(2, main).start();

	}
}
