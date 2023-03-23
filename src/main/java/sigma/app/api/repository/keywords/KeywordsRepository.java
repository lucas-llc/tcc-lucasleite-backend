package sigma.app.api.repository.keywords;

import org.springframework.data.jpa.repository.JpaRepository;

import sigma.app.api.model.keywords.Keywords;

public interface KeywordsRepository extends JpaRepository<Keywords, Long> {

}
