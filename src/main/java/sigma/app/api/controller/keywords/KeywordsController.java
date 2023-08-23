package sigma.app.api.controller.keywords;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import sigma.app.api.controller.signature.SignatureController;
import sigma.app.api.controller.user.UserController;
import sigma.app.api.model.keywords.Keywords;
import sigma.app.api.model.user.User;
import sigma.app.api.object.keywords.KeywordsDTO;
import sigma.app.api.object.user.UserDTO;
import sigma.app.api.repository.keywords.KeywordsRepository;

@RestController
@RequestMapping("/keywords")
public class KeywordsController {

	@Autowired
	UserController userController;
	
	@Autowired
	private KeywordsRepository repository;
	
	@Autowired
	SignatureController signatureController;

	@PostMapping
	@Transactional
	public  ResponseEntity<KeywordsDTO> createKeyword(HttpServletRequest request, @RequestBody KeywordsDTO keywordsObject, UriComponentsBuilder uriBuilder) {
		UserDTO userDTO = userController.getLoggedUserDTO(request);
		User user = new User(userDTO);
		Keywords keywords = new Keywords(keywordsObject, user);
		repository.save(keywords);
		KeywordsDTO keywordsDTO = new KeywordsDTO(keywords);
		URI uri = uriBuilder.path("/signature/{id}").buildAndExpand(keywords.getId()).toUri();
		return ResponseEntity.created(uri).body(keywordsDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<KeywordsDTO>> listKeyword(HttpServletRequest request) {
		UserDTO user = userController.getLoggedUserDTO(request);
		List<KeywordsDTO> result =  repository.listKeywordsByUser(user.getId()).stream().map(KeywordsDTO::new).collect(Collectors.toList());
		List<KeywordsDTO> keywordsList = new ArrayList<>();
		for(KeywordsDTO keyword : result) {
			keyword.setSignatureCount(signatureController.countSignatureByCategory(user.getId(), keyword.getId()));
			if(keyword.getSignatureCount() > 0) {
				keywordsList.add(keyword);
			}
		}
		
		return ResponseEntity.ok(keywordsList);
	}
}
