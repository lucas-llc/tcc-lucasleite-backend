package sigma.app.api.object.signature;

import jakarta.validation.constraints.NotBlank;
import sigma.app.api.model.signature.Signature;

public record SignatureDTO(
		Long id,
		@NotBlank
		String name,
		String description,
		Double price) {
	
	public SignatureDTO(Signature signature) {
		this(
				signature.getId(),
				signature.getName(),
				signature.getDescription(),
				signature.getPrice()
			);
	}

}
