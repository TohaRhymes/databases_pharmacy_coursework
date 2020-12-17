package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "drugs_to_poisons")
public class DrugsToPoisons implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drugs_id")
    private Drugs drugs;

    @ManyToOne
    @JoinColumn(name = "poison_id")
    private Poisons poisons;
}
