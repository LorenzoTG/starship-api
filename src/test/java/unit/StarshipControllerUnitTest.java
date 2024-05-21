package unit;

import com.w2m.starshipapi.StarshipApiApplication;
import com.w2m.starshipapi.exceptions.InternalServerErrorException;
import com.w2m.starshipapi.exceptions.NoContentException;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
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
    public void testGetAllStarships() throws Exception {
        Starship starship1 = new Starship(1L, "X-Wing", "Luke Skywalker");
        Starship starship2 = new Starship(2L, "TIE Fighter", "Darth Vader");

        when(starshipService.getAllStarships(any())).thenReturn(new PageImpl<>(Arrays.asList(starship1, starship2)));

        mockMvc.perform(get("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("X-Wing"))
                .andExpect(jsonPath("$.content[1].name").value("TIE Fighter"));

        verify(starshipService, times(1)).getAllStarships(any());
    }

    @Test
    public void testGetAllStarships_NoContent() throws Exception {
        when(starshipService.getAllStarships(any())).thenThrow(new NoContentException("No starships available."));

        mockMvc.perform(get("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No starships available."));

        verify(starshipService, times(1)).getAllStarships(any());
    }

    @Test
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
    public void testGetStarshipById_NotFound() throws Exception {
        when(starshipService.getStarshipById(1L)).thenThrow(new NotFoundException("Starship not found with id: 1"));

        mockMvc.perform(get("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Starship not found with id: 1"));

        verify(starshipService, times(1)).getStarshipById(1L);
    }

    @Test
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
    public void testDeleteStarship() throws Exception {
        doNothing().when(starshipService).deleteStarship(1L);

        mockMvc.perform(delete("/api/starships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(starshipService, times(1)).deleteStarship(1L);
    }
}