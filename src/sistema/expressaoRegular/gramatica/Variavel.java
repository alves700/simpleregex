package sistema.expressaoRegular.gramatica;

import java.util.Vector;

public class Variavel extends Simbolo{
	
	public char simboloDebug;
	public boolean produzEpsilon = false;
	public Vector<Terminal> _First = new Vector<Terminal>();
	public Vector<Terminal> _Follow = new Vector<Terminal>();
	
	/**
	 * N�mero que identifica a linha em uma tabela LL1, evita
	 * pesquisa e consome menos mem�ria em TabelaLL1.
	 */
	public Integer idLinha;
	
	public Variavel() {
		super(null);
	}
	
	public Variavel(char simbDebug) {	//Debug
		super(null);
		
		simboloDebug = simbDebug;
	}
}