package hu.nye.spring.core.repistory;

import java.util.List;

import hu.nye.spring.core.entity.MCVersionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.nye.spring.core.entity.MCServerEntity;

@Repository
public interface IMCServerRepository extends CrudRepository<MCServerEntity, Long> {


    List<MCServerEntity> findAllByVersion(MCVersionEntity version);
}
