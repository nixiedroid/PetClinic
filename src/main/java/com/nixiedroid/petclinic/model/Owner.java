package com.nixiedroid.petclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owners",schema = "site")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name",nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name",nullable = false, length = 50)
    private String lastName;
    @Column(name = "address", length = 100)
    private String address;
    @Column(name = "city", length = 50)
    private String city;
    @Column(name = "telephone", length = 20)
    private String telephone;

    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

}