package hu.nye.spring.core.service;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.entity.MCVersionEntity;
import hu.nye.spring.core.request.MCServerRequest;

import java.util.List;

public interface IMCServerService {

	MCServerEntity saveMCServer(MCServerRequest userRequest);

	MCServerEntity getMCServerById(Long id);

	MCServerEntity updateMCServer(Long id, MCServerRequest userRequest);

	void deleteMCServerById(Long id);

	List<String> getMCVersions();

	public List<MCServerEntity> getAllMCServers();
}
