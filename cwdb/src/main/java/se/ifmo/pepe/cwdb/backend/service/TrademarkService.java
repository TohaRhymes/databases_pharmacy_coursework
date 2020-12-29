package se.ifmo.pepe.cwdb.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.pepe.cwdb.backend.model.Drugs;
import se.ifmo.pepe.cwdb.backend.model.Patents;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;
import se.ifmo.pepe.cwdb.backend.repo.DrugRepo;
import se.ifmo.pepe.cwdb.backend.repo.PatentRepo;
import se.ifmo.pepe.cwdb.backend.repo.TrademarkRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TrademarkService {
    private static final Logger LOGGER = Logger.getLogger(TrademarkService.class.getName());
    private final TrademarkRepo trademarkRepository;
    private final DrugRepo drugRepository;
    private final PatentRepo patentRepository;

    public TrademarkService(TrademarkRepo trademarkRepository, DrugRepo drugRepository, PatentRepo patentRepository) {
        this.trademarkRepository = trademarkRepository;
        this.drugRepository = drugRepository;
        this.patentRepository = patentRepository;
    }

    public List<Trademarks> findAll() {
        return trademarkRepository.findAll();
    }

    public List<Trademarks> findAll(String query) {
        if (query == null || query.isEmpty()) {
            return trademarkRepository.findAll().stream().sorted(Comparator.comparing(Trademarks::getId)).collect(Collectors.toList());
        } else {
            return trademarkRepository.searchByName(query);
        }
    }

    public List<String> findAnyByName(String query) {
        List<String> names = new ArrayList<>();
        drugRepository.searchByName(query).forEach(e -> {
            names.add(e.getActiveSubstance());
        });
        return names;
    }

    public Drugs findDrugByDrugId(Long id) {
        if (drugRepository.findById(id).isPresent())
            return drugRepository.findById(id).get();
        else
            return null;
    }

    public Patents findPatentByPatentId(Long id) {
        if (patentRepository.findById(id).isPresent())
            return patentRepository.findById(id).get();
        else
            return null;
    }

    public Long count() {
        return trademarkRepository.count();
    }

    public Long countPatents() {
        return patentRepository.count();
    }

    public void delete(Trademarks t) {
        trademarkRepository.delete(t);
    }

    public String validate(String query) {
        if (drugRepository.searchByName(query).isEmpty())
            return String.format("'%s' is not available", query);
        return null;
    }

    @Transactional
    public void save(Trademarks t, String activeSubstance, String distribution) throws TrademarkServiceSavingException {
        if (t.getName() == null || t.getDoze() == null || t.getReleasePrice() == null) {
            LOGGER.log(Level.SEVERE, "tf r u tryna to save null-object. getta fuck out");
            throw new TrademarkServiceSavingException("tf r u tryna to save null-object. getta fuck out");
        } else {
            if (trademarkRepository.existsById(t.getId())) {
                  /*
                  Drugs d = drugRepository.findById(t.getDrugId()).get();
                d.setActiveSubstance(activeSubstance);
                Patents p = patentRepository.findById(t.getPatentId()).get();
                p.setDistribution(PatentDistribution.parse(distribution.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_")));



              trademarkRepository.save(t);
                drugRepository.save(d);
                patentRepository.naiveSave(p.getId(),
                        distribution.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_"),
                        p.getStartDate());
                System.out.println(t);
                System.out.println(d);
                System.out.println(p);

                   */
                t.setDrugId(drugRepository.findByActiveSubstance(activeSubstance).getId());
                trademarkRepository.updateTrademark(t.getCompanyId().intValue(),
                        t.getCompanyId().intValue(),
                        t.getDrugId().intValue(),
                        t.getName(),
                        BigDecimal.valueOf(t.getDoze()),
                        BigDecimal.valueOf(t.getReleasePrice()),
                        distribution.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_"));
            } else {
                t.setDrugId(drugRepository.findByActiveSubstance(activeSubstance).getId());
                trademarkRepository.addTrademark(t.getCompanyId().intValue(),
                        t.getDrugId().intValue(),
                        t.getName(),
                        BigDecimal.valueOf(t.getDoze()),
                        BigDecimal.valueOf(t.getReleasePrice()),
                        distribution.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_"));
            }
        }

//            trademarkRepository.save(t);
    }

    public Drugs findDrugByActiveSubstance(String activeSubstance) {
        return drugRepository.findByActiveSubstance(activeSubstance);
    }

    public static class TrademarkServiceSavingException extends Exception {
        public TrademarkServiceSavingException(String msg) {
            super(msg);
        }
    }
}
