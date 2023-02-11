package sigma.app.api.model.signature;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sigma.app.api.object.signature.SignatureDTO;

@Entity
@Table(name="signature")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Signature {

	public Signature(SignatureDTO signatureObject) {
		this.name = signatureObject.name();
		this.description = signatureObject.description();
		this.price = signatureObject.price();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private Double price;

}
