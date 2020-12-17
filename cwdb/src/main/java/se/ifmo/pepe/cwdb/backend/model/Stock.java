package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "stock")
public class Stock implements Serializable {
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

    public Stock setId(Long id) {
        this.id = id;
        return this;
    }

    public Stock setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
        return this;
    }

    public Stock setTrademarkId(Long trademarkId) {
        this.trademarkId = trademarkId;
        return this;
    }

    public Stock setAvailability(Long availability) {
        this.availability = availability;
        return this;
    }
}
