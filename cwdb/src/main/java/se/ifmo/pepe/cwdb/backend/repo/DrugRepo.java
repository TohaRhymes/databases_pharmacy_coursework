package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.ifmo.pepe.cwdb.backend.model.Drugs;

public interface DrugRepo extends JpaRepository<Drugs, Long>, CrudRepository<Drugs, Long> {
}
