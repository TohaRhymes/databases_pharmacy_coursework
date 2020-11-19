package se.ifmo.pepe.coursage.model;

import lombok.Data;
import se.ifmo.pepe.coursage.customtype.PathogenType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pathogens")
public class Pathogens {
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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PathogenType type;

    @Column(name = "action")
    private String action;

}
