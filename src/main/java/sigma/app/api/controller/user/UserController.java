package sigma.app.api.controller.user;

import java.net.URI;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import sigma.app.api.model.user.User;
import sigma.app.api.object.user.UserDTO;
import sigma.app.api.repository.user.UserRepository;
import sigma.app.api.service.TokenService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TokenService tokenService;
	
	public UserDetails getUserByToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null) {
			String tokenJWT =  authorizationHeader.replace("Bearer ", "");
			String subject = tokenService.getSubject(tokenJWT);
			UserDetails user = userRepository.findByEmail(subject);
			return user;
		}
		return null;
	}

	@PostMapping
	@Transactional
	public  ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userObject, UriComponentsBuilder uriBuilder) {
		User user = new User(userObject);
		userRepository.save(user);
		UserDTO userDTO = new UserDTO(user);
		URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userObject) {
		User user = userRepository.getReferenceById(userObject.getId());
		user.setName(userObject.getName());
		user.setLastName(userObject.getLastName());
		user.setEmail(userObject.getEmail());
		
		if(userObject.getPassword() != null && !userObject.getPassword().isEmpty()) {
			user.setPassword(new BCryptPasswordEncoder().encode(userObject.getPassword()));
		}
		
		UserDTO userDTO = new UserDTO(user);
		
		return ResponseEntity.ok(userDTO);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping
	public ResponseEntity<Page<UserDTO>> listUser(Pageable pageable) {
		Page page =  userRepository.findAll(pageable).map(UserDTO::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
		UserDTO userDTO = new UserDTO(userRepository.getReferenceById(Long.valueOf(id)));
		return ResponseEntity.ok(userDTO);
	}
	
	@PostMapping("/send/forgot/{email}")
	public ResponseEntity<Boolean> sendForgotCode(@PathVariable String email) {
		UserDTO userDTO = new UserDTO(userRepository.findUserByEmail(email));
		if(userDTO != null && userDTO.getId() > 0) {
			System.out.println(userDTO.getId());
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);
		}
	}
	
	@PostMapping("/confirm/forgot/{email}/{code}")
	public ResponseEntity<Boolean> confirmForgotCode(@PathVariable String email, @PathVariable String code) {
		System.out.println(email);
		System.out.println(code);
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<Boolean> getUserByEmail(@PathVariable String email) {
		UserDTO userDTO = new UserDTO(userRepository.findUserByEmail(email));
		if(userDTO != null && userDTO.getId() > 0) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);
		}
	}
	
	@GetMapping("/logged")
	public ResponseEntity<UserDTO> getLoggedUser(HttpServletRequest request) {
		UserDetails user = this.getUserByToken(request);
		UserDTO userDTO = new UserDTO(userRepository.findUserByEmail(user.getUsername()));
		return ResponseEntity.ok(userDTO);
	}
	
	public UserDTO getLoggedUserDTO(HttpServletRequest request) {
		UserDetails user = this.getUserByToken(request);
		UserDTO userDTO = new UserDTO(userRepository.findUserByEmail(user.getUsername()));
		return userDTO;
	}
	
//	@SuppressWarnings("rawtypes")
//	@DeleteMapping("/{id}")
//	@Transactional
//	public ResponseEntity deleteUser(@PathVariable String id) {
//		userRepository.deleteById(Long.valueOf(id));
//		return ResponseEntity.noContent().build();
//	}
}
