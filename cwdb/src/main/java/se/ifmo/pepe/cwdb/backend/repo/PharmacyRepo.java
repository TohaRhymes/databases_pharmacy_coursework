package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.ifmo.pepe.cwdb.backend.model.Pharmacies;

public interface PharmacyRepo extends JpaRepository<Pharmacies, Long>, CrudRepository<Pharmacies, Long> {
    Pharmacies findFirstByName(String name);
}
