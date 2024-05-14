package hu.nye.spring.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.nye.spring.core.model.dto.MCServerDTO;
import hu.nye.spring.core.request.MCFiltersRequest;
import hu.nye.spring.core.request.MCServerRequest;
import hu.nye.spring.core.service.IMCServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class MCServerDTOControllerTest {

	private MockMvc mockMvc;

	@Mock
	private IMCServerService mcServerService;

	@InjectMocks
	private MCServerController mcServerController;

	@BeforeEach
	public void setUp() {
		mcServerService = mock(IMCServerService.class);
		mcServerController = new MCServerController(mcServerService);
		mockMvc = MockMvcBuilders.standaloneSetup(mcServerController).build();
	}

	/**
	 * Ebben a tesztben egy tartalmilag helyes kérés van,
	 * amit a szervernek el kell fogadnia és hozzáadni az adatbázishoz
	 * Persze ez nem része ennek a tesztnek
	 * 1. létrehozzuk a kérést
	 * 2.elküldjük a JSON-ként
	 * 3. OK kódot várunk válaszként
	 * 5. Mmgnézzük, hogy csak egyszer adja-e hozzá
	 * @throws Exception
	 */
	@Test
	public void shouldSaveMCServer() throws Exception {
		MCServerRequest request = new MCServerRequest(
				"Doragon teszt", "doragonteszt.xyz",
				"leírás", "1.20.4", 25565, 20
		);

		mockMvc.perform(post("/servers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(request)))
				.andExpect(status().isOk());

		verify(mcServerService, times(1)).saveMCServer(any(MCServerRequest.class));
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	// Teszt eset, amikor nincsenek paraméterek megadva
	@Test
	public void whenNoParameters_thenGetAllServers() throws Exception {
		List<MCServerDTO> allServers = Arrays.asList(/* szerver entitások */);
		given(mcServerService.getMCServersByFilters(new MCFiltersRequest(null, null, null, null))).willReturn(allServers);

		mockMvc.perform(get("/servers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(allServers.size())));
	}

	// Teszt eset, amikor a 'v' paraméter null
//	@Test
	public void whenVParameterIsNull_thenGetAllServers() throws Exception {
		var request = new MCServerRequest("Geemu o shimasu ka",
				"geemuoshimasuka.xyz",
				"1.20.4",
				"",
				25565,
				20);
		var filteredServers = new MCServerDTO[] {
				new MCServerDTO(request), new MCServerDTO(request)
		};
		var filterRequest = new MCFiltersRequest(null, null, null, null);
		List<MCServerDTO> filteredServersList = Arrays.asList(filteredServers);

		when(mcServerService.getMCServersByFilters(filterRequest)).thenReturn(filteredServersList);

		mockMvc.perform(get("/servers")
						.param("v", "1.20.4")
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(filteredServers.length)));
	}

	// Teszt eset, amikor a 'v' paraméter üres
	@Test
	public void whenVParameterIsEmpty_thenGetAllServers() throws Exception {
		List<MCServerDTO> filteredServers = Arrays.asList(/* szűrt szerver entitások */);
		given(mcServerService.getMCServersByFilters(new MCFiltersRequest(new ArrayList<>(), null, null, null))).willReturn(filteredServers);

		mockMvc.perform(get("/servers").param("v", ""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(filteredServers.size())));
	}
}