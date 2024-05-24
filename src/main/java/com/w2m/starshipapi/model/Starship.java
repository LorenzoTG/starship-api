package com.w2m.starshipapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name="Starships")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Starship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Pilot cannot be empty")
    private String pilot;
}