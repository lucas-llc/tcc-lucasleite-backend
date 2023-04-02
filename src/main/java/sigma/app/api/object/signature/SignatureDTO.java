package sigma.app.api.object.signature;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sigma.app.api.model.keywords.Keywords;
import sigma.app.api.model.signature.Signature;
import sigma.app.api.object.keywords.KeywordsDTO;

public class SignatureDTO {
	
	private Long id;
	private String name;
	private String description;
	private Date startDate;
	private Double price;
	private String status;
	private String frequency;
	private boolean sendPush;
	private String currency;
	private String iconImage;
	private Long userId;
	private Date nextPaymentDate;
	private List<KeywordsDTO> keywords;
	
	public SignatureDTO() {
		
	}
	
	public SignatureDTO(Signature signature) {
		this.id = signature.getId();
		this.name = signature.getName();
		this.description = signature.getDescription();
		this.startDate = signature.getStartDate();
		this.price = signature.getPrice();
		this.status = signature.getStatus().value;
		this.frequency = signature.getFrequency().value;
		this.sendPush = signature.isSendPush();
		this.currency = signature.getCurrency();
		this.iconImage = signature.getIconImage();
		this.userId = signature.getUser().getId();
		
		this.keywords = new ArrayList<KeywordsDTO>();
		if(signature.getKeywords() != null) {
			for (Keywords keywords : signature.getKeywords()) {
				KeywordsDTO keywordsDTO = new KeywordsDTO();
				keywordsDTO.setId(keywords.getId());
				keywordsDTO.setName(keywords.getName());
				keywordsDTO.setDescription(keywords.getDescription());
				this.keywords.add(keywordsDTO);
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public boolean isSendPush() {
		return sendPush;
	}

	public void setSendPush(boolean sendPush) {
		this.sendPush = sendPush;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIconImage() {
		return iconImage;
	}

	public void setIconImage(String iconImage) {
		this.iconImage = iconImage;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getNextPaymentDate() {
		return nextPaymentDate;
	}

	public void setNextPaymentDate(Date nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public List<KeywordsDTO> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordsDTO> keywords) {
		this.keywords = keywords;
	}
	
}
