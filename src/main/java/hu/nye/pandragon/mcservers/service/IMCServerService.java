package hu.nye.pandragon.mcservers.service;

import hu.nye.pandragon.mcservers.model.dto.MCServerDTO;
import hu.nye.pandragon.mcservers.request.MCServerRequest;
import hu.nye.pandragon.mcservers.request.MCFiltersRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IMCServerService {

	/**
	 * Szerver hozzáadása az adatbázishoz
	 * true-t ad vissza, ha sikerült, és létezik az adatbázisban,
	 * false-t, ha az INSERT után nem található
	 *
	 * @param userRequest a kérés adatai
	 * @return sikerült-e hozzáadni az adatbázishoz
	 */
	ResponseEntity saveMCServer(MCServerRequest userRequest);

	MCServerDTO getMCServerById(Long id);

	void updateMCServer(String address, MCServerRequest userRequest);

	void deleteMCServerById(Long id);

	void deleteMCServerByAddress(String address);

	List<String> getMCVersions();

	List<Object[]> getMCServerCountsByVersions();

	List<MCServerDTO> getAllMCServers();

	List<MCServerDTO> getMCServersByFilters(MCFiltersRequest request);
}
