package sistema.expressaoRegular.gramatica;

public class Variavel extends Simbolo{
	
	public boolean produzEpsilon = false;
	
	/**
	 * N�mero que identifica a linha em uma tabela LL1, evita
	 * pesquisa e consome menos mem�ria em TabelaLL1.
	 */
	public Integer idLinha;
	
	public Variavel() {
		super(null);
	}
	
	@Override
	public Object clone() {
		Variavel v = new Variavel();
		v.idLinha = this.idLinha;
		v.produzEpsilon = this.produzEpsilon;
		return v;
	}
}