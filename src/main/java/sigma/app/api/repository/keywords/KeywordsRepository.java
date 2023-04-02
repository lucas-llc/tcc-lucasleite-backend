package sigma.app.api.repository.keywords;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sigma.app.api.model.keywords.Keywords;

public interface KeywordsRepository extends JpaRepository<Keywords, Long> {

	@Query(
			  value = "SELECT * FROM KEYWORDS K WHERE K.USER_ID = ?1 ", 
			  nativeQuery = true)
	public List<Keywords> listKeywordsByUser(long userId);
}
