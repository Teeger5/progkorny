package hu.nye.spring.core.controller;

import hu.nye.spring.core.entity.MCVersionEntity;
import hu.nye.spring.core.request.MCFiltersRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.request.MCServerRequest;
import hu.nye.spring.core.service.IMCServerService;

import java.util.List;
import java.util.Optional;

@RestController
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
	public ResponseEntity saveMCServer(@RequestBody MCServerRequest request) {
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

	@DeleteMapping("/servers/{id}")
	public void deleteMCServerById(@PathVariable("id") Long id) {
		mcServerService.deleteMCServerById(id);
	}

	@PatchMapping("/servers/{id}")
	public void updateMCServer(@PathVariable("id") Long id, @RequestBody MCServerRequest request) {
		mcServerService.updateMCServer(id, request);
	}

	@GetMapping("/versions")
	public List<String> getVersions() {
		return mcServerService.getMCVersions();
	}
}
