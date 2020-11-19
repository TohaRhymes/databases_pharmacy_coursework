package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "companies")
public class Companies {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "market_cap")
    private Double marketCap;

    @Column(name = "net_profit_margin_pct_annual")
    private Double netProfitMarginPctAnnual;
}
