package sistema.expressaoRegular.parser;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Simbolo;

public class Nodo {
	// Representa��o da forma sentencial
	public Vector<Simbolo> _FormaSentencial = new Vector<Simbolo>();
	
	// Caminhos diferentes de deriva��o
	private Vector<Derivacao> _Caminhos = new Vector<Derivacao>();
	
	// Caractere � casar
	public int pCharACasar;
	
	public Nodo(int charACasar, Simbolo simbolo) {
		pCharACasar = charACasar;
		_FormaSentencial.add(simbolo);
	}
}