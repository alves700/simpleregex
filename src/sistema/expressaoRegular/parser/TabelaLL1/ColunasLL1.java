package sistema.expressaoRegular.parser.TabelaLL1;

import java.util.Vector;

import sistema.expressaoRegular.gramatica.Producao;
import sistema.expressaoRegular.parser.Nodo;

public class ColunasLL1 {
	public Vector<Producao> producoes = new Vector<Producao>();

	public Vector<Nodo> getCaminhos(Nodo pai) {
		Vector<Nodo> caminhos = new Vector<Nodo>();
		
		// Gerando todos os caminhos poss�veis de deriva��o
		for (int i = 0; i < producoes.size(); i++) {
			Nodo n = new Nodo(pai);					// Criando novo nodo a partir do pai
			n.derivacao = producoes.elementAt(i);	// Adicionando a produ��o que o derivou
			
			// Retirando a vari�vel derivada do novo nodo e adicionando corpo da produ��o
			n._FormaSentencial.removeElementAt(n.pCharACasar);
			n._FormaSentencial.addAll(n.pCharACasar, producoes.elementAt(i)._Corpo);
			
			caminhos.add(n);
		}
		
		return caminhos;
	}

	public void addProducao(Producao producao) {
		if (producoes.indexOf(producao) == -1){
			producoes.add(producao);
		}
	}
}