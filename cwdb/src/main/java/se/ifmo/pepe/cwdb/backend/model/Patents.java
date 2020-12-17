package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;
import se.ifmo.pepe.cwdb.backend.customtype.PatentDistribution;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table(name = "patents")
public class Patents implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "distribution")
    @Enumerated(EnumType.STRING)
    private PatentDistribution distribution;

    @Column(name = "start_date")
    private Date startDate;
}
