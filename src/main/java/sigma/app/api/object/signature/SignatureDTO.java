package sigma.app.api.object.signature;

import jakarta.validation.constraints.NotBlank;

public record SignatureDTO(
		Long id,
		@NotBlank
		String name,
		String description,
		Double price) {

}
