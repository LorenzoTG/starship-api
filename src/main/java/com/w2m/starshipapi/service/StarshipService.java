package com.w2m.starshipapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.repository.StarshipRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.w2m.starshipapi.exceptions.NotFoundException;
import com.w2m.starshipapi.exceptions.InternalServerErrorException;
import com.w2m.starshipapi.exceptions.NoContentException;

@Service
public class StarshipService {
    @Autowired
    private StarshipRepository starshipRepository;

    public List<Starship> getStarshipsByName(String name) {
        List<Starship> starships = starshipRepository.findByNameContainingIgnoreCase(name);
        if (starships.isEmpty()) {
            throw new NoContentException("No starships found containing name: " + name);
        }
        return starships;
    }

    public Page<Starship> getAllStarships(Pageable pageable) {
        Page<Starship> starships = starshipRepository.findAll(pageable);
        if (starships.isEmpty()) {
            throw new NoContentException("No starships available.");
        }
        return starships;
    }

    public Optional<Starship> getStarshipById(Long id) {
        return Optional.ofNullable(starshipRepository.findById(id).orElseThrow(() -> new NotFoundException("Starship not found with id: " + id)));
    }

    public Starship addStarship(Starship starship) {
        try {
            return starshipRepository.save(starship);
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not save starship: " + e.getMessage());
        }
    }

    public Optional<Starship> updateStarship(Long id, Starship starshipDetails) {
        Optional<Starship> existingStarship = starshipRepository.findById(id);

        if (existingStarship.isPresent()) {
            Starship starship = existingStarship.get();
            starship.setName(starshipDetails.getName());
            starship.setPilot(starshipDetails.getPilot());
            try {
                return Optional.of(starshipRepository.save(starship));
            } catch (Exception e) {
                throw new InternalServerErrorException("Could not update starship: " + e.getMessage());
            }
        } else {
            throw new NotFoundException("Starship not found with id: " + id);
        }
    }

    public void deleteStarship(Long id) {
        try {
            starshipRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not delete starship: " + e.getMessage());
        }
    }
}
