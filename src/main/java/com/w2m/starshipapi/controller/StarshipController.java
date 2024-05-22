package com.w2m.starshipapi.controller;

import com.w2m.starshipapi.service.StarshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import com.w2m.starshipapi.model.Starship;
import java.util.List;

@RestController
@RequestMapping("/api/starships")
public class StarshipController {

    private static final Logger logger = LoggerFactory.getLogger(StarshipController.class);

    @Autowired
    private StarshipService starshipService;

    @Operation(summary = "Get all starships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved starship",
                    content = @Content(schema = @Schema(implementation = Starship.class))),
            @ApiResponse(responseCode = "204", description = "Starships not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Starship>> getAllStarships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        Page<Starship> starships = starshipService.getAllStarships(page, size, sortBy, sortOrder);
        if (starships.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(starships);
        }
    }

    @Operation(summary = "Get all starships by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved starships",
                    content = @Content(schema = @Schema(implementation = Starship.class))),
            @ApiResponse(responseCode = "204", description = "Starships not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Starship>> getStarshipsByName(@RequestParam String name) {
        List<Starship> starships = starshipService.getStarshipsByName(name);
        return ResponseEntity.ok(starships);
    }

    @Operation(summary = "Get a starship by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved starship",
                    content = @Content(schema = @Schema(implementation = Starship.class))),
            @ApiResponse(responseCode = "404", description = "Starship not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Starship> getStarshipById(@PathVariable Long id) {
        Starship starship = starshipService.getStarshipById(id).orElseThrow();
        return ResponseEntity.ok(starship);
    }

    @Operation(summary = "Post new starship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Starship created",
                    content = @Content(schema = @Schema(implementation = Starship.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Starship> addStarship(@RequestBody Starship starship) {
        Starship savedStarship = starshipService.addStarship(starship);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStarship);
    }

    @Operation(summary = "Put a new starship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Starship created",
                    content = @Content(schema = @Schema(implementation = Starship.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Starship> updateStarship(@PathVariable Long id, @RequestBody Starship starshipDetails) {
        Starship updatedStarship = starshipService.updateStarship(id, starshipDetails).orElseThrow();
        return ResponseEntity.ok(updatedStarship);
    }

    @Operation(summary = "Delete a starship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Starship deleted"),
            @ApiResponse(responseCode = "404", description = "Starship not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStarship(@PathVariable Long id) {
        starshipService.deleteStarship(id);
        return ResponseEntity.noContent().build();
    }
}