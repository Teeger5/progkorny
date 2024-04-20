package hu.nye.spring.core.controller;

import hu.nye.spring.core.entity.MCVersionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.request.MCServerRequest;
import hu.nye.spring.core.service.IMCServerService;

import java.util.List;

@RestController
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
	public MCServerEntity saveMCServer(@RequestBody MCServerRequest request) {
		return mcServerService.saveMCServer(request);
	}

	@GetMapping("/servers")
	public List<MCServerEntity> getAllMCServers() {
		return mcServerService.getAllMCServers();
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
