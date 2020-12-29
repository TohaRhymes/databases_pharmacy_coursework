package se.ifmo.pepe.cwdb.backend.service;

import org.springframework.stereotype.Service;
import se.ifmo.pepe.cwdb.backend.dto.StockDTO;
import se.ifmo.pepe.cwdb.backend.model.Drugs;
import se.ifmo.pepe.cwdb.backend.model.Pharmacies;
import se.ifmo.pepe.cwdb.backend.model.Stock;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;
import se.ifmo.pepe.cwdb.backend.repo.DrugRepo;
import se.ifmo.pepe.cwdb.backend.repo.PharmacyRepo;
import se.ifmo.pepe.cwdb.backend.repo.StockRepo;
import se.ifmo.pepe.cwdb.backend.repo.TrademarkRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class StockService {
    private static final Logger LOGGER = Logger.getLogger(StockService.class.getName());
    private final StockRepo stockRepository;
    private final TrademarkRepo trademarkRepository;
    private final PharmacyRepo pharmacyRepository;
    private final DrugRepo drugRepository;

    public StockService(StockRepo stockRepository, TrademarkRepo trademarkRepository, PharmacyRepo pharmacyRepository, DrugRepo drugRepository) {
        this.stockRepository = stockRepository;
        this.trademarkRepository = trademarkRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.drugRepository = drugRepository;
    }

    public List<StockDTO> findAll(Long id) {
        List<Stock> plainStock = stockRepository.findAllByPharmacyId(id);
        List<StockDTO> stockDTOList = new ArrayList<>();
        plainStock.forEach(s -> {
            StockDTO current = new StockDTO();
            current.setAvailability(s.getAvailability());
            current.setTrademarkName(trademarkRepository.findById(s.getTrademarkId()).isPresent() ? trademarkRepository.findById(s.getTrademarkId()).get().getName() : "n/a");
            current.setPharmacyName(pharmacyRepository.findById(s.getPharmacyId()).isPresent() ? pharmacyRepository.findById(s.getPharmacyId()).get().getName() : "n/a");
            stockDTOList.add(current);
        });

        return stockDTOList;
    }

    public Pharmacies findPharmacyById(Long id) {
        Stock s = null;
        if (stockRepository.findById(id).isPresent()) {
            s = stockRepository.findById(id).get();
            return pharmacyRepository.findById(s.getPharmacyId()).isPresent() ? pharmacyRepository.findById(s.getPharmacyId()).get() : null;
        }
        return null;
    }

    public Trademarks findFirstTmByName(String tmName) {
        return trademarkRepository.searchByName(tmName).get(0);
    }

    public List<String> findTmNamesLikeInput(String tmName) {
        List<String> names = new ArrayList<>();
        trademarkRepository.searchByName(tmName).forEach(e -> {
            names.add(e.getName());
        });
        return names;
    }

    public Drugs findOneDrugById(Long id) {
        return drugRepository.findById(id).get();
    }

    public void purchase(StockDTO stockDTO) throws StockServicePurchasingException {
        Stock s = new Stock()
                .setPharmacyId(stockDTO.getPharmacyId())
                .setTrademarkId(trademarkRepository.searchByName(stockDTO.getTrademarkName()).get(0).getId())
                .setAvailability(stockDTO.getAvailability())
                .setId(stockRepository.count() + 1);
        List<Stock> currentStock = stockRepository.findAllByPharmacyId(s.getPharmacyId());
        List<String> trademarks = new ArrayList<>();
        currentStock.forEach(current -> {
            if (current.getAvailability() != null)
                if (current.getAvailability() > 0)
                    trademarks.add(trademarkRepository.findById(current.getTrademarkId()).get().getName());
        });
        if (trademarks.contains(trademarkRepository.findById(s.getTrademarkId()).get().getName()))
            throw new StockServicePurchasingException("Already exists");
        else {
            /*
             * Замена на процедуру
             * */
            stockRepository.addToStock(s.getPharmacyId().intValue(), s.getTrademarkId().intValue(), s.getAvailability().intValue());

//            stockRepository.save(s);
        }
    }

    public void sell(StockDTO stockDTO, Long amount) {
        List<Stock> currentStock = stockRepository.findAllByPharmacyId(stockDTO.getPharmacyId());
        currentStock.forEach(st -> {
            if (trademarkRepository.findById(st.getTrademarkId()).get().getName().equals(stockDTO.getTrademarkName())) {
                st.setAvailability(st.getAvailability() - amount);
                stockRepository.save(st);
            }
        });
    }

    public void buy(StockDTO stockDTO, Long amount) {
        List<Stock> currentStock = stockRepository.findAllByPharmacyId(stockDTO.getPharmacyId());
        currentStock.forEach(st -> {
            if (trademarkRepository.findById(st.getTrademarkId()).get().getName().equals(stockDTO.getTrademarkName())) {
                st.setAvailability(st.getAvailability() + amount);
                stockRepository.save(st);
            }
        });
    }

    public String validate(String tmName) {
        if (trademarkRepository.searchByName(tmName).isEmpty())
            return String.format("'%s' is not available", tmName);
        return null;
    }

    public String validate(Long amount) {
        if (amount < 1)
            return "Amount should be more or equal than 1.";
        return null;
    }

    public String validateAmountToSell(Long amount, Long available) {
        if (amount < 1)
            return "Amount should be more or equal than 1.";
        if (amount > available)
            return "Can't sell that much";
        return null;
    }

    public static class StockServicePurchasingException extends Exception {
        public StockServicePurchasingException(String message) {
            super(message);
        }
    }
}
