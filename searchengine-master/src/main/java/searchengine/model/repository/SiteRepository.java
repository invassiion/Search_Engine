package searchengine.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.SiteEntity;

import java.net.URL;

@Repository
public interface SiteRepository extends JpaRepository<SiteEntity,Integer> {
    @Query(value = "select * from site s where s.url = :host limit 1", nativeQuery = true)
    SiteEntity getSiteEntityByUrl(@Param("host") String host);

    SiteEntity findSiteEntityByUrl(URL url);

    @Query(value = "select s.last_error from site s where s.url=: url", nativeQuery = true)
    String findErrorByUrl(URL url);
}
