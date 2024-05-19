package com.w2m.starshipapi.controller;

import com.w2m.starshipapi.model.Starship;
import com.w2m.starshipapi.repository.StarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/starships") // Base URL for all endpoints
public class StarshipController {

    @Autowired
    private StarshipRepository starshipRepo;

    // Get all starships
    @GetMapping
    public ResponseEntity<List<Starship>> getAllStarships(){
        try{
            //List<Starship> starships = starshipRepo.findAll();
            List<Starship> starships = new ArrayList<>();
            starshipRepo.findAll().forEach(starships::add);

            if (starships.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(starships, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get starship by ID
    @GetMapping("/{id}")
    public ResponseEntity<Starship> getStarshipById(@PathVariable Long id){
        try{
            Optional<Starship> starship = starshipRepo.findById(id);
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
            Starship savedStarship = starshipRepo.save(starship);
            return new ResponseEntity<>(savedStarship, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing starship
    @PutMapping("/{id}")
    public ResponseEntity<Starship> updateStarship(@PathVariable Long id, @RequestBody Starship starshipDetails){
        try{
            Optional<Starship> existingStarship = starshipRepo.findById(id);
            if (existingStarship.isPresent()) {
                Starship starship = existingStarship.get();
                starship.setName(starshipDetails.getName());
                Starship updatedStarship = starshipRepo.save(starship);
                return new ResponseEntity<>(updatedStarship, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
           } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a starship
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteStarship(@PathVariable Long id){
        try{
            starshipRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}