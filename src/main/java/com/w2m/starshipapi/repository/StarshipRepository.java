package com.w2m.starshipapi.repository;

import com.w2m.starshipapi.model.Starship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarshipRepository extends JpaRepository<Starship, Long> {
}
