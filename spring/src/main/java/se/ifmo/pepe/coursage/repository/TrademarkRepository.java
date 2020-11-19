package se.ifmo.pepe.coursage.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import se.ifmo.pepe.coursage.model.Trademarks;

public interface TrademarkRepository extends PagingAndSortingRepository<Trademarks, Long>, JpaSpecificationExecutor<Trademarks>, CrudRepository<Trademarks, Long> {
}
