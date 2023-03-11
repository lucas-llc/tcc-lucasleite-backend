package sigma.app.api.controller.signature;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import sigma.app.api.controller.user.UserController;
import sigma.app.api.model.signature.Signature;
import sigma.app.api.model.user.User;
import sigma.app.api.object.signature.SignatureDTO;
import sigma.app.api.object.signature.SignatureTotalPrices;
import sigma.app.api.object.user.UserDTO;
import sigma.app.api.repository.signature.SignatureRepository;
import sigma.app.api.repository.user.UserRepository;

@RestController
@RequestMapping("/signature")
public class SignatureController {
	
	@Autowired
	private SignatureRepository repository;
	
	@Autowired
	UserController userController;
	
	@Autowired
	UserRepository userRepository;
	
	 private static final DecimalFormat df = new DecimalFormat("0.00");
	
	@PostMapping
	@Transactional
	public  ResponseEntity<SignatureDTO> createSignature(HttpServletRequest request, @RequestBody SignatureDTO signatureObject, UriComponentsBuilder uriBuilder) {
		UserDTO userDTO = userController.GetLoggedUserDTO(request);
		User user = new User(userDTO);
		Signature signature = new Signature(signatureObject, user);
		repository.save(signature);
		SignatureDTO signatureDTO = new SignatureDTO(signature);
		URI uri = uriBuilder.path("/signature/{id}").buildAndExpand(signature.getId()).toUri();
		return ResponseEntity.created(uri).body(signatureDTO);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<SignatureDTO> updateSignature(@RequestBody SignatureDTO signatureObject) {
		Signature signature = repository.getReferenceById(signatureObject.getId());
		signature.setName(signatureObject.getName());
		signature.setDescription(signatureObject.getDescription());
		signature.setPrice(signatureObject.getPrice());
		signature.setStartDate(signature.getStartDate());
		signature.setFrequency(signature.getFrequency());
		signature.setStatus(signature.getStatus());
		signature.setSendPush(signature.isSendPush());
		signature.setCurrency(signature.getCurrency());
		signature.setIconImage(signature.getIconImage());
		
		SignatureDTO signatureDTO = new SignatureDTO(signature);
		
		return ResponseEntity.ok(signatureDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<SignatureDTO>> listSignature(HttpServletRequest request) {
		UserDTO user = userController.GetLoggedUserDTO(request);
		List<SignatureDTO> result =  repository.listSignatureByUser(user.getId()).stream().map(SignatureDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/{id}")
	public  ResponseEntity<SignatureDTO> getSignature(@PathVariable String id) {
		SignatureDTO signatureDTO = new SignatureDTO(repository.getReferenceById(Long.valueOf(id)));
		return ResponseEntity.ok(signatureDTO);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity deleteSignature(@PathVariable String id) {
		repository.deleteById(Long.valueOf(id));
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/total")
	public ResponseEntity<SignatureTotalPrices> getTotalPrices(HttpServletRequest request) {
		UserDTO user = userController.GetLoggedUserDTO(request);
		List<SignatureDTO> result =  repository.listSignatureByUser(user.getId()).stream().map(SignatureDTO::new).collect(Collectors.toList());
		
		Double total = 0.0;
		for (SignatureDTO signatureDTO : result) {
			switch(signatureDTO.getFrequency()) {
				case "MENSAL":
					total += signatureDTO.getPrice()*12;
					break;
				case "BIMESTRAL":
					total += signatureDTO.getPrice()*6;
					break;
				case "TRIMESTRAL":
					total += signatureDTO.getPrice()*4;
					break;
				case "SEMESTRAL":
					total += signatureDTO.getPrice()*2;
					break;
				case "ANUAL":
					total += signatureDTO.getPrice();
					break;
			}
		}
		
		SignatureTotalPrices signatureTotalPrices = new SignatureTotalPrices();
		
		signatureTotalPrices.setMonthlyTotal(df.format(total/12));
		signatureTotalPrices.setYearlyTotal(df.format(total));
		
		return ResponseEntity.ok(signatureTotalPrices);
	}
}
