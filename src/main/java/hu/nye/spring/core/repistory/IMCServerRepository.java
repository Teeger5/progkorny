package hu.nye.spring.core.repistory;

import java.util.List;

import hu.nye.spring.core.entity.MCVersionEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.nye.spring.core.entity.MCServerEntity;

@Repository
public interface IMCServerRepository extends CrudRepository<MCServerEntity, Long>, JpaSpecificationExecutor<MCServerEntity> {

    List<MCServerEntity> findAllByVersion(MCVersionEntity version);

    boolean existsByAddress(String address);

    /**
     * Lekérdezi, hogy az egyes verziókhoz hány szerver tartozik
     * Ez hasznos lehet, amikor a kliens csak bizonyos verziójú
     * szervereket kér, mert így tudhatja,
     * hogy egy adott verzióhoz hány szerver tartozik,
     * azaz nem kell találgatnia, hogy melyik verzióhoz
     * tartozik legalább egy szerver és melyiknél nincs
     * @return verzió neve -> mennyi szerver tartozik hozzá
     */
    @Query("SELECT v.name, COUNT(s) FROM MCServerEntity s JOIN s.version v GROUP BY v.name")
    List<Object[]> countServersByAllVersions();
}
