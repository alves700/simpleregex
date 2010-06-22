package sistema;

import java.util.Vector;

import sistema.expressaoRegular.Interseccao;

public class SistemaReescrita {
	private static boolean cicloIniciado = false;
	private static int i_Entrada;
	private static int i_Regra;
	
	private Vector<Alimentacao> alimentacaoSistema = new Vector<Alimentacao>();
	private Vector<RegraReescrita> regras = new Vector<RegraReescrita>();
	
	/*
	 * Regras de substitui��o
	 */
	public void addRegraReescrita(String expressaoRegular, String cadeiaSubstituicao) {
		regras.add(new RegraReescrita(expressaoRegular, cadeiaSubstituicao));
		Interseccao.atualizar(regras);
	}
	public RegraReescrita removeRegraReescrita(int i) {
		if (mensagemErro()) { return null; }
		
		RegraReescrita regra = regras.remove(i);
		Interseccao.atualizar(regras);
		return regra;
	}
	
	/*
	 * Alimenta��o do sistema
	 */
	public void limparAlimentacao() {
		if (mensagemErro()) { return; }
		
		alimentacaoSistema.removeAllElements();
	}
	public void addAlimentacao(String str) {
		if (mensagemErro()) { return; }
		
		alimentacaoSistema.add(new Alimentacao(str));
	}
	
	/**
	 * Fun��es de controle
	 */
	private boolean mensagemErro() {
		if (processandoCiclo()) {
			System.out.println("Aguarde processar o ciclo.");
			return true;
		}
		
		return false;
	}
	
	public boolean processandoCiclo() { return cicloIniciado; }
	
	/**
	 * Processa uma regra e retorna todas as strings geradas por uma regra.
	 */
	public Vector<String> processarUmaRegra() {
		if (alimentacaoSistema.size() == 0) {
			System.err.println("Sistema n�o alimentado!\nForne�a alguma regra para processamento.");
			cicloIniciado = false;
			return new Vector<String>();
		}
		
		if (!cicloIniciado) {
			cicloIniciado = true;
			i_Entrada = 0;
			i_Regra = 0;
		}
		
		// Processar a entrada i_Entrada com a regra i_Regra
		Alimentacao entrada = alimentacaoSistema.elementAt(i_Entrada);
		Vector<RegraReescrita> vetorRegras = (entrada.regraOriginaria==null)?regras:entrada.regraOriginaria.regrasComInterseccao;
		RegraReescrita regra = vetorRegras.elementAt(i_Regra);
		Vector<String> result =  regra.processarEntrada(entrada.stringEntrada);
		
		// Caso seja a �ltima regra da entrada
		if (i_Regra >= vetorRegras.size()-1) {
			// Avan�ar para a pr�xima entrada, caso ela exista, caso contr�rio terminar ciclo
			if (i_Entrada >= alimentacaoSistema.size()-1) {
				cicloIniciado = false;		// Acabou o processamento do ciclo
				
			} else {
				i_Regra = 0;
				i_Entrada++;
			}
			
		// Caso n�o seja a �ltima regra para a string de entrada, avan�ar
		} else {
			i_Regra++;
		}
		
		return result;
	}
	
	/**
	 * Processa todas as entradas para todas as regras e retorna as string geradas
	 * por todas as regras e entradas, incluindo strings repetidas.
	 */
	public Vector<String> processarUmCiclo() {
		
		Vector<String> result = processarUmaRegra();
		
		while(processandoCiclo()) {
			result.addAll(processarUmaRegra());
		}
		
		return result;
	}
	
	/**
	 * Processa todas as entradas para todas as regras e retorna somente as strings
	 * geradas pelo �ltimo ciclo. Somente ir� parar quando houver um ciclo que n�o
	 * retornar� nenhuma string.
	 */
	public Vector<String> processarTodosCiclos() {
		Vector<String> ultimoResult = processarUmCiclo();
		Vector<String> resultAtual;
		
		while ((resultAtual = processarUmCiclo()).size() > 0) {
			ultimoResult = resultAtual;
		}
		
		return ultimoResult;
	}
	
	/**
	 * Classe interna para controle da alimenta��o do sistema
	 */
	public class Alimentacao {
		public String stringEntrada;
		public RegraReescrita regraOriginaria;
		
		public Alimentacao(String entrada) {
			this.stringEntrada = entrada;
		}
	}
}