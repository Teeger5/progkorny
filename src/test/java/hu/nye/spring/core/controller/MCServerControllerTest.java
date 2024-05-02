package hu.nye.spring.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.nye.spring.core.request.MCServerRequest;
import hu.nye.spring.core.service.IMCServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MCServerControllerTest {


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

}