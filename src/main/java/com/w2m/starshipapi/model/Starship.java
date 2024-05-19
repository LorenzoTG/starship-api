package com.w2m.starshipapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="Starships")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Starship {

    private Long id;
    private String title;
    private String author;
}
