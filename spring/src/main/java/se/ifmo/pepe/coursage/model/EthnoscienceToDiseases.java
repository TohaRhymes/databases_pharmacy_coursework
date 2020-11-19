package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ethnoscience_to_diseases")
public class EthnoscienceToDiseases {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ethnoscience_id")
    private Ethnoscience ethnoscience;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Diseases disease;
}
