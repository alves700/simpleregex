package sistema;

import java.util.Vector;

public class CadeiaSustituicao {
	public Vector<String> _Fixo = new Vector<String>();
	public Vector<Integer> _Indice = new Vector<Integer>();
	public int _MaxIndice=-1;		// Para verificar se existe at� tal �ndice na express�o regular.
	
	/**
	 * Cria cadeia de substitui��o j� interpretando os �ndices.
	 * 
	 * @param str - Cadeia de substitui��o com �ndices definidos como /nnn/,
	 * com n sendo um d�gito, e caracteres definidos como /n com n sendo qualquer
	 * caractere menos d�gito.  
	 */
	public CadeiaSustituicao(String str) {
		
		for (int i = 0; i < str.length(); i++) {
			
			if (str.charAt(i) == '\\') {
				// Caso seja um terminal
				if (!Character.isDigit(str.charAt(i+1))) {
					// N�o altera o texto, para n�o ocorrer em erro na entrada de outra regra de reescrita.
					i++;
					continue;
					
				// Caso seja um identificador
				} else {
					_Fixo.add( str.substring(0, i) );		// Adicionando parte fixa
					str = str.substring(i, str.length());	// Alterando para processar o resto da string
					
					i = getIndiceFinal(str); 	// �ndice do caractere \
					String numero = str.substring(1, i);
					_Indice.add(new Integer(numero));	// Adicionando o n�mero do �ndice
					
					// Para verificar se existe tal grupo na experss�o regular
					if (_MaxIndice < _Indice.lastElement())
						_MaxIndice = _Indice.lastElement();
					
					// Eliminando sobra da string
					str = str.substring(i+1, str.length());
					i=-1;
				}
			}
		}
		
		if (str.length()>0)
			_Fixo.add( str );
	}

	private int getIndiceFinal(String str) {
		int i;
		for (i = 1; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)))
				continue;
			break;
		}
		
		if (str.charAt(i) != '\\') {
			System.out.println("Erro de sintaxe\nSubstring fora do padr�o: " + str);
			System.exit(1);
		}
		return i;
	}
}