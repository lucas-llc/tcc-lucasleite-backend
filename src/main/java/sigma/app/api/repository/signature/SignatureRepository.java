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
}
