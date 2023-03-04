package sigma.app.api.model.signature;

public enum SignatureStatus {

	ATIVO("ATIVO"), INATIVO("INATIVO");
	
	public String value;
	
	private SignatureStatus(String value){
		this.value = value;
	}
}
