package sistema.expressaoRegular.parser.TabelaLL1;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;

public class TabelaLL1 {
	public Vector<LinhaLL1> _Linhas = new Vector<LinhaLL1>();
	
	public TabelaLL1() {
		super();
	}
	
	public TabelaLL1(Gramatica _G) {
		super();
		
		/**
		 * Percorrer todas as produ��es A->BCDE... e analisar cada s�mbolo
		 */
		for (int i = 0; i < _G._P.size(); i++) {
			Producao prod = _G._P.elementAt(i);
			
			// Percorrer cada s�mbolo da produ��o
			for (int s = 0; s < prod._Corpo.size(); s++) {
				Simbolo simb = prod._Corpo.elementAt(s);
				
				/**
				 * Caso o s�mbolo Z seja de A->BCDE...Z e Z deriva epsilon, adicionar produ��o � Follow(A)
				 */
				if (s == prod._Corpo.size()-1) {
					if (	(simb._caractere != null && simb._caractere == Terminal.epsilon) ||
							(simb instanceof Variavel && ((Variavel)simb).produzEpsilon) ) {
						addProducaoEmFollow(prod);
					}
				}
				
				/**
				 * Adicionar produ��o A->BCDE... para cada terminal em First (BCDE...)
				 */
				if (simb instanceof Variavel) {
					Variavel varB = (Variavel) simb;
					
					// Adicionando produ��o nos terminais First
					addProducaoEmFirstMenosEpsilon(prod, varB._First);
					
					// Percorrer pr�ximo s�mbolo da producao somente se este derivar epsilon
					if (!varB.produzEpsilon) { break; }
					
				// Caso seja um terminal, diferente de epsilon
				} else {
					if (simb._caractere != Terminal.epsilon)
						addProducao(prod._V, (Terminal) simb, prod);
					break;	// N�o percorrer pr�ximos s�mbolos
				}
				
			}
		}
		
		/**
		 * Limpando produ��es
		 */
		for (int i = 0; i < _G._V.size(); i++) {
			_G._V.elementAt(i)._First = null;
			_G._V.elementAt(i)._Follow = null;
		}
	}
	
	private void addProducaoEmFollow(Producao prod) {
		// Para cada terminal em Follow
		for (int i = 0; i < prod._V._Follow.size(); i++) {
			// Adicionar produ��o ao terminal
			addProducao(prod._V, prod._V._Follow.elementAt(i), prod);
		}
		
	}

	private void addProducaoEmFirstMenosEpsilon(Producao prod,  Vector<Terminal> grupoFirst) {
		// Para cada terminal em First de B...
		for (int t = 0; t < grupoFirst.size(); t++) {
			// Adicionar somente caso n�o seja epsilon 
			if (!grupoFirst.elementAt(t).equals(Terminal.epsilon)) {
				addProducao(prod._V, grupoFirst.elementAt(t), prod);
			}
		}
	}
	
	/**
	 * Adiciona uma produ��o � tabela LL(1)
	 */
	public void addProducao(Variavel variavel, Terminal terminal, Producao producao) {
		// Adicionando o endere�o da linha da tabela LL(1) � vari�vel
		if (variavel.idLinha == null) {
			variavel.idLinha = _Linhas.size();
			_Linhas.add(new LinhaLL1());
		}
		LinhaLL1 var = _Linhas.elementAt(variavel.idLinha);
		
		// Adicionando o mapeamento dos terminais da coluna
		if (!var.containsKey(terminal)) {
			var.put(terminal, new ColunasLL1());
		}
		var.get(terminal).addProducao(producao);
	}
	
	/**
	 * Retorna a coluna com as deriva��es de uma vari�vel a partir de um terminal
	 */
	public ColunasLL1 getDerivacoesLL1(Variavel v, Terminal t) {
		return _Linhas.elementAt(v.idLinha).get(t);
	}
}