package hu.nye.spring.core.controller;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.request.MCFiltersRequest;
import hu.nye.spring.core.request.MCServerRequest;
import hu.nye.spring.core.service.IMCServerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class MCServerController {

	@Autowired
	private IMCServerService mcServerService;

	/**
	 * Új szerver hozzáadása
	 * POST /servers
	 * @param request
	 * @return
	 */
	@PostMapping("/servers")
	public ResponseEntity saveMCServer(@Valid  @RequestBody MCServerRequest request) {
		return mcServerService.saveMCServer(request);
	}

	/**
	 * Visszaküldi a Minecraft szerverek listáját
	 * Ha vannak megadva szűrési feltételek,
	 * akkor azoknak megfelelőeket küld vissza
	 * @return
	 */
	@GetMapping("/servers")
	public List<MCServerEntity> getAllMCServers(
			@RequestParam(value = "v", required = false) List<String> versions,
			@RequestParam(value = "maxPlayersMax", required = false) Integer maxPlayersMax,
			@RequestParam(value = "maxPlayersMin", required = false) Integer maxPlayersMin,
			@RequestParam(value = "partOfName", required = false) String partOfName) {
		var filter = new MCFiltersRequest(versions, maxPlayersMin, maxPlayersMax, partOfName);
		log.info("GET /servers filter: " + filter);
		return mcServerService.getMCServersByFilters(filter);
	}

	/**
	 * Szerver adatainak kérése ID alapján
	 * GET /servers/{id}
	 */
	@GetMapping("/servers/{id}")
	public MCServerEntity getMCServerById(@PathVariable("id") Long id) {
		return mcServerService.getMCServerById(id);
	}

	@DeleteMapping("/servers/{address}")
	public ResponseEntity<Object> eleteMCServerByAddress(@PathVariable("address") String address) {
		if (!mcServerService.existsByAddress(address)) {
			mcServerService.deleteMCServerByAddress(address);
			if (mcServerService.existsByAddress(address)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PatchMapping("/servers/{address}")
	public void updateMCServer(@PathVariable("address") String address, @RequestBody MCServerRequest request) {
		mcServerService.updateMCServer(address, request);
	}

	@GetMapping("/versions")
	public List<Object[]> getVersions() {
		return mcServerService.getMCServerCountsByVersions();
	}
}
