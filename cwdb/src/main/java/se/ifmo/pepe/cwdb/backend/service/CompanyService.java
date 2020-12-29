package se.ifmo.pepe.cwdb.backend.service;

import org.springframework.stereotype.Service;
import se.ifmo.pepe.cwdb.backend.model.Companies;
import se.ifmo.pepe.cwdb.backend.model.CompanyInfo;
import se.ifmo.pepe.cwdb.backend.repo.CompanyInfoRepo;
import se.ifmo.pepe.cwdb.backend.repo.CompanyRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CompanyService {
    private static final Logger LOGGER = Logger.getLogger(CompanyService.class.getName());
    private final CompanyRepo companyRepository;
    private final CompanyInfoRepo companyInfoRepository;

    public CompanyService(CompanyRepo companyRepository, CompanyInfoRepo companyInfoRepository) {
        this.companyRepository = companyRepository;
        this.companyInfoRepository = companyInfoRepository;
    }

    public List<Companies> findAll() {
        return companyRepository.findAll();
    }

    public Companies findById(Long id) {
        return companyRepository.findDistinctById(id);
    }

    public void delete(Companies t) {
        companyRepository.delete(t);
    }

    public void save(Companies c) {
        if (c == null) {
            LOGGER.log(Level.SEVERE, "tf r u tryna to save null-object. getta fuck out");
            return;
        }
        companyRepository.save(c);
    }

    public Companies updateStats(Long companyId) {
        companyRepository.randomChangeStatsByCompanyId(companyId.intValue());
        return companyRepository.findById(companyId).get();
    }

    public List<CompanyInfo> findAllInfoByCompanyId(Long companyId) {
        return companyInfoRepository.findAllByCompanyId(companyId);
    }
}
