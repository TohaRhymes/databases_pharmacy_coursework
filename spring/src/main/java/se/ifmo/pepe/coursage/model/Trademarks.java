package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "trademarks")
public class Trademarks {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "entity_id_seq")
    @SequenceGenerator(
            name = "entity_id_seq",
            sequenceName = "hibernate_sequence",
            allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "doze")
    private Double doze;

    @Column(name = "release_price")
    private Double releasePrice;

    @Column(name = "drug_id")
    private Long drugId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "patent_id")
    private Long patentId;
}
