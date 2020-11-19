package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "diseases")
public class Diseases {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "pathogen_id")
    private Long pathogenId;

    @Column(name = "name")
    private String name;

    @Column(name = "mortality")
    private Double mortality;

    @OneToMany(mappedBy = "ethnoscience")
    Set<EthnoscienceToDiseases> ethnoscienceToDiseases;
}
