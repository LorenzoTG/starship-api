package com.w2m.starshipapi.service;

import com.w2m.starshipapi.exceptions.NoContentException;
import com.w2m.starshipapi.exceptions.NotFoundException;
import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.repository.StarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StarshipService {

    @Autowired
    private StarshipRepository starshipRepository;

    @Cacheable(value = "starshipsByName", key = "#name")
    public List<Starship> getStarshipsByName(String name) {
        List<Starship> starships = starshipRepository.findByNameContainingIgnoreCase(name);
        if (starships.isEmpty()) {
            throw new NoContentException("No starships found containing name: " + name);
        }
        return starships;
    }

    @Cacheable(value = "allStarships", key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public Page<Starship> getAllStarships(Pageable pageable) {
        Page<Starship> starships = starshipRepository.findAll(pageable);

        if (starships == null || starships.isEmpty()) {
            return Page.empty();
        }

        return starships;
    }


    @Cacheable(value = "starshipById", key = "#id")
    public Optional<Starship> getStarshipById(Long id) {
        return Optional.ofNullable(starshipRepository.findById(id).orElseThrow(() -> new NotFoundException("Starship not found with id: " + id)));
    }

    @CacheEvict(value = {"starshipsByName", "allStarships"}, allEntries = true)
    @CachePut(value = "starshipById", key = "#result.id")
    public Starship addStarship(Starship starship) {
        return starshipRepository.save(starship);
    }

    @CacheEvict(value = {"starshipsByName", "allStarships"}, allEntries = true)
    @CachePut(value = "starshipById", key = "#id")
    public Optional<Starship> updateStarship(Long id, Starship starshipDetails) {
        return starshipRepository.findById(id).map(starship -> {
            starship.setName(starshipDetails.getName());
            starship.setPilot(starshipDetails.getPilot());
            return starshipRepository.save(starship);
        });
    }

    @CacheEvict(value = {"starshipsByName", "allStarships", "starshipById"}, allEntries = true)
    public void deleteStarship(Long id) {
        starshipRepository.deleteById(id);
    }

    // New method for pagination and sorting logic
    public Page<Starship> getAllStarships(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = (sortOrder != null && sortOrder.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, (sortBy != null) ? sortBy : "id"));

        return getAllStarships(pageable);
    }
}