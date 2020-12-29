package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;
import se.ifmo.pepe.cwdb.backend.customtype.DrugsGroup;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "drugs")
public class Drugs implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active_substance")
    private String activeSubstance;

    @Column(name = "homeopathy")
    private Boolean homeopathy;

    @Column(name = "drugs_group")
    @Enumerated(EnumType.STRING)
    private DrugsGroup drugsGroup;

    @OneToMany(mappedBy = "poisons", fetch = FetchType.LAZY)
    Set<DrugsToPoisons> drugsToPoisons;

    @OneToMany(mappedBy = "diseases", fetch = FetchType.LAZY)
    Set<DrugsToDiseases> drugsToDiseases;


}
