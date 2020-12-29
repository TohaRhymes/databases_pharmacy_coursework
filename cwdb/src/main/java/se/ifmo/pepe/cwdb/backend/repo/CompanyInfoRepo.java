package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.ifmo.pepe.cwdb.backend.model.CompanyInfo;

import java.util.List;

public interface CompanyInfoRepo extends JpaRepository<CompanyInfo, Long>, CrudRepository<CompanyInfo, Long> {
    List<CompanyInfo> findAllByCompanyId(Long companyId);
}
