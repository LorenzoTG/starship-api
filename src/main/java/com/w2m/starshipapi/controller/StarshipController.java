package com.w2m.starshipapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.service.StarshipService;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/starships") // Base URL for all endpoints
public class StarshipController {

    @Autowired
    private StarshipService starshipService;

    // Get all starships with pagination and sort methods
    @GetMapping
    public ResponseEntity<Page<Starship>> getAllStarships(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder
    ) {
        try {
            // Default sorting direction is ascending
            Sort.Direction direction = (sortOrder != null && sortOrder.equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC : Sort.Direction.ASC;

            // Create a pageable with sorting
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(direction, (sortBy != null) ? sortBy : "id"));

            Page<Starship> starships = starshipService.getAllStarships(sortedPageable);

            if (starships.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(starships, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Get all starships that contain certain string value
    @GetMapping("/search")
    public ResponseEntity<List<Starship>> getStarshipsByName(@RequestParam String name) {
        try {
            List<Starship> starships = starshipService.getStarshipsByName(name);
            if (starships.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(starships, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get starship by ID
    @GetMapping("/{id}")
    public ResponseEntity<Starship> getStarshipById(@PathVariable Long id){
        try{
            Optional<Starship> starship = starshipService.getStarshipById(id);
            return starship.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add a new starship
    @PostMapping
    public ResponseEntity<Starship> addStarship(@RequestBody Starship starship){
        try{
            Starship savedStarship = starshipService.addStarship(starship);
            return new ResponseEntity<>(savedStarship, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing starship
    @PutMapping("/{id}")
    public ResponseEntity<Starship> updateStarship(@PathVariable Long id, @RequestBody Starship starshipDetails) {
        try {
            Optional<Starship> updatedStarship = starshipService.updateStarship(id, starshipDetails);
            return updatedStarship
                    .map(starship -> new ResponseEntity<>(starship, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a starship
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStarship(@PathVariable Long id) {
        try {
            starshipService.deleteStarship(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}