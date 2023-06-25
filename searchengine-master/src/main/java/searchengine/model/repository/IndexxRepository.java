package searchengine.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.IndexEntity;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;

import java.util.List;

@Repository
public interface IndexxRepository extends JpaRepository<IndexEntity,Integer> {
    @Query(value = "select * from indexx t where t.page_id = :pageId and t.lemma_id = :lemmaId",nativeQuery = true)
    IndexEntity indexSearchExist(@Param("pageId")Integer pageId, @Param("lemmaId")Integer lemmaId);

    @Query(value = "select t from IndexEntity t where t.lemma IN :lemmas AND t.page IN pages")
    List<IndexEntity> findByPagesAndLemmas(List<LemmaEntity> lemmas,
                                           List<PageEntity> pages);

    IndexEntity findByPageAndLemma(PageEntity pageEntity, LemmaEntity lemma);
}
