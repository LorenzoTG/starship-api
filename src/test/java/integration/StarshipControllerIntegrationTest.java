package integration;

import com.w2m.starshipapi.StarshipApiApplication;
import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.repository.StarshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StarshipApiApplication.class)
@AutoConfigureMockMvc
public class StarshipControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StarshipRepository starshipRepository;

    @BeforeEach
    public void setup() {
        starshipRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllStarships() throws Exception {
        Starship starship1 = new Starship(null, "X-Wing", "Luke Skywalker");
        Starship starship2 = new Starship(null, "TIE Fighter", "Darth Vader");

        starshipRepository.save(starship1);
        starshipRepository.save(starship2);

        mockMvc.perform(get("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("X-Wing"))
                .andExpect(jsonPath("$.content[1].name").value("TIE Fighter"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddStarship() throws Exception {
        String starshipJson = "{\"name\":\"X-Wing\",\"pilot\":\"Luke Skywalker\"}";

        mockMvc.perform(post("/api/starships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(starshipJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("X-Wing"))
                .andExpect(jsonPath("$.pilot").value("Luke Skywalker"));

        Optional<Starship> savedStarship = starshipRepository.findByNameContainingIgnoreCase("X-Wing").stream().findFirst();
        assert(savedStarship.isPresent());
        assert(savedStarship.get().getPilot().equals("Luke Skywalker"));
    }
}