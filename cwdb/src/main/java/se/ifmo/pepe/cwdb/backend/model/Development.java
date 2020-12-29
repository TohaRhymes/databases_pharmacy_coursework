package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;
import se.ifmo.pepe.cwdb.backend.customtype.DevelopmentStage;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "development")
public class Development implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
