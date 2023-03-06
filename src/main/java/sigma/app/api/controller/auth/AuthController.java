package sigma.app.api.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sigma.app.api.model.user.User;
import sigma.app.api.object.auth.TokenDataJWT;
import sigma.app.api.object.user.AuthData;
import sigma.app.api.service.TokenService;

@RestController
@RequestMapping("/login")
public class AuthController {

	@Autowired
	AuthenticationManager manager;
	
	@Autowired
	TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDataJWT> doLogin(@RequestBody AuthData data) {
		var token = new UsernamePasswordAuthenticationToken(data.email(), data.password());
		var authentication = manager.authenticate(token);
		String tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());
		return ResponseEntity.ok(new TokenDataJWT(tokenJWT));
	}
}
