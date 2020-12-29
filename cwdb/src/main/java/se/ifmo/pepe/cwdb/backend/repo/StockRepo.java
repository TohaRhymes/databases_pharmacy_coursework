package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.ifmo.pepe.cwdb.backend.model.Stock;

import java.util.List;

public interface StockRepo extends JpaRepository<Stock, Long>, CrudRepository<Stock, Long> {
    List<Stock> findAllByPharmacyId(Long id);

    Stock findByTrademarkId(Long id);

    @Procedure(procedureName = "add_to_stock")
    Integer addToStock(Integer _pharmacy_id, Integer _trademark_id, Integer _availability);

}
