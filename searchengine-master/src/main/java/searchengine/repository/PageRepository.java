package searchengine.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.PageEntity;

@Repository

public interface PageRepository extends JpaRepository<PageEntity,Integer> {

public interface PageRepository extends CrudRepository<PageEntity,Integer> {

}
