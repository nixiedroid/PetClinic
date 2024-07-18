package com.nixiedroid.petclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents an owner in the system.
 *
 * <p>This entity is used to store information about owners, including their personal details and
 * the pets they own. It is mapped to the "owners" table in the "site" schema.
 * </p>
 *
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owners",schema = "site")
public class Owner {
    /**
     * The unique identifier of the owner.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The first name of the owner.<br>
     * It is a required field with a maximum length of 50 characters.
     */
    @Column(name = "first_name",nullable = false, length = 50)
    private String firstName;

    /**
     * The last name of the owner.<br>
     * It is a required field with a maximum length of 50 characters.
     */
    @Column(name = "last_name",nullable = false, length = 50)
    private String lastName;

    /**
     * The address of the owner.<br>
     * It is an optional field with a maximum length of 100 characters.
     */
    @Column(name = "address", length = 100)
    private String address;

    /**
     * The city where the owner lives.<br>
     * It is an optional field with a maximum length of 50 characters.
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * The telephone number of the owner.<br>
     * It is an optional field with a maximum length of 20 characters.
     */
    @Column(name = "telephone", length = 20)
    private String telephone;


    /**
     * The list of pets owned by this owner.<br>
     *  It is initialized to an empty list by default and managed using a {@link OneToMany}
     * relationship with cascade operations and orphan removal enabled.
     */
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @Setter(AccessLevel.NONE)
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();


    /**
     * Sets the list of pets for this owner.<br>
     * This method clears the existing list of pets and adds all pets from the provided list.
     *
     * @param ps the list of pets to set
     */
    public void setPets(List<Pet> ps){
        pets.clear();
        pets.addAll(ps);
    }
}