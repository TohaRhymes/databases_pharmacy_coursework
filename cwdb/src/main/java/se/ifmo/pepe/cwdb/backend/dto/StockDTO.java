package se.ifmo.pepe.cwdb.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockDTO implements Serializable {
    private String pharmacyName;
    private String trademarkName;
    private Long pharmacyId;
    private Long companyId;
    private Long availability;


    //--

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public void setTrademarkName(String trademarkName) {
        this.trademarkName = trademarkName;
    }

    public void setAvailability(Long availability) {
        this.availability = availability;
    }
}


