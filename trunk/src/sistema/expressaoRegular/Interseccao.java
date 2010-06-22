package sistema.expressaoRegular;

import java.util.Vector;

import sistema.RegraReescrita;

public class Interseccao {

	public static void atualizar(Vector<RegraReescrita> regras) {
		// Verificar a intersec��o de todas com todas
		for (int i = 0; i < regras.size(); i++) {
			RegraReescrita origem = regras.elementAt(i);
			origem.regrasComInterseccao.removeAllElements();		// Limpando a intersec��o
			
			for (int j = 0; j < regras.size(); j++) {
				RegraReescrita destino = regras.elementAt(j);
				
				// Caso exista intersec��o adicionar na lista de regras com intersec��o
				if (existeInterseccao(origem, destino)) {
					origem.regrasComInterseccao.add(destino);
				}
			}
			
		}
	}
	
	/**
	 * M�todo de intersec��o proposto
	 */
	private static boolean existeInterseccao(RegraReescrita regraOriginaria, RegraReescrita regraAlimentada) {
		if (regraOriginaria == null)
			return true;
		
		return true;
	}
}