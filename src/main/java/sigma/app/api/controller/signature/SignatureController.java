package sigma.app.api.controller.signature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@PutMapping
	@Transactional
	public void UpdateSignature(@RequestBody @Valid SignatureDTO signatureObject) {
		Signature signature = repository.getReferenceById(signatureObject.id());
		signature.setName(signatureObject.name());
		signature.setDescription(signatureObject.description());
		signature.setPrice(signatureObject.price());
	}
	
	@GetMapping
	public Page<SignatureDTO> ListSignature(Pageable pageable) {
		return repository.findAll(pageable).map(SignatureDTO::new);
	}
	
	@GetMapping("/{id}")
	public SignatureDTO GetSignature(@PathVariable String id) {
		return new SignatureDTO(repository.getReferenceById(Long.valueOf(id)));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public void DeleteSignature(@PathVariable String id) {
		repository.deleteById(Long.valueOf(id));
	}
}
