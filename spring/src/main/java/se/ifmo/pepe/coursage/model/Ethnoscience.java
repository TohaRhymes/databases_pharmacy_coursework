package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "ethnoscience")
public class Ethnoscience {
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
