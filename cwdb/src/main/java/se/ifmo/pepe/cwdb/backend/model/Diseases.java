package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "diseases")
public class Diseases implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pathogen_id")
    private Long pathogenId;

    @Column(name = "name")
    private String name;

    @Column(name = "mortality")
    private Double mortality;

    @OneToMany(mappedBy = "ethnoscience", fetch = FetchType.EAGER)
    Set<EthnoscienceToDiseases> ethnoscienceToDiseases;
}
