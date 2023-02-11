package sigma.app.api.controller.signature;

import org.springframework.beans.factory.annotation.Autowired;
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
	
//	@GetMapping(value="/{id}")
//	public SignatureDTO GetSignature(@PathVariable String id) {
//		Signature signature = repository.findById(Long.valueOf(id)).get();
//		return new SignatureDTO(signature.)
//	}
}
