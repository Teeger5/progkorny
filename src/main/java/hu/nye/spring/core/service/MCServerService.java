package hu.nye.spring.core.service;

import hu.nye.spring.core.entity.MCVersionEntity;
import hu.nye.spring.core.repistory.IMCVersionRepository;
import hu.nye.spring.core.repistory.MCServerSpecification;
import hu.nye.spring.core.request.MCFiltersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity saveMCServer(MCServerRequest request) {
		var version = mcVersionRepository.findByName(request.getVersion())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.UNPROCESSABLE_ENTITY,
						"'" + request.getVersion() + "' verzió nem ismert"));
		var mcServerEntity = MCServerEntity.fromRequest(request, version);
		if (mcServerRepository.existsByAddress(request.getAddress())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(request.getAddress() + " címen már van regisztrált szerver.");
		}
		mcServerRepository.save(mcServerEntity);
		return ResponseEntity.ok("Szerver hozzáadva \uD83D\uDE0E");
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
	public void deleteMCServerByAddress(String address) {

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

	@Override
	public List<MCServerEntity> getMCServersByFilters (MCFiltersRequest request) {
		if (request.getVersions() != null && !request.getVersions().isEmpty()) {
			var specification = Specification.where(MCServerSpecification
					.hasVersion(mcVersionRepository.findAllByName(request.getVersions())));
			return mcServerRepository.findAll(specification);
		}
		return getAllMCServers();
	}

	@Override
	public boolean existsByAddress(String address) {
		return mcServerRepository.existsByAddress(address);
	}

	/*public void printLoggedUser(){
        System.out.println(userRepository.getLoggedUser());
    }

    public User getUser() {
        return userRepository.getLoggedUser();
    }*/
}
