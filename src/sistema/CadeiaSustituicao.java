package sistema;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Grupo;
import sistema.expressaoRegular.gramatica.Terminal;

/**
 * Esta classe representa uma cadeia de substitui��o atrav�s de seus strings
 * fixos e �ndices para strings variados.
 * 
 * Tome como exemplo a cadeia de substitui��o: "ab/1/cd/2/df"
 * Nesta cadeia sempre teremos os strings fixos: "ab","cd","df"
 * Entre estes strings fixos teremos strings variadas dependendo da �rvore de deriva��o
 * encontrada pelo parser que ser�o inseridos entre estes strings.
 * 
 * Guardamos ent�o no vetor _Indice somente o n�mero corresponte � ER para criacao
 * da string final.
 *
 * Formato da cadeia �: Fixo[0]Variavel[0]Fixo[1]Variavel[1]...
 */
public class CadeiaSustituicao {
	
	public Vector<String> _Fixo = new Vector<String>();
	public Vector<Indice> _Indice = new Vector<Indice>();
	public boolean realizarDiferenciacao = false;
	
	/**
	 * Cria cadeia de substitui��o j� interpretando os �ndices.
	 * 
	 * @param str - Cadeia de substitui��o com �ndices definidos como /nnn.../,
	 * com n sendo um d�gito, e caracteres definidos como /n com n sendo qualquer
	 * caractere menos d�gito.
	 * Pode limitar a quantidade de substring casadas por um grupo adicionando a quantidade
	 * m�xima ap�s o n�mero do �ndice e os separando por v�rgula: /n,l/ indica �ndice n e
	 * no m�ximo l substrings casadas. � importante lembrar que sobre � adicionada as primeiras
	 * l substrings casadas.
	 */
	public CadeiaSustituicao(String str, int maxIndiceGramatica) {
		
		for (int i = 0; i < str.length(); i++) {
			
			if (str.charAt(i) == Terminal.escape) {
				
				/**
				 * Caso seja um terminal
				 */
				if (!Character.isDigit(str.charAt(i+1))) {
					// N�o altera o texto, para n�o ocorrer em erro na entrada de outra regra de reescrita.
					i++;
					continue;
					
				/**
				 * Caso seja um identificador
				 */
				} else {
					/**
					 * Adicionando parte fixa at� o identificador de grupo
					 */
					_Fixo.add( str.substring(0, i) );		// Adicionando parte fixa
					str = str.substring(i, str.length());	// Alterando para processar o resto da string
					
					/**
					 * Processar o identificador de grupo
					 */
					i = getNumero(str);		// Adquirindo o �ndice do caractere ap�s o n�mero
					
					// Encontrado o n�mero do �ndice do grupo na express�o regular, adicion�-lo
					String numero = str.substring(1, i);
					_Indice.add(new Indice(numero));
					
					/**
					 * Verificar se o �ndice do grupo possui limitador l /n,l/
					 */
					if (str.charAt(i) == ',') {
						int n = getNumero(str.substring(i, str.length()));		// Substring desde i[,] at� length()-1 
						_Indice.lastElement().limitador = new Integer(str.substring(i+1, i+n));
						i += n;		// Apontar para caractere '/'
					}
					
					/**
					 * Verificar se o �ndice do grupo possui diferencia��o entre substrings /nD/ ou /n,lD/
					 */
					if (str.charAt(i) == 'D') {
						realizarDiferenciacao = true;
						i++; // Apontar para caractere '/'
					}
					
					/**
					 * Verificar erro de sintaxe
					 */
					// Erro caso n�o seja o caractere de escape '/'
					if (str.charAt(i) != Terminal.escape) {
						System.err.println("Erro de sintaxe\nSubstring da cadeia de substitui��o fora do padr�o: " + str);
						System.exit(1);
					}
					
					// Verificar se existe tal grupo na express�o regular
					if (maxIndiceGramatica < _Indice.lastElement().ind) {
						System.err.println("Erro de Sintaxe\nCadeia de substitui��o " + str + " com �ndice errado: " + _Indice.lastElement());
						System.exit(1);
					}
					
					/**
					 * Retirando parte processada da string
					 */
					str = str.substring(i+1, str.length());
					i=-1;			// Volta ao �ndice 0
				}
			}
		}
		
		if (str.length()>0)			// Caso sobre s�mbolos fixos
			_Fixo.add( str );
	}
	
	private int getNumero(String str) {
		int i;
		for (i = 1; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)))
				continue;
			break;
		}
		
		return i;
	}
	
	public class Indice {
		public int ind;
		public int limitador;
		
		public Indice(String indice) {
			ind = new Integer(indice);
			limitador = 0;
		}
	}

	public String gerarString(Gramatica gramatica) {
		Vector<Grupo> grupos = new Vector<Grupo>();
		
		// Criando um vetor ordenado somente de grupos
		for (int g = 0; g <= gramatica._MaxIndice; g++) {		// Adicionar o �ndice 0, depois 1, 2, 3 ... at� _MaxIndice
			for (int i = 0; i < gramatica._V.size(); i++) {
				if (gramatica._V.elementAt(i) instanceof Grupo && ((Grupo) gramatica._V.elementAt(i))._IndiceGrupo == g) {
					Grupo grupo = (Grupo) gramatica._V.elementAt(i);
					grupos.add(grupo);
					
					// Caso precise diferenciar as substrings dos grupos
					if (realizarDiferenciacao) {
						for (int s1 = 0; s1 < grupo.substrings.size(); s1++) {
							for (int s2 = s1+1; s2 < grupo.substrings.size(); s2++) {
								if (grupo.substrings.elementAt(s1).equals(grupo.substrings.elementAt(s2))) {
									grupo.substrings.removeElementAt(s2--);		// Decrementar para pesquisar o pr�ximo
								}
							}
						}
					}
				}
			}
		}
		
		// Criando a string
		String str = _Fixo.firstElement();
		
		// Adicionar todas as substrings vari�veis e fixas
		for (int i = 0; i < _Indice.size(); i++) {
			str += getSubstring( grupos.elementAt(_Indice.elementAt(i).ind).substrings, _Indice.elementAt(i).limitador);
			if (i+1<_Fixo.size()) str += _Fixo.elementAt(i+1);
		}
		
		// Retirando os caracteres de escape
		String strSemEscape = "";
		for (int j = 0; j < str.length(); j++) {
			if (str.charAt(j) == Terminal.escape)
				strSemEscape += str.charAt(++j);		// Pular escape
			else
				strSemEscape += str.charAt(j);
		}
		
		return strSemEscape;
	}

	private String getSubstring(Vector<String> substrings, int limitador) {
		int qtdeSubstrings = (limitador==0)?substrings.size():limitador;
		
		String str = "";
		for (int i = 0; i < qtdeSubstrings; i++) {
			str += substrings.elementAt(i);
		}
		
		return str;
	}
}