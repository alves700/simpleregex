package sistema;

import sistema.expressaoRegular.ExpressaoRegular;

public class RegraReescrita {
	public ExpressaoRegular _ER;
	public CadeiaSustituicao _CadeiaSubstituicao;
	
	public RegraReescrita(String expressaoRegular, String cadeiaSubstituicao) {
		_ER = new ExpressaoRegular(expressaoRegular);
		_CadeiaSubstituicao = new CadeiaSustituicao(cadeiaSubstituicao, _ER._P._G._MaxIndice);
		
		System.out.println("Verificar se os �ndices de cadeia substitui��o existem como grupos na expressao regular");
	}
}