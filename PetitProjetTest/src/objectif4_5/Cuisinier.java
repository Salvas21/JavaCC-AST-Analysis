package objectif4_5;

public class Cuisinier {

	public void prepareOrange(Orange orange) {
		orange.couper();
		Orange.retirerPepins(orange);
		
		orange.couper();


		System.out.println( "C'est pret" );
	}

	public void testABC() {

		C c = (new A()).b().c();
		
		c.toString();
		this.testABC();
	}

}
