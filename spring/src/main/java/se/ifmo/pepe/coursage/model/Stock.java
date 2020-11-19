package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "trademark_id")
    private Long trademarkId;

    @Column(name = "availability")
    private Long availability;
}
