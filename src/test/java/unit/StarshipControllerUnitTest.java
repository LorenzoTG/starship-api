package unit;

import com.w2m.starshipapi.StarshipApiApplication;
import com.w2m.starshipapi.exceptions.InternalServerErrorException;
import com.w2m.starshipapi.exceptions.NotFoundException;
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

import java.util.Collections;
import java.util.Optional;

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

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetStarshipsByName() throws Exception {
        Starship starship = new Starship(1L, "X-Wing", "Luke Skywalker");

        when(starshipService.getStarshipsByName("X-Wing")).thenReturn(Collections.singletonList(starship));

        mockMvc.perform(get("/api/starships/search")
                        .param("name", "X-Wing")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("X-Wing"));

        verify(starshipService, times(1)).getStarshipsByName("X-Wing");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetStarshipById() throws Exception {
        Starship starship = new Starship(1L, "X-Wing", "Luke Skywalker");

        when(starshipService.getStarshipById(1L)).thenReturn(Optional.of(starship));

        mockMvc.perform(get("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("X-Wing"));

        verify(starshipService, times(1)).getStarshipById(1L);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetStarshipById_NotFound() throws Exception {
        when(starshipService.getStarshipById(1L)).thenThrow(new NotFoundException("Starship not found with id: 1"));

        mockMvc.perform(get("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Starship not found with id: 1"));

        verify(starshipService, times(1)).getStarshipById(1L);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddStarship() throws Exception {
        Starship starship = new Starship(1L, "X-Wing", "Luke Skywalker");

        when(starshipService.addStarship(any(Starship.class))).thenReturn(starship);

        mockMvc.perform(post("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X-Wing\",\"pilot\":\"Luke Skywalker\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("X-Wing"));

        verify(starshipService, times(1)).addStarship(any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddStarship_InternalServerError() throws Exception {
        when(starshipService.addStarship(any(Starship.class))).thenThrow(new InternalServerErrorException("Could not save starship."));

        mockMvc.perform(post("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X-Wing\",\"pilot\":\"Luke Skywalker\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Could not save starship."));

        verify(starshipService, times(1)).addStarship(any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateStarship() throws Exception {
        Starship starship = new Starship(1L, "X-Wing", "Luke Skywalker");

        when(starshipService.updateStarship(eq(1L), any(Starship.class))).thenReturn(Optional.of(starship));

        mockMvc.perform(put("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X-Wing\",\"pilot\":\"Luke Skywalker\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("X-Wing"));

        verify(starshipService, times(1)).updateStarship(eq(1L), any(Starship.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteStarship() throws Exception {
        doNothing().when(starshipService).deleteStarship(1L);

        mockMvc.perform(delete("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(starshipService, times(1)).deleteStarship(1L);
    }
}