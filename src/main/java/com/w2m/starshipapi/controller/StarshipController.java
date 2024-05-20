package com.w2m.starshipapi.controller;

import com.w2m.starshipapi.service.StarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.w2m.starshipapi.model.Starship;
import org.springframework.data.web.PageableDefault;
import java.util.List;

@RestController
@RequestMapping("/api/starships")
public class StarshipController {

    @Autowired
    private StarshipService starshipService;

    @GetMapping
    public ResponseEntity<Page<Starship>> getAllStarships(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        Sort.Direction direction = (sortOrder != null && sortOrder.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(direction, (sortBy != null) ? sortBy : "id"));

        Page<Starship> starships = starshipService.getAllStarships(sortedPageable);
        return ResponseEntity.ok(starships);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Starship>> getStarshipsByName(@RequestParam String name) {
        List<Starship> starships = starshipService.getStarshipsByName(name);
        return ResponseEntity.ok(starships);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Starship> getStarshipById(@PathVariable Long id) {
        Starship starship = starshipService.getStarshipById(id).orElseThrow();
        return ResponseEntity.ok(starship);
    }

    @PostMapping
    public ResponseEntity<Starship> addStarship(@RequestBody Starship starship) {
        Starship savedStarship = starshipService.addStarship(starship);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStarship);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Starship> updateStarship(@PathVariable Long id, @RequestBody Starship starshipDetails) {
        Starship updatedStarship = starshipService.updateStarship(id, starshipDetails).orElseThrow();
        return ResponseEntity.ok(updatedStarship);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStarship(@PathVariable Long id) {
        starshipService.deleteStarship(id);
        return ResponseEntity.noContent().build();
    }
}