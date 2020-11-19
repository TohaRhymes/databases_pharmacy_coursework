package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "drugs_to_diseases")
public class DrugsToDiseases {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drugs_id")
    private Drugs drugs;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Diseases diseases;

}
