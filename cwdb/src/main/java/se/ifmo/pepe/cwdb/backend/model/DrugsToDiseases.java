package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "drugs_to_diseases")
public class DrugsToDiseases implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drugs_id")
    private Drugs drugs;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Diseases diseases;

}
