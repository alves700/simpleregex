package sistema;

import java.util.Vector;

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
	public Vector<Integer> _Indice = new Vector<Integer>();
	
	/**
	 * Cria cadeia de substitui��o j� interpretando os �ndices.
	 * 
	 * @param str - Cadeia de substitui��o com �ndices definidos como /nnn/,
	 * com n sendo um d�gito, e caracteres definidos como /n com n sendo qualquer
	 * caractere menos d�gito.  
	 */
	public CadeiaSustituicao(String str, int maxIndiceGramatica) {
		
		for (int i = 0; i < str.length(); i++) {
			
			if (str.charAt(i) == Terminal.escape) {
				// Caso seja um terminal
				if (!Character.isDigit(str.charAt(i+1))) {
					// N�o altera o texto, para n�o ocorrer em erro na entrada de outra regra de reescrita.
					i++;
					continue;
					
				// Caso seja um identificador
				} else {
					_Fixo.add( str.substring(0, i) );		// Adicionando parte fixa
					str = str.substring(i, str.length());	// Alterando para processar o resto da string
					
					i = getIndiceFinal(str); 	// �ndice do caractere /
					String numero = str.substring(1, i);
					_Indice.add(new Integer(numero));	// Adicionando o n�mero do �ndice
					
					// Verificar se existe tal grupo na express�o regular
					if (maxIndiceGramatica < _Indice.lastElement()) {
						System.err.println("Erro de Sintaxe\nCadeia de substitui��o " + str + " com �ndice errado: " + _Indice.lastElement());
						System.exit(1);
					}
					
					// Eliminando sobra da string
					str = str.substring(i+1, str.length());
					i=-1;			// Volta ao �ndice 0
				}
			}
		}
		
		if (str.length()>0)			// Caso sobre s�mbolos fixos
			_Fixo.add( str );
	}

	private int getIndiceFinal(String str) {
		int i;
		for (i = 1; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)))
				continue;
			break;
		}
		
		if (str.charAt(i) != Terminal.escape) {
			System.err.println("Erro de sintaxe\nSubstring fora do padr�o: " + str);
			System.exit(1);
		}
		return i;
	}
}