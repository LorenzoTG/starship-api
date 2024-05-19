package com.w2m.starshipapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.w2m.starshipapi.model.Starship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarshipRepository extends JpaRepository<Starship, Long> {
    List<Starship> findByNameContainingIgnoreCase(String name);
    Page<Starship> findAll(Pageable pageable);
}
