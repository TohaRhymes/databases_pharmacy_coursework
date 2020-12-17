package se.ifmo.pepe.cwdb.backend.service;

import org.springframework.stereotype.Service;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;
import se.ifmo.pepe.cwdb.backend.repo.TrademarkRepo;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TrademarkService {
    private static final Logger LOGGER = Logger.getLogger(TrademarkService.class.getName());
    private final TrademarkRepo trademarkRepository;

    public TrademarkService(TrademarkRepo trademarkRepository) {
        this.trademarkRepository = trademarkRepository;
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

    public Long count() {
        return trademarkRepository.count();
    }

    public void delete(Trademarks t) {
        trademarkRepository.delete(t);
    }

    public void save(Trademarks t) throws TrademarkServiceSavingException {
        if (t.getName() == null || t.getDoze() == null || t.getReleasePrice() == null) {
            LOGGER.log(Level.SEVERE, "tf r u tryna to save null-object. getta fuck out");
            throw new TrademarkServiceSavingException("tf r u tryna to save null-object. getta fuck out");
        } else
            trademarkRepository.save(t);
    }

    public static class TrademarkServiceSavingException extends Exception {
        public TrademarkServiceSavingException(String msg) {
            super(msg);
        }
    }
}
