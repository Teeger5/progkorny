package hu.nye.spring.core.service;

import hu.nye.spring.core.entity.MCVersionEntity;
import hu.nye.spring.core.model.MCServer;
import hu.nye.spring.core.repistory.IMCVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.repistory.IMCServerRepository;
import hu.nye.spring.core.request.MCServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MCServerService implements IMCServerService {

	@Autowired
	private IMCServerRepository mcServerRepository;

	@Autowired
	private IMCVersionRepository mcVersionRepository;

	@Override
	public MCServerEntity saveMCServer(MCServerRequest request) {
		var version = mcVersionRepository.findByName(request.getVersion())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatusCode.valueOf(404),
						"Version '" + request.getVersion() + "' not present in the database"));
		var mcServerEntity = MCServerEntity.fromRequest(request, version);
		return mcServerRepository.save(mcServerEntity);
	}

	@Override
	public MCServerEntity getMCServerById(Long id) {
		return mcServerRepository.findById(id).orElseThrow();
	}

	@Override
	public MCServerEntity updateMCServer(Long id, MCServerRequest mcServerRequest) {
		var mcServerEntity = mcServerRepository.findById(id).orElseThrow();
		mcServerEntity.updateWith(mcServerRequest);
		return mcServerRepository.save(mcServerEntity);
	}

	@Override
	public void deleteMCServerById(Long id) {
		mcServerRepository.deleteById(id);
	}

	@Override
	public List<String> getMCVersions() {
		return ((List<MCVersionEntity>) mcVersionRepository.findAll())
				.stream()
				.map(x -> x.getName())
				.collect(Collectors.toList());
	}

	public List<MCServerEntity> getAllMCServers() {
		return ((List<MCServerEntity>) mcServerRepository.findAll());
	}

    /*public void printLoggedUser(){
        System.out.println(userRepository.getLoggedUser());
    }

    public User getUser() {
        return userRepository.getLoggedUser();
    }*/
}
