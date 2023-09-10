package sigma.app.api.repository.signature;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sigma.app.api.model.signature.Signature;

public interface SignatureRepository extends JpaRepository<Signature, Long>{

	@Query(
			  value = "SELECT * FROM SIGNATURE S WHERE S.USER_ID = ?1 AND STATUS = ?2", 
			  nativeQuery = true)
	public List<Signature> listSignatureByUser(long userId, String status);
	
	@Query(
			  value = "SELECT * FROM SIGNATURE S, SIGNATURE_KEYWORDS SK WHERE S.USER_ID = ?1 AND S.ID = SK.SIGNATURE_ID AND SK.KEYWORDS_ID = ?2 AND S.STATUS = 'ATIVO' ", 
			  nativeQuery = true)
	public List<Signature> listSignatureByCategory(long userId, long categoryId);
	
	@Query(
			  value = "SELECT COUNT(*) FROM SIGNATURE S, SIGNATURE_KEYWORDS SK WHERE S.USER_ID = ?1 AND S.ID = SK.SIGNATURE_ID AND SK.KEYWORDS_ID = ?2 AND S.STATUS = 'ATIVO'", 
			  nativeQuery = true)
	public long countSignatureByCategory(long userId, long categoryId);
	
	@Query(
			  value = "SELECT * FROM SIGNATURE S WHERE S.USER_ID = ?1 ORDER BY START_DATE ASC LIMIT 1 ", 
			  nativeQuery = true)
	public Signature getFirstPaymentDate(long userId);
}
