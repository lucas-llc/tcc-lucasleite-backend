package sigma.app.api.object.keywords;

import sigma.app.api.model.keywords.Keywords;

public class KeywordsDTO {

	private long id;
	private String name;
	private String description;
	private Long userId;
	private Long signatureCount;
	
	public KeywordsDTO() {
		
	}
	
	public KeywordsDTO(Keywords keywords) {
		this.id = keywords.getId();
		this.name = keywords.getName();
		this.description = keywords.getDescription();
		this.userId = keywords.getUser().getId();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSignatureCount() {
		return signatureCount;
	}

	public void setSignatureCount(Long signatureCount) {
		this.signatureCount = signatureCount;
	}
	
}
