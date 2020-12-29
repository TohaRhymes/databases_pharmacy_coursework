package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;
import se.ifmo.pepe.cwdb.backend.customtype.PathogenType;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "pathogens")
public class Pathogens implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PathogenType type;

    @Column(name = "action")
    private String action;

}
