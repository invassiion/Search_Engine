package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.config.Site;
import searchengine.model.SiteEntity;

@Repository
public interface SiteRepository extends CrudRepository<SiteEntity,Integer> {



}
