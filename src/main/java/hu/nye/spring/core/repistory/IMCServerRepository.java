package hu.nye.spring.core.repistory;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.entity.MCVersionEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMCServerRepository extends CrudRepository<MCServerEntity, Long>, JpaSpecificationExecutor<MCServerEntity> {

    /**
     * Megkeresi azokat a szervereket, amelyek a megadott verziójú játékos futtatják
     * @param version a keresett verzió
     * @return az ilyen verziójú szerverek
     */
    List<MCServerEntity> findAllByVersion(MCVersionEntity version);

    /**
     * Létezik-e szerver a megadott címen?
     * @param address a megadott cím
     * @return létezik-e ilyen szerver
     */
    boolean existsByAddress(String address);

    /**
     * Lekérdezi, hogy az egyes verziókhoz hány szerver tartozik
     * Ez hasznos lehet, amikor a kliens csak bizonyos verziójú
     * szervereket kér, mert így tudhatja,
     * hogy egy adott verzióhoz mennyi szerver tartozik,
     * azaz nem kell találgatnia, hogy melyik verzióhoz
     * tartozik legalább egy szerver és melyiknél nincs
     * RIGHT nélkül csak azokat a verziókat adja vissza,
     * amelyeknél a szám nem 0
     * De azokat is küldeni kell, hogy benne legyen minden elérhető verzió,
     * hogy ne kelljen külön lekérdezni őket
     * @return verzió neve -> mennyi szerver tartozik hozzá
     */
    @Query("SELECT v.name, COUNT(s) FROM MCServerEntity s RIGHT JOIN s.version v GROUP BY v.name")
    List<Object[]> countServersByAllVersions();
}
