package se.ifmo.pepe.coursage.model;

import lombok.Data;
import se.ifmo.pepe.coursage.customtype.DrugsGroup;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "drugs")
public class Drugs {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "active_substance")
    private String activeSubstance;

    @Column(name = "homeopathy")
    private Boolean homeopathy;

    @Column(name = "drugs_group")
    @Enumerated(EnumType.STRING)
    private DrugsGroup drugsGroup;

    @OneToMany(mappedBy = "poisons")
    Set<DrugsToPoisons> drugsToPoisons;

    @OneToMany(mappedBy = "diseases")
    Set<DrugsToDiseases> drugsToDiseases;
 }
