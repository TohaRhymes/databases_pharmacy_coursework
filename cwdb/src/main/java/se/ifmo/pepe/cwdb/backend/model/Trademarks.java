package se.ifmo.pepe.cwdb.backend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "trademarks")
public class Trademarks implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "doze", nullable = false)
    private Double doze;

    @Column(name = "release_price", nullable = false)
    private Double releasePrice;

    @Column(name = "drug_id")
    private Long drugId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "patent_id")
    private Long patentId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDoze(Double doze) {
        this.doze = doze;
    }

    public void setReleasePrice(Double releasePrice) {
        this.releasePrice = releasePrice;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setPatentId(Long patentId) {
        this.patentId = patentId;
    }
}
