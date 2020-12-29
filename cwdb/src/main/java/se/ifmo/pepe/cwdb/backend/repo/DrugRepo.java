package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import se.ifmo.pepe.cwdb.backend.model.Drugs;

import java.util.List;

public interface DrugRepo extends JpaRepository<Drugs, Long>, CrudRepository<Drugs, Long> {
    Drugs findByActiveSubstance(String activeSubstance);

    @Query("select d from Drugs d where lower(d.activeSubstance) like lower(concat('%', :searchTerm, '%'))")
    List<Drugs> searchByName(@Param("searchTerm") String searchTerm);

}
