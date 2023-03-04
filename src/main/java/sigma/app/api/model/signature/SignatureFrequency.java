package sigma.app.api.model.signature;

public enum SignatureFrequency {

	MENSAL("MENSAL"), BIMESTRAL("BIMESTRAL"), TRIMESTRAL("TRIMESTRAL"), SEMESTRAL("SEMESTRAL"), ANUAL("ANUAL");
	
	public String value;
	
	private SignatureFrequency(String value){
		this.value = value;
	}
}
