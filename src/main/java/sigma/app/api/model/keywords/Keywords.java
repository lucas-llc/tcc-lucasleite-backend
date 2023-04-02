package sigma.app.api.model.keywords;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import sigma.app.api.model.user.User;
import sigma.app.api.object.keywords.KeywordsDTO;

@Entity
@Table(name="KEYWORDS")
public class Keywords {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;

	@jakarta.persistence.ManyToOne(targetEntity=User.class)
	@jakarta.persistence.JoinColumn(name = "USER_ID")
	private User user;
	
	public Keywords() {}
	
	public Keywords(KeywordsDTO keywordsObject, User user) {
		this.name = keywordsObject.getName();
		this.description = keywordsObject.getDescription();
		this.user = user;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
