package com.w2m.starshipapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.repository.StarshipRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StarshipService {
    @Autowired
    private StarshipRepository starshipRepository;

    public List<Starship> getStarshipsByName(String name) {
        return starshipRepository.findByNameContainingIgnoreCase(name);
    }

    public Page<Starship> getAllStarships(Pageable pageable) {
        return starshipRepository.findAll(pageable);
    }

    public Optional<Starship> getStarshipById(Long id) {
        return starshipRepository.findById(id);
    }

    public Starship addStarship(Starship starship) {
        return starshipRepository.save(starship);
    }

    public Optional<Starship> updateStarship(Long id, Starship starshipDetails) {
        Optional<Starship> existingStarship = starshipRepository.findById(id);

        if (existingStarship.isPresent()) {
            Starship starship = existingStarship.get();
            starship.setName(starshipDetails.getName());
            starship.setPilot(starshipDetails.getPilot());
            return Optional.of(starshipRepository.save(starship));
        } else {
            return Optional.empty();
        }
    }

    public void deleteStarship(Long id) {
        starshipRepository.deleteById(id);
    }
}
