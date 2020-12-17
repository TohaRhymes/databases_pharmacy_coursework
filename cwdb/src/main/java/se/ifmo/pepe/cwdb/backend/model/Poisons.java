package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;
import se.ifmo.pepe.cwdb.backend.customtype.PoisonOrigin;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "poisons")
public class Poisons implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "active_substance")
    private String activeSubstance;

    @Column(name = "type_by_action")
    private String typeByAction;

    @Column(name = "type_by_origin")
    @Enumerated(EnumType.STRING)
    private PoisonOrigin typeByOrigin;

    @Column(name = "mortality")
    private Double mortality;

    @OneToMany(mappedBy = "drugs")
    Set<DrugsToPoisons> drugsToPoisons;

}
