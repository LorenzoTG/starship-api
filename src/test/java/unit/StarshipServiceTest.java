package unit;

import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.repository.StarshipRepository;
import com.w2m.starshipapi.service.StarshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StarshipServiceTest {

    @InjectMocks
    private StarshipService starshipService;

    @Mock
    private StarshipRepository starshipRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStarshipsReturnsValues() {

        Starship starship = new Starship();
        starship.setId(1L);
        starship.setName("Starship 1");

        List<Starship> starshipList = List.of(starship);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Starship> starshipPage = new PageImpl<>(starshipList, pageable, starshipList.size());

        when(starshipRepository.findAll(pageable)).thenReturn(starshipPage);

        Page<Starship> result = starshipService.getAllStarships(0, 10, "id", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Starship 1", result.getContent().get(0).getName());
    }

    @Test
    public void testGetAllStarshipsReturnsNoContent() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Starship> emptyPage = Page.empty(pageable);

        when(starshipRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<Starship> result = starshipService.getAllStarships(0, 10, "id", "asc");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
