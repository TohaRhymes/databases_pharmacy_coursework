package se.ifmo.pepe.coursage.model;

import lombok.Data;
import se.ifmo.pepe.coursage.customtype.DevelopmentStage;

import javax.persistence.*;

@Data
@Entity
@Table(name = "development")
public class Development {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "pathogen_id")
    private Long pathogenId;

    @Column(name = "testing_stage")
    @Enumerated(EnumType.STRING)
    private DevelopmentStage testingStage;

    @Column(name = "failed")
    private Boolean failed;
}
