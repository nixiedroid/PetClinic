package com.nixiedroid.petclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Represents a pet in the system.
 *<br>
 *This entity is used to store information about pets, including their name, birthdate,
 * type, and the owner to whom they belong. It is mapped to the "pets" table in the "site" schema.
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets",schema = "site")
public class Pet {
    /**
     * The unique identifier of the pet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the pet.
     * <br>It is a required field with a maximum length of 50 characters.
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * The birthdate of the pet.
     * <br>It is an optional field.
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * The type of the pet.
     * <br>It is a required field with a maximum length of 30 characters.
     */
    @Column(name = "type", nullable = false, length = 30)
    private String type;

    /**
     * The owner of the pet.
     * <br>It is managed using a {@link ManyToOne} relationship with cascade operations for persist,
     * merge, and refresh. Fetch type is set to LAZY.
     */
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

}