package sigma.app.api.controller.signature;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import sigma.app.api.model.keywords.Keywords;
import sigma.app.api.model.signature.Signature;
import sigma.app.api.model.signature.SignatureDateComparator;
import sigma.app.api.model.signature.SignatureStatus;
import sigma.app.api.model.user.User;
import sigma.app.api.object.keywords.KeywordsDTO;
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
		UserDTO userDTO = userController.getLoggedUserDTO(request);
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
		
		List<Keywords> keywordsList = new ArrayList<Keywords>();
		if(signatureObject.getKeywords() != null) {
			for (KeywordsDTO keywords : signatureObject.getKeywords()) {
				Keywords key = new Keywords();
				key.setId(keywords.getId());
				keywordsList.add(key);
			}
		}
		signature.setKeywords(keywordsList);
		
		SignatureDTO signatureDTO = new SignatureDTO(signature);
		
		return ResponseEntity.ok(signatureDTO);
	}
	
	@PutMapping("/changeStatus/{id}")
	@Transactional
	public ResponseEntity<SignatureDTO> changeStatus(@PathVariable String id) {
		Signature signature = repository.getReferenceById(Long.valueOf(id));
		if(signature.getStatus().equals(SignatureStatus.ATIVO)) {
			signature.setStatus(SignatureStatus.INATIVO);
		} else {
			signature.setStatus(SignatureStatus.ATIVO);
		}
		
		SignatureDTO signatureDTO = new SignatureDTO(signature);
		
		return ResponseEntity.ok(signatureDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<SignatureDTO>> listSignature(HttpServletRequest request) {
		UserDTO user = userController.getLoggedUserDTO(request);
		String status = request.getParameter("status");
		List<SignatureDTO> result =  repository.listSignatureByUser(user.getId(), (status != null && !status.isEmpty()) ? status : "ATIVO").stream().map(SignatureDTO::new).collect(Collectors.toList());
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
	
	@GetMapping("/spent/{id}")
	public ResponseEntity<SignatureTotalPrices> getTotalPrices(HttpServletRequest request, @PathVariable String id) {
		SignatureDTO signatureDTO = new SignatureDTO(repository.getReferenceById(Long.valueOf(id)));
		
		Calendar todayCalendar = Calendar.getInstance();
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(signatureDTO.getStartDate());
		int meses = 0;
		meses = (todayCalendar.get(Calendar.YEAR) * 12 + todayCalendar.get(Calendar.MONTH))
		        - (startCalendar.get(Calendar.YEAR) * 12 + startCalendar.get(Calendar.MONTH));
		
		SignatureTotalPrices signatureTotalPrices = new SignatureTotalPrices();
		
		switch (signatureDTO.getFrequency()) {
		case "MENSAL":
			signatureTotalPrices.setTotalSpent(df.format((meses + 1) * signatureDTO.getPrice()));
			break;
		case "BIMESTRAL":
			signatureTotalPrices.setTotalSpent(df.format(((meses/2) + 1) * signatureDTO.getPrice()));
			break;
		case "TRIMESTRAL":
			signatureTotalPrices.setTotalSpent(df.format(((meses/4) + 1) * signatureDTO.getPrice()));
			break;
		case "SEMESTRAL":
			signatureTotalPrices.setTotalSpent(df.format(((meses/6) + 1) * signatureDTO.getPrice()));
			break;
		case "ANUAL":
			signatureTotalPrices.setTotalSpent(df.format(((meses/12) + 1) * signatureDTO.getPrice()));
			break;
		}
		
		return ResponseEntity.ok(signatureTotalPrices);
	}
	
	@GetMapping("/total")
	public ResponseEntity<SignatureTotalPrices> getTotalPrices(HttpServletRequest request) {
		UserDTO user = userController.getLoggedUserDTO(request);
		List<SignatureDTO> result =  repository.listSignatureByUser(user.getId(), "ATIVO").stream().map(SignatureDTO::new).collect(Collectors.toList());
		
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
	
	@GetMapping("/calendar")
	public ResponseEntity<List<SignatureDTO>> getCalendar(HttpServletRequest request) {
		UserDTO user = userController.getLoggedUserDTO(request);
		int month = Integer.valueOf(request.getParameter("month"));
		List<SignatureDTO> signatureList =  repository.listSignatureByUser(user.getId(), "ATIVO").stream().map(SignatureDTO::new).collect(Collectors.toList());
		List<SignatureDTO> result = new ArrayList<SignatureDTO>();
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.set(Calendar.MONTH, month);
		Calendar startCalendar = Calendar.getInstance();
		int meses = 0;
		for (SignatureDTO signatureDTO : signatureList) {
			meses = 0;
			switch(signatureDTO.getFrequency()) {
				case "MENSAL":
					startCalendar.setTime(signatureDTO.getStartDate());
					startCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
					startCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
					signatureDTO.setNextPaymentDate(startCalendar.getTime());
					result.add(signatureDTO);
					break;
				case "BIMESTRAL":
					startCalendar.setTime(signatureDTO.getStartDate());
					meses = (todayCalendar.get(Calendar.YEAR) * 12 + todayCalendar.get(Calendar.MONTH))
			        - (startCalendar.get(Calendar.YEAR) * 12 + startCalendar.get(Calendar.MONTH));
					if (meses%2 == 0) {
						startCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
						startCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
						signatureDTO.setNextPaymentDate(startCalendar.getTime());
						result.add(signatureDTO);
					}
					break;
				case "TRIMESTRAL":
					startCalendar.setTime(signatureDTO.getStartDate());
					meses = (todayCalendar.get(Calendar.YEAR) * 12 + todayCalendar.get(Calendar.MONTH))
			        - (startCalendar.get(Calendar.YEAR) * 12 + startCalendar.get(Calendar.MONTH));
					if (meses%3 == 0) {
						startCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
						startCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
						signatureDTO.setNextPaymentDate(startCalendar.getTime());
						result.add(signatureDTO);
					}
					break;
				case "SEMESTRAL":
					startCalendar.setTime(signatureDTO.getStartDate());
					meses = (todayCalendar.get(Calendar.YEAR) * 12 + todayCalendar.get(Calendar.MONTH))
			        - (startCalendar.get(Calendar.YEAR) * 12 + startCalendar.get(Calendar.MONTH));
					if (meses%6 == 0) {
						startCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
						startCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
						signatureDTO.setNextPaymentDate(startCalendar.getTime());
						result.add(signatureDTO);
					}
					break;
				case "ANUAL":
					startCalendar.setTime(signatureDTO.getStartDate());
					meses = (todayCalendar.get(Calendar.YEAR) * 12 + todayCalendar.get(Calendar.MONTH))
			        - (startCalendar.get(Calendar.YEAR) * 12 + startCalendar.get(Calendar.MONTH));
					if (meses%12 == 0) {
						startCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
						startCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
						signatureDTO.setNextPaymentDate(startCalendar.getTime());
						result.add(signatureDTO);
					}
					break;
			}
		}
		
		Collections.sort(result, new SignatureDateComparator());
		
		return ResponseEntity.ok(result);
	}
}
