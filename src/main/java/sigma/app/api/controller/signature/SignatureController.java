package sigma.app.api.controller.signature;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import sigma.app.api.model.signature.Signature;
import sigma.app.api.object.signature.SignatureDTO;
import sigma.app.api.repository.signature.SignatureRepository;

@RestController
@RequestMapping("/signature")
public class SignatureController {
	
	@Autowired
	private SignatureRepository repository;

	@PostMapping
	@Transactional
	public void CreateSignature(@RequestBody @Valid SignatureDTO signatureObject) {
		Signature signature = new Signature(signatureObject);
		repository.save(signature);
	}
	
	@GetMapping
	public Page<SignatureDTO> GetSignature(Pageable pageable) {
		return repository.findAll(pageable).map(SignatureDTO::new);
	}
	
//	@GetMapping
//	public List<SignatureDTO> GetSignature(@PathVariable String id) {
//		return repository.findAll().stream().map(SignatureDTO::new).toList();
//	}
}
