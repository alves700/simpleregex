package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produ��es para ER da forma: ({A})
 */
public class ER_Parentes_Grupo_Var implements ConversorER {
	private static final ConversorER _c;
	
	static { _c = new ER_Parentes_Grupo_Var(); }

	public static ConversorER getInstance() { return _c; }

	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		// Incrementa o { do ER_Grupo_Var + 1 para o (
		return ER_Grupo_Var.getInstance().converter(g, varPendente, ouPendente, p, n, terminaisLidos) + 1;
	}
}