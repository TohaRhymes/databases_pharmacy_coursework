package se.ifmo.pepe.coursage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.ifmo.pepe.coursage.model.Trademarks;
import se.ifmo.pepe.coursage.repository.TrademarkRepository;
import se.ifmo.pepe.coursage.util.TrademarkDataTableFilter;

@Service
public class TrademarkService {
    private final TrademarkRepository trademarkRepository;

    @Autowired
    public TrademarkService(TrademarkRepository trademarkRepository) {
        this.trademarkRepository = trademarkRepository;
    }

    public Page<Trademarks> getTrademarksForDatable(String queryString, Pageable pageable) {
        TrademarkDataTableFilter trademarkDatableFilter = new TrademarkDataTableFilter(queryString);
        return trademarkRepository.findAll(trademarkDatableFilter, pageable);
    }
}
