package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.pepe.cwdb.backend.customtype.PatentDistribution;
import se.ifmo.pepe.cwdb.backend.model.Patents;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;

import java.sql.Date;

public interface PatentRepo extends JpaRepository<Patents, Long>, CrudRepository<Patents, Long> {

    @Modifying
    @Query("update Patents p set p.id = :id, p.distribution = :distribution, p.startDate = :start_date")
    void naiveSave(@Param("id")Long id,
                   @Param("distribution") String distribution,
                   @Param("start_date") Date startDate);
}
