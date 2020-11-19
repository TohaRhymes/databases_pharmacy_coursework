package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pharmacies")
public class Pharmacies {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price_mul")
    private Double priceMul;

    @Column(name = "price_plus")
    private Double pricePlus;
}
