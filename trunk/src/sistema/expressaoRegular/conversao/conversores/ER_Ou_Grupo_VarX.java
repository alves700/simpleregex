package sistema.expressaoRegular.conversao.conversores;

import java.util.Vector;

import sistema.expressaoRegular.conversao.ParserConversor;
import sistema.expressaoRegular.gramatica.Gramatica;
import sistema.expressaoRegular.gramatica.Grupo;
import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.gramatica.Variavel;
import sistema.expressaoRegular.parser.Nodo;

/**
 * Cria produ��es para ER da forma: +{AY}, sendo Y vari�vel que produz +alfa
 */
public class ER_Ou_Grupo_VarX implements Conversor {
	private static final Conversor _c;
	
	static { _c = new ER_Ou_Grupo_VarX(); }
	
	public static Conversor getInstance() { return _c; }

	@Override
	public int converter(Gramatica g, Vector<Variavel> varPendente, Vector<Variavel> ouPendente, ParserConversor p, Nodo n, int terminaisLidos) {
		Variavel X = ouPendente.remove(0);
		
		Grupo grupo = new Grupo();
		g.addVariavel(grupo);
		
		// Produ��o X->G
		Producao _p = new Producao(X, null);
		_p.addSimboloCorpo(grupo);
		g.addProducao(_p);
		
		// Troca o pendente de X para G
		ouPendente.add(0, grupo);
		
		// Produ��o G->A
		Variavel varA = new Variavel();
		g.addVariavel(varA);
		varPendente.add(0, varA);
		
		_p = new Producao(grupo, null);
		_p.addSimboloCorpo(varA);
		g.addProducao(_p);
		
		return ++terminaisLidos;	// Referente ao +
	}
}