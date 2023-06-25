package searchengine.model.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.LemmaEntity;
import searchengine.model.SiteEntity;

import javax.persistence.SecondaryTable;
import java.util.List;
import java.util.Set;

@Repository
@ConfigurationProperties(prefix = "config-frecuency")
public interface LemmaRepository  extends JpaRepository<LemmaEntity,Integer> {
    @Query(value = "select * from lemma t where t.lemma = :lemma for update",nativeQuery = true)
    LemmaEntity lemmaExist(@Param("lemma") String lemma);

    Integer countBySite(SiteEntity siteEntity);

    @Query( value = "select t from lemma t where t.frecuency<300 " +
            "and t.lemma in (:lemmas) " +
            "and t.site =:site",nativeQuery = true )
    List<LemmaEntity> selectLemmaBySyte(Set<String> lemmas, SiteEntity site);
}
