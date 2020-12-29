package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ethnoscience_to_diseases")
public class EthnoscienceToDiseases implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ethnoscience_id")
    private Ethnoscience ethnoscience;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Diseases disease;
}
