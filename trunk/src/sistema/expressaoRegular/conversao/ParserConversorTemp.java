package sistema.expressaoRegular.conversao;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Simbolo;
import sistema.expressaoRegular.gramatica.Terminal;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;
import sistema.expressaoRegular.parser.TabelaLL1.ColunasLL1;

public class ParserConversorTemp {
	// Gram�tica com tabela LL(1) e string a ser casada.
	private Gramatica _G;
	private String _Str;
	
	// Senten�a formal da deriva��o
	private Nodo _FS;
	private Nodo _NodoPai;
	
	// Vetor para BackTracking do parser
	private Vector<Integer> _BT;
	
	public ParserConversorTemp(Gramatica grammatica) {
		_G = grammatica;
	}
	
	/**
	 * Iniciar o parser para encontrar deriva��es
	 * 
	 * @param str - String a ser interpretada, casada
	 */
	public void iniciar(String str) {
		_Str = str;
		_NodoPai = new Nodo(0, _G._VariavelInicial);
		_FS = _NodoPai;
		_BT = new Vector<Integer>();
	}
	
	/**
	 * Realiza os passos do parser LL(1)
	 */
	private boolean proximoPasso() {
		/**
		 * Verificar se j� casou com todos os i elementos da string de entrada
		 */
		if (_FS.pCharACasar >= _Str.length()) {
			
			// Caso existam ainda mais s�mbolos na forma sentencial
			if (_FS._FormaSentencial.size() > _Str.length()) {
				
				// Todos dever�o ser vari�veis com epsilon em FIRST
				if (isAllEpsilonVariaveis(_FS._FormaSentencial, _FS.pCharACasar)) {
					return true;					// Conseguiu encontrar uma deriva��o
				} else {
					return abortarCaminho();		// Abortar caminho de deriva��o
				}
				
			// Caso n�o existam mais s�mbolos
			} else {
				return true;		// Conseguiu encontrar uma deriva��o
			}
		
		/**
		 * Caso a SENTEN�A gerada seja menor do que a string de entrada
		 */
		} else if (_FS.pCharACasar >= _FS._FormaSentencial.size()) {		// Se for senten�a, ocorrer� um estouro no �ndice pCharCasar
			return abortarCaminho();		// Abortar caminho de deriva��o
		}
		
		
		// Adquirindo s�mbolo mais a esquerda � casar e caractere de entrada
		Simbolo sEsq = _FS._FormaSentencial.elementAt(_FS.pCharACasar);
		Character charEntrada = getCharEntrada(_FS.pCharACasar);
		
		/**
		 * Caso o s�mbolo da forma sentencial for um terminal
		 */
		if (sEsq.isTerminal()) {
			// Se o s�mbolo da forma sentencial for correto
			if (itMatch((Terminal) sEsq, charEntrada)) {
				_FS.pCharACasar++;
				return proximoPasso();			// Casar pr�ximo caractere
				
			// Se o s�mbolo da forma sentencial for errado
			} else {
				return abortarCaminho();		// Abortar caminho de deriva��o
			}
		
		/**
		 * Caso o s�mbolo da forma sentencial for uma vari�vel
		 */
		} else {
			ColunasLL1 derivacoes = getDerivacoesLL1((Variavel) sEsq, charEntrada);
			
			// Caso n�o exista deriva��o para esta vari�vel e terminal
			if (derivacoes == null) {
				return abortarCaminho();	// Abortar caminho atual
			
			// Caso exista, considerando ambiguidade, gerar todos os caminhos possiveis
			} else {
				Vector<Nodo> caminhos = derivacoes.getCaminhos(_FS);	// Indica o nodo pai para os caminhos filhos
				
				_BT.add(0);					// Adicionar primeiro endere�o da bifurca��o para Backtracking
				_FS.setCaminhos(caminhos);	// Adicionando caminhos do nodo
				
				_FS = _FS.getCaminho(0);	// Pesquisando pelo primeiro caminho
				return proximoPasso();
			}
		}
	}

	private boolean isAllEpsilonVariaveis(Vector<Simbolo> variaveis, int indiceInicial) {
		
		for (; indiceInicial < variaveis.size(); indiceInicial++) {
			Simbolo s = variaveis.elementAt(indiceInicial);
			
			if (!(s instanceof Variavel) || !((Variavel)s).produzEpsilon) {
				return false;
			}
		}
		
		return true;
	}

	private boolean abortarCaminho() {
		// N�o existe mais caminhos alternativos
		if (_BT.isEmpty()) {
			return false;
		}
		
		_FS = _FS._Pai;								// Voltar a deriva��o ao nodo Pai
		_FS.eliminarCaminho(_BT.lastElement());		// Eliminar caminho atual. Aponta o _BT.lastElement() para o pr�ximo caminho.
		
		/**
		 * Caso exista um pr�ximo caminho atrav�s do nodo Pai
		 */
		if (_BT.lastElement() < _FS.getMaxBacktracking()) {
			_FS = _FS.getCaminho(_BT.lastElement());		// Pesquisar novo caminho
			return proximoPasso();
			
		/**
		 * Caso n�o exista um pr�ximo caminho atrav�s do nodo Pai
		 */
		} else {
			_BT.remove(_BT.size()-1);		// Eliminar Backtracking do Pai
			return abortarCaminho();		// Pesquisar caminhos alternativos pelo Av�
		}
	}
	
	public Nodo getNextDerivacao() {
		if (proximoPasso()) { return _NodoPai; }
		
		return null;
	}
	
	public void limpar() {
		_Str = null;
		_NodoPai = null;
		_FS = null;
		_BT = null;
		System.gc();
	}
	
	private Character getCharEntrada(int charACasar) {
		return _Str.charAt(charACasar);
	}
	
	private boolean itMatch(Terminal t, Character charEntrada) {
		return t.equals(charEntrada);
	}
	
	private ColunasLL1 getDerivacoesLL1(Variavel linha, Character charFirst) {
		return _G._TabLL1.getDerivacoesLL1(linha, (Character)charFirst);
	}
}