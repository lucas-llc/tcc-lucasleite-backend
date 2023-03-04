package sigma.app.api.object.signature;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import sigma.app.api.model.signature.Signature;

public record SignatureDTO(
		Long id,
		@NotBlank
		String name,
		String description,
		Double price,
		Date startDate,
		String frequency,
		String status,
		boolean sendPush,
		String currency,
		String iconImage) {
	
	public SignatureDTO(Signature signature) {
		this(
				signature.getId(),
				signature.getName(),
				signature.getDescription(),
				signature.getPrice(),
				signature.getStartDate(),
				signature.getFrequency().value,
				signature.getStatus().value,
				signature.isSendPush(),
				signature.getCurrency(),
				signature.getIconImage()
			);
	}

}
