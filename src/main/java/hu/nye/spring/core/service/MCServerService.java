package hu.nye.spring.core.service;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.entity.MCVersionEntity;
import hu.nye.spring.core.exceptions.MCServerNotFoundException;
import hu.nye.spring.core.exceptions.UnknownMCServerVersionException;
import hu.nye.spring.core.model.dto.MCServerDTO;
import hu.nye.spring.core.repository.IMCServerRepository;
import hu.nye.spring.core.repository.IMCVersionRepository;
import hu.nye.spring.core.repository.MCServerSpecification;
import hu.nye.spring.core.request.MCFiltersRequest;
import hu.nye.spring.core.request.MCServerRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MCServerService implements IMCServerService {

	@Autowired
	private final IMCServerRepository mcServerRepository;

	@Autowired
	private final IMCVersionRepository mcVersionRepository;

	@Override
	public ResponseEntity saveMCServer(MCServerRequest request) {
		var serverDTO = new MCServerDTO(request);
		var version = mcVersionRepository.findByName(serverDTO.getVersion())
				.orElseThrow(() -> new UnknownMCServerVersionException(serverDTO.getVersion()));
		if (mcServerRepository.existsByAddress(serverDTO.getAddress())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(request.getAddress() + " címen már van regisztrált szerver.");
		}
		mcServerRepository.save(new MCServerEntity(serverDTO, version));
		return ResponseEntity.ok("Szerver hozzáadva");
	}

	@Override
	public MCServerDTO getMCServerById(Long id) {
		var mcServerEntity = mcServerRepository.findById(id)
				.orElseThrow(() -> new MCServerNotFoundException(String.valueOf(id)));
		return new MCServerDTO(mcServerEntity);
	}

	@Override
	public void updateMCServer(String address, MCServerRequest request) {
		var mcServerEntity = mcServerRepository.findByAddress(address)
				.orElseThrow(() -> new MCServerNotFoundException(address));
		mcServerEntity.setName(request.getName());
		mcServerEntity.setDescription(request.getDescription());
		mcServerEntity.setPort(request.getPort());
		mcServerEntity.setMaxPlayers(request.getMaxPlayers());
		mcServerRepository.save(mcServerEntity);
	}

	@Override
	public void deleteMCServerById(Long id) {
		if (!mcServerRepository.existsById(id)) {
			throw new MCServerNotFoundException();
		}
		mcServerRepository.deleteById(id);
	}

	@Override
	public void deleteMCServerByAddress(String address) {
		if (!mcServerRepository.existsByAddress(address)) {
			throw new MCServerNotFoundException(address);
		}
		mcServerRepository.deleteByAddress(address);
	}

	@Override
	public List<String> getMCVersions() {
		return ((List<MCVersionEntity>) mcVersionRepository.findAll())
				.stream()
				.map(x -> x.getName())
				.collect(Collectors.toList());
	}

	@Override
	public List<Object[]> getMCServerCountsByVersions() {
		return mcServerRepository.countServersByAllVersions();
	}

	public List<MCServerDTO> getAllMCServers() {
		return mcServerEntityToDTO((List<MCServerEntity>) mcServerRepository.findAll());
	}

	@Override
	public List<MCServerDTO> getMCServersByFilters(MCFiltersRequest request) {
		if (request.getVersions() != null && !request.getVersions().isEmpty()) {
			var specification = Specification.where(MCServerSpecification
					.hasVersion(mcVersionRepository.findAllByName(request.getVersions())));
			return mcServerEntityToDTO(mcServerRepository.findAll(specification));
		}
		return getAllMCServers();
	}

	private static List<MCServerDTO> mcServerEntityToDTO(List<MCServerEntity> entities) {
		return entities
				.stream()
				.map(x -> new MCServerDTO(x))
				.collect(Collectors.toUnmodifiableList());
	}
}
