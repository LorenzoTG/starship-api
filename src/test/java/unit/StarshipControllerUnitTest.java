package unit;

import com.w2m.starshipapi.StarshipApiApplication;
import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.service.StarshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = StarshipApiApplication.class)
@AutoConfigureMockMvc
public class StarshipControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StarshipService starshipService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Existing tests...

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddStarship_NameEmpty() throws Exception {
        mockMvc.perform(post("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"pilot\":\"Luke Skywalker\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name cannot be empty"));

        verify(starshipService, times(0)).addStarship(any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddStarship_PilotEmpty() throws Exception {
        mockMvc.perform(post("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X-Wing\",\"pilot\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pilot").value("Pilot cannot be empty"));

        verify(starshipService, times(0)).addStarship(any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddStarship_NameNull() throws Exception {
        mockMvc.perform(post("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":null,\"pilot\":\"Luke Skywalker\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name cannot be empty"));

        verify(starshipService, times(0)).addStarship(any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddStarship_PilotNull() throws Exception {
        mockMvc.perform(post("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X-Wing\",\"pilot\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pilot").value("Pilot cannot be empty"));

        verify(starshipService, times(0)).addStarship(any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateStarship_NameEmpty() throws Exception {
        mockMvc.perform(put("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"pilot\":\"Luke Skywalker\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name cannot be empty"));

        verify(starshipService, times(0)).updateStarship(eq(1L), any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateStarship_PilotEmpty() throws Exception {
        mockMvc.perform(put("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X-Wing\",\"pilot\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pilot").value("Pilot cannot be empty"));

        verify(starshipService, times(0)).updateStarship(eq(1L), any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateStarship_NameNull() throws Exception {
        mockMvc.perform(put("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":null,\"pilot\":\"Luke Skywalker\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name cannot be empty"));

        verify(starshipService, times(0)).updateStarship(eq(1L), any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateStarship_PilotNull() throws Exception {
        mockMvc.perform(put("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X-Wing\",\"pilot\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pilot").value("Pilot cannot be empty"));

        verify(starshipService, times(0)).updateStarship(eq(1L), any(Starship.class));
    }
}