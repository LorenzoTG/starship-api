package unit;

import com.w2m.starshipapi.StarshipApiApplication;
import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.repository.StarshipRepository;
import com.w2m.starshipapi.service.StarshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = StarshipApiApplication.class)
public class ServiceCachingTest {

    @Autowired
    private StarshipService starshipService;

    @MockBean
    private StarshipRepository starshipRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStarshipByIdCaching() {
        Starship starship = new Starship(1L, "Millennium Falcon", "Han Solo");
        when(starshipRepository.findById(1L)).thenReturn(Optional.of(starship));

        Starship result1 = starshipService.getStarshipById(1L).get();
        assertThat(result1).isEqualTo(starship);

        Starship result2 = starshipService.getStarshipById(1L).get();
        assertThat(result2).isEqualTo(starship);

        verify(starshipRepository, times(1)).findById(1L);
    }
}
