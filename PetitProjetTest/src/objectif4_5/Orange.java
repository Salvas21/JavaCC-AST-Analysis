package objectif4_5;

public class Orange {

	private int nbPepins;

	public Orange(int nbPepins) {
		this.nbPepins = nbPepins;
	}

	public void couper() {
	}

	public static void retirerPepins(Orange o) {
		o.nbPepins = 0;
		
	}

}
