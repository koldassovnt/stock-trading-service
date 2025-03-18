package kz.nkoldassov.stocktrading.service;

import kz.nkoldassov.stocktrading.model.dto.StockTradeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockTradeServiceImpl implements StockTradeService {

    private static final Logger logger = LoggerFactory.getLogger(StockTradeServiceImpl.class);

    private static final StockTradeServiceImpl instance = new StockTradeServiceImpl();

    private StockTradeServiceImpl() {}

    public static StockTradeServiceImpl instance() {
        return instance;
    }

    @Override
    public void buy(StockTradeDto stockToBuy) {
        logger.info("IzGpOMlO :: buy called");
        //todo
    }

    @Override
    public void sell(StockTradeDto stockToSell) {
        logger.info("iVdAN9V0 :: sell called");
        //todo
    }
}