package se.ifmo.pepe.coursage.model;

import lombok.Data;
import se.ifmo.pepe.coursage.customtype.PatentDistribution;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "patents")
public class Patents {
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
