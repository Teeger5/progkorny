package hu.nye.spring.core.service;

import hu.nye.spring.core.model.dto.MCServerDTO;
import hu.nye.spring.core.request.MCFiltersRequest;
import hu.nye.spring.core.request.MCServerRequest;
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
