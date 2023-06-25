package searchengine.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;

import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public interface PageRepository extends JpaRepository<PageEntity,Integer> {
    @Query(value = "select * from page t where t.site_id = :siteId and t.path = :path limit 1",nativeQuery = true)
    PageEntity findPageBySiteIdAndPath(@Param("path") String path, @Param("siteId") Integer siteId);

//    @Query(value = "select p from PageEntity p " +
//            "join IndexEntity i on p.id = i.page.id " +
//            "where i.lemma =:lemma and p.site =:site")
//    List<PageEntity> ByLemma(@Param("site") SiteEntity siteEntity, @Param("lemma") String lemma);
@Query("SELECT p FROM PageEntity p " +
        "JOIN IndexEntity i ON p.id = i.page.id " +
        "WHERE i.lemma = :lemma AND p.site = :site " +
        "ORDER BY p.id ASC")
CopyOnWriteArrayList<PageEntity> ByLemma(String lemma, SiteEntity site);
    @Query(value = "select COUNT(*) from PageEntity AS e group by e.site having e.site =: site")
    Integer CountBySite(@Param("site")SiteEntity siteEntity);
}
