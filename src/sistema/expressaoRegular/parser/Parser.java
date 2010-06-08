package sistema.expressaoRegular.parser;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;

public class Parser {
	// Gram�tica com tabela LL(1) e string a ser casada.
	private Gramatica _G;
	private String _Str;
	
	// Senten�a formal da deriva��o
	private Nodo _FS;
	
	public Parser(Gramatica grammatica) {
		_G = grammatica;
	}
	
	/**
	 * Iniciar o parser para encontrar deriva��es
	 * 
	 * @param str - String a ser interpretada, casada
	 */
	public void iniciar(String str) {
		_Str = str;
		_FS = new Nodo(0, _G._VariavelInicial);
	}
	
	/**
	 * Realiza os passos do parser LL(1)
	 */
	private boolean proximoPasso() {
		// Verificar se chegou no fim da string de entrada
		if (_Str.length()>= _FS.pCharACasar) {
			return true;
		}
		
		// Adquirindo s�mbolo mais a esquerda � casar e caractere de entrada
		Simbolo sEsq = _FS._FormaSentencial.elementAt(_FS.pCharACasar);
		char charEntrada = _Str.charAt(_FS.pCharACasar);
		
		/**
		 * Caso o s�mbolo da forma sentencial for um terminal
		 */
		if (sEsq.isTerminal()) {
			// Se o s�mbolo da forma sentencial for correto
			if (sEsq.equals(charEntrada)) {
				_FS.pCharACasar++;
				return proximoPasso();		// Casar pr�ximo caractere
				
			// Se o s�mbolo da forma sentencial for errado
			} else {
				return false;		// Abortar deriva��o
			}
		
		/**
		 * Caso o s�mbolo da forma sentencial for uma vari�vel
		 */
		} else {
			ColunasLL1 derivacoes = _G._TabLL1.getDerivacoesLL1((Variavel) sEsq, charEntrada);
			
		}
		
		return false;
	}
	
	public Nodo getNextDerivacao() {
		
		
		return null;
	}
	
	public void limpar() {
		
	}
}