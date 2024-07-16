package com.nixiedroid.petclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets",schema = "site")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false, length = 50)
    private String name;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "type",nullable = false, length = 30)
    private String type;
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
    }, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

}