package sistema.expressaoRegular.parser;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Simbolo;

public class Parser {
	private Gramatica _G;
	private String _Str;
	
	private Vector<Simbolo> _FormaSentencial;
	private int pCharACasar;
	
	public Parser(Gramatica grammatica) {
		_G = grammatica;
	}
	
	/**
	 * Iniciar o parser para encontrar deriva��es
	 * 
	 * @param str - String a ser interpretada, parseada
	 */
	public void iniciar(String str) {
		_Str = str;
		pCharACasar = 0;
		_FormaSentencial = new Vector<Simbolo>();
		_FormaSentencial.add(_G._VariavelInicial);
	}
	
	/**
	 * @return false - Quando a deriva��o for errada, deve ser descartada
	 */
	private boolean proximoPasso() {
		// Verificar se chegou no fim da string de entrada
		if (_Str.length()>= pCharACasar) {
			return true;
		}
		
		// Adquirindo caractere de entrada e s�mbolo mais a esquerda � casar
		char charEntrada = _Str.charAt(pCharACasar);
		Simbolo sEsq = _FormaSentencial.elementAt(pCharACasar);
		
		/**
		 * Caso o s�mbolo da senten�a formal for um terminal
		 */
		if (sEsq.isTerminal()) {
			// Se o s�mbolo da forma sentencial for correto
			if (sEsq.equals(charEntrada)) {
				return proximoPasso();
				
			// Se o s�mbolo da forma sentencial for errado
			} else {
				return false;		// Abortar deriva��o
			}
		
		/**
		 * Caso o s�mbolo da senten�a formal for uma vari�vel
		 */
		} else {
			
		}
		
		return false;
	}
	
	public Nodo getNextDerivacao() {
		
		
		return null;
	}
	
	public void limpar() {
		
	}
}