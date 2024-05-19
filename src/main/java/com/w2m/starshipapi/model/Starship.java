package com.w2m.starshipapi.model;

import jakarta.persistence.*;
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

    private String name;

    private String pilot;
}
