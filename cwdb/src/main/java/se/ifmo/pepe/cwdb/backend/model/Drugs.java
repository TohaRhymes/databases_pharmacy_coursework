package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;
import se.ifmo.pepe.cwdb.backend.customtype.DrugsGroup;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "drugs")
public class Drugs implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "active_substance")
    private String activeSubstance;

    @Column(name = "homeopathy")
    private Boolean homeopathy;

    @Column(name = "drugs_group")
    private String drugsGroup;

    @OneToMany(mappedBy = "poisons")
    Set<DrugsToPoisons> drugsToPoisons;

    @OneToMany(mappedBy = "diseases")
    Set<DrugsToDiseases> drugsToDiseases;
 }
