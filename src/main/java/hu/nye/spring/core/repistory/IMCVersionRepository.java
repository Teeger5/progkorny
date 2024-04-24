package hu.nye.spring.core.repistory;

import hu.nye.spring.core.entity.MCVersionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMCVersionRepository extends CrudRepository<MCVersionEntity, Long> {

/*	@Query("SELECT v FROM MCVersions;")
	List<MCVersionEntity> findAll();*/

	/**
	 * Verzió keresése név alapján
	 * Név pl.: 1.19.2, azaz '.' kell
	 * @param name a keresett név
	 * @return a megtalált verzió, ha megtalálható
	 */
	@Query("SELECT v FROM MCVersionEntity v WHERE v.name = ?1")
	Optional<MCVersionEntity> findByName(String name);

	@Query("SELECT v FROM MCVersionEntity v WHERE v.name IN (?1)")
	List<MCVersionEntity> findAllByName(List<String> names);

}
