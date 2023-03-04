package sigma.app.api.controller.signature;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
	public  ResponseEntity<SignatureDTO> CreateSignature(@RequestBody @Valid SignatureDTO signatureObject, UriComponentsBuilder uriBuilder) {
		Signature signature = new Signature(signatureObject);
		repository.save(signature);
		SignatureDTO signatureDTO = new SignatureDTO(signature);
		URI uri = uriBuilder.path("/signature/{id}").buildAndExpand(signature.getId()).toUri();
		return ResponseEntity.created(uri).body(signatureDTO);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<SignatureDTO> UpdateSignature(@RequestBody @Valid SignatureDTO signatureObject) {
		Signature signature = repository.getReferenceById(signatureObject.id());
		signature.setName(signatureObject.name());
		signature.setDescription(signatureObject.description());
		signature.setPrice(signatureObject.price());
		signature.setStartDate(signature.getStartDate());
		signature.setFrequency(signature.getFrequency());
		signature.setStatus(signature.getStatus());
		signature.setSendPush(signature.isSendPush());
		signature.setCurrency(signature.getCurrency());
		signature.setIconImage(signature.getIconImage());
		
		SignatureDTO signatureDTO = new SignatureDTO(signature);
		
		return ResponseEntity.ok(signatureDTO);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping
	public ResponseEntity<Page<SignatureDTO>> ListSignature(Pageable pageable) {
		Page page =  repository.findAll(pageable).map(SignatureDTO::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	public  ResponseEntity<SignatureDTO> GetSignature(@PathVariable String id) {
		SignatureDTO signatureDTO = new SignatureDTO(repository.getReferenceById(Long.valueOf(id)));
		return ResponseEntity.ok(signatureDTO);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity DeleteSignature(@PathVariable String id) {
		repository.deleteById(Long.valueOf(id));
		return ResponseEntity.noContent().build();
	}
}
