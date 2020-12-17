package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.ifmo.pepe.cwdb.backend.model.Stock;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;

import java.util.List;

public interface StockRepo extends JpaRepository<Stock, Long>, CrudRepository<Stock, Long> {
    List<Stock> findAllByPharmacyId(Long id);
    Stock findByTrademarkId(Long id);

}
