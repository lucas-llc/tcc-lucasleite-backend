package sigma.app.api.object.user;

import jakarta.validation.constraints.NotBlank;
import sigma.app.api.model.user.User;

public record UserDTO(
		Long id,
		@NotBlank
		String name,
		@NotBlank
		String email,
		@NotBlank
		String password) {
	
	public UserDTO(User user) {
		this(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getPassword()
			);
	}

}
