package hu.nye.spring.core.service;

import hu.nye.spring.core.entity.MCServerEntity;
import hu.nye.spring.core.entity.MCVersionEntity;
import hu.nye.spring.core.exceptions.MCServerNotFoundException;
import hu.nye.spring.core.repistory.IMCServerRepository;
import hu.nye.spring.core.repistory.IMCVersionRepository;
import hu.nye.spring.core.request.MCServerRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MCServerServiceTest {

	@Mock
	private IMCServerRepository mcServerRepository;

	@Mock
	private IMCVersionRepository mcVersionRepository;

	@InjectMocks
	private MCServerService mcServerService;

	@Test
	public void shouldNotSaveMCServerWhenAddressExists() {
		// Adott
		MCServerRequest request = new MCServerRequest(
				"Doragon teszt", "doragonteszt.xyz",
				"leírás", "1.19.2", 25565, 20
		);
		// Tegyük fel, hogy a cím már létezik
		when(mcVersionRepository.findByName(anyString())).thenReturn(Optional.of(new MCVersionEntity()));
		when(mcServerRepository.existsByAddress(anyString())).thenReturn(true);

		// Amikor
		ResponseEntity response = mcServerService.saveMCServer(request);

		// Akkor
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		verify(mcServerRepository, never()).save(any(MCServerEntity.class));
	}

	@Test
	public void shouldGetMCServerByIdFound() {
		// Adott
		Long id = 1L;
		MCServerEntity mcServerEntity = new MCServerEntity();
		when(mcServerRepository.findById(id)).thenReturn(Optional.of(mcServerEntity));

		// Amikor
		MCServerEntity result = mcServerService.getMCServerById(id);

		// Akkor
		assertNotNull(result);
		assertEquals(mcServerEntity, result);
	}

	@Test
	public void testGetMCServerByIdNotFound() {
		// Adott
		Long id = 1L;
		when(mcServerRepository.findById(id)).thenReturn(Optional.empty());;
		// Amikor és Akkor
		assertThrows(MCServerNotFoundException.class, () -> mcServerService.getMCServerById(id));
	}
}