package sigma.app.api.repository.signature;

import org.springframework.data.jpa.repository.JpaRepository;

import sigma.app.api.model.signature.Signature;

public interface SignatureRepository extends JpaRepository<Signature, Long>{

}
