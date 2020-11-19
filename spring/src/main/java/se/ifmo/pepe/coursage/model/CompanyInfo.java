package se.ifmo.pepe.coursage.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "company_info")
public class CompanyInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "restore_date")
    private Date restoreDate;

    @Column(name = "market_cap")
    private Double marketCap;

    @Column(name = "net_profit_margin_pct_annual")
    private Double netProfitMarginPctAnnual;
}
