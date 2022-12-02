package objectif3;

public class Enfant extends Parent implements Mangeur {
	
	private Jouet[] jouets;
	
	public void manger(Patate patate) {
		
		if(patate.estBonne()) {
			// Miam
		}else {
			// Beurk
			throw new IndigestionException();
		}
		
		
	}

	
	
}
