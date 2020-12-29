package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import se.ifmo.pepe.cwdb.backend.model.Companies;

import java.math.BigDecimal;
import java.util.List;

public interface CompanyRepo extends JpaRepository<Companies, Long>, CrudRepository<Companies, Long> {

    Companies findDistinctById(Long id);

    @Procedure(procedureName = "change_economical")
    void randomChangeStatsByCompanyId(Integer id);

}
