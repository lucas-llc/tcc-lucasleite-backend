package sigma.app.api.model.signature;

import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import sigma.app.api.model.user.User;
import sigma.app.api.object.signature.SignatureDTO;

@Entity
@Table(name="SIGNATURE")
public class Signature {

	public Signature() {}
	
	public Signature(SignatureDTO signatureObject, User user) {
		this.name = signatureObject.getName();
		this.description = signatureObject.getDescription();
		this.price = signatureObject.getPrice();
		this.startDate = signatureObject.getStartDate();
		this.frequency = SignatureFrequency.valueOf(signatureObject.getFrequency());
		this.status = SignatureStatus.valueOf(signatureObject.getStatus());
		this.sendPush = signatureObject.isSendPush();
		this.currency = signatureObject.getCurrency();
		this.iconImage = signatureObject.getIconImage();
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private Double price;
	
	private Date startDate;
	
	@jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
	private SignatureFrequency frequency;
	
	@jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
	private SignatureStatus status;
	
	private boolean sendPush;
	
	private String currency;
	
	private String iconImage;
	
	@jakarta.persistence.ManyToOne(targetEntity=User.class)
	@jakarta.persistence.JoinColumn(name = "USER_ID")
	private User user;

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public SignatureFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(SignatureFrequency frequency) {
		this.frequency = frequency;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public SignatureStatus getStatus() {
		return status;
	}

	public void setStatus(SignatureStatus status) {
		this.status = status;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
