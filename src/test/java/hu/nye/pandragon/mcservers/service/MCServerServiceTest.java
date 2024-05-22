package hu.nye.pandragon.mcservers.service;

import hu.nye.pandragon.mcservers.exceptions.MCServerNotFoundException;
import hu.nye.pandragon.mcservers.model.dto.MCServerDTO;
import hu.nye.pandragon.mcservers.entity.MCServerEntity;
import hu.nye.pandragon.mcservers.entity.MCVersionEntity;
import hu.nye.pandragon.mcservers.exceptions.UnknownMCServerVersionException;
import hu.nye.pandragon.mcservers.repository.IMCServerRepository;
import hu.nye.pandragon.mcservers.repository.IMCVersionRepository;
import hu.nye.pandragon.mcservers.request.MCServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Slf4j
class MCServerServiceTest {

	@Mock
	private IMCServerRepository mcServerRepository;

	@Mock
	private IMCVersionRepository mcVersionRepository;

	@InjectMocks
	private MCServerService mcServerService;

	private MCServerRequest request;

	@BeforeEach
	public void setup() {
		request = new MCServerRequest(
				"Doragon teszt", "doragonteszt.xyz",
				"leírás", "1.19.2", 25565, 20
		);
	}

	@Test
	public void shouldNotSaveMCServerWhenAddressExists() {
		MCServerRequest request = new MCServerRequest(
				"Doragon teszt", "doragonteszt.xyz",
				"leírás", "1.19.2", 25565, 20
		);

		when(mcVersionRepository.findByName(anyString())).thenReturn(Optional.of(new MCVersionEntity()));
		when(mcServerRepository.existsByAddress(anyString())).thenReturn(true);

		ResponseEntity response = mcServerService.saveMCServer(request);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		verify(mcServerRepository, never()).save(any(MCServerEntity.class));
	}

	@Test
	public void shouldNotSaveMCServerWhenVersionNotExists() {
		when(mcVersionRepository.findByName(anyString())).thenReturn(Optional.empty());

		assertThrows(UnknownMCServerVersionException.class, () -> mcServerService.saveMCServer(request));
		verify(mcServerRepository, never()).save(any(MCServerEntity.class));
	}

	@Test
	public void shouldSaveMCServer() {
		var entity = new MCVersionEntity();
		when(mcVersionRepository.findByName(anyString())).thenReturn(Optional.of(entity));

		ResponseEntity response = mcServerService.saveMCServer(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(mcServerRepository, times(1)).save(any(MCServerEntity.class));
	}

	@Test
	public void shouldGetMCServerByIdFound() {
		Long id = 2l;
		var version = new MCVersionEntity("1.19.2");
		MCServerEntity mcServerEntity = new MCServerEntity();
		mcServerEntity.setId(2l);
		mcServerEntity.setVersion(version);
		when(mcServerRepository.findById(id)).thenReturn(Optional.of(mcServerEntity));

		var expected = new MCServerDTO(mcServerEntity);
		MCServerDTO result = mcServerService.getMCServerById(id);

		assertNotNull(result);
		assertEquals(expected, result);
	}

	@Test
	public void shouldNotGetMCServerByIdNotFound() {
		Long id = 1L;

		when(mcServerRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(MCServerNotFoundException.class, () -> mcServerService.getMCServerById(id));
	}

	@Test
	public void shouldGetAllMCVersions() {
		var name1 = "Doragon";
		var name2 = "Arimasu";
		var versionEntities = Arrays.asList(
				new MCVersionEntity(name1),
				new  MCVersionEntity(name2)
		);

		when(mcVersionRepository.findAll()).thenReturn(versionEntities);

		var result = mcServerService.getMCVersions();
		var expected = Arrays.asList(name1, name2);

		log.info("versions: " + expected);
		log.info("result: " + result);

		assertEquals(expected, result);
	}

	@Test
	public void shouldGetAllMCServers() {
		var version = new MCVersionEntity("1.19.2");
		var serverDTO = new MCServerDTO(request);
		var serverEntities = Arrays.asList(
				new MCServerEntity(serverDTO, version),
				new MCServerEntity(serverDTO, version)
		);
		var serverDTOS = Arrays.asList(
				new MCServerDTO(request),
				new MCServerDTO(request)
		);

		when(mcServerRepository.findAll()).thenReturn(serverEntities);

		var result = mcServerService.getAllMCServers();

		log.info("versions: " + serverDTOS);
		log.info("result:   " + result);

		assertEquals(serverDTOS, result);
	}

	@Test
	public void shouldDeleteMCServerByID() {
		when(mcServerRepository.existsById(anyLong())).thenReturn(true);

		long id = 2;
		mcServerService.deleteMCServerById(id);

		verify(mcServerRepository, times(1)).deleteById(anyLong());
	}

	@Test
	public void shouldNotDeleteMCServerByIDWhenNotExists() {
		when(mcServerRepository.existsById(anyLong())).thenReturn(false);

		long id = 2;

		assertThrows(MCServerNotFoundException.class, () -> mcServerService.deleteMCServerById(id));
		verify(mcServerRepository, never()).deleteById(anyLong());
	}

	@Test
	public void shouldDeleteMCServerByAddress() {
		when(mcServerRepository.existsByAddress(anyString())).thenReturn(true);

		var address = "";
		mcServerService.deleteMCServerByAddress(address);

		verify(mcServerRepository, times(1)).deleteByAddress(anyString());
	}

	@Test
	public void shouldNotDeleteMCServerByAddressWhenNotExists() {
		when(mcServerRepository.existsByAddress(anyString())).thenReturn(false);

		var address = "";

		assertThrows(MCServerNotFoundException.class, () -> mcServerService.deleteMCServerByAddress(anyString()));
		verify(mcServerRepository, never()).deleteByAddress(anyString());
	}

	@Test
	public void shouldUpdateMCServerByAddress() {
		var address = "";
		var mcServerEntity = new MCServerEntity(new MCServerDTO(request), new MCVersionEntity());

		when(mcServerRepository.findByAddress(anyString())).thenReturn(Optional.of(mcServerEntity));

		mcServerService.updateMCServer(address, request);

		verify(mcServerRepository, times(1)).save(any(MCServerEntity.class));
	}

	@Test
	public void shouldNotUpdateMCServerWhenNotExists() {
		var address = "";

		when(mcServerRepository.findByAddress(anyString())).thenReturn(Optional.empty());

		assertThrows(MCServerNotFoundException.class, () -> mcServerService.updateMCServer(address, request));
	}

	@Test
	public void shouldGetMCVersionsAndCounts() {
		var name1 = "1.19.2";
		var name2 = "1.20.4";
		var count1 = 2;
		var count2 = 3;
		var expected = Arrays.asList(
				new Object[] { name1, count1 },
				new Object[] { name2, count2 }
		);

		when(mcServerService.getMCServerCountsByVersions()).thenReturn(expected);

		var result = mcServerService.getMCServerCountsByVersions();

		assertEquals(expected, result);
	}
}
