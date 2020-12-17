package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "ethnoscience")
public class Ethnoscience implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "origin")
    private String origin;

    @OneToMany(mappedBy = "disease")
    Set<EthnoscienceToDiseases> ethnoscienceToDiseases;
}
