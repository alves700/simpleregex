package sistema.expressaoRegular.gramatica;

public class Terminal extends Simbolo {
	/**
	 * S�mbolo Epsilon
	 */
	public static char epsilon = 0;
	
	public Terminal(char caractere) {
		super(caractere);
	}
	
	public static boolean isEpsilon(Terminal t) { return t._caractere==epsilon; }
	
	public static Terminal getEpsisolon() { return new Terminal(epsilon); }
	
	@Override
	public int hashCode(){
		return this._caractere.hashCode();
	}
	
	@Override
	public Object clone() {
		return new Terminal(this._caractere);		// N�o utilizado
	}
}