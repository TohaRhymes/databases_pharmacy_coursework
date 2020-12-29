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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "distribution")
    @Enumerated(EnumType.STRING)
    private PatentDistribution distribution;

    @Column(name = "start_date")
    private Date startDate;

    public String resolveDistribution() {
        switch (this.distribution) {
            case free_to_use: {
                return "Free to use";
            }
            case usage_with_constraints: {
                return "Usage with constraints";
            }
            default: {
                return "Restricted to use";
            }
        }
    }

    public static PatentDistribution resolveDistribution(String query) {
        switch (query) {
            case "free_to_use": {
                return PatentDistribution.free_to_use;
            }
            case "usage_with_constraints": {
                return PatentDistribution.usage_with_constraints;
            }
            default: {
                return PatentDistribution.restricted_to_use;
            }
        }
    }
}
