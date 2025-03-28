package kz.nkoldassov.stocktrading.service.impl;

import kz.nkoldassov.stocktrading.model.db.StockBuyOrderQueue;
import kz.nkoldassov.stocktrading.model.db.StockSellOrderQueue;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToBuyDto;
import kz.nkoldassov.stocktrading.model.dto.StockOrderToSellDto;
import kz.nkoldassov.stocktrading.repository.StockBuyOrderQueueRepository;
import kz.nkoldassov.stocktrading.repository.StockSellOrderQueueRepository;
import kz.nkoldassov.stocktrading.service.StockOrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StockOrderServiceImpl implements StockOrderService {

    private final StockBuyOrderQueueRepository stockBuyOrderQueueRepository;
    private final StockSellOrderQueueRepository stockSellOrderQueueRepository;

    public StockOrderServiceImpl(StockBuyOrderQueueRepository stockBuyOrderQueueRepository,
                                 StockSellOrderQueueRepository stockSellOrderQueueRepository) {
        this.stockBuyOrderQueueRepository = stockBuyOrderQueueRepository;
        this.stockSellOrderQueueRepository = stockSellOrderQueueRepository;
    }

    @Override
    public void saveBuyOrderToQueue(StockOrderToBuyDto stockToBuy, Long stockId) {

        List<StockBuyOrderQueue> buyOrderQueueList = IntStream.range(0, stockToBuy.quantity())
                .mapToObj(i -> new StockBuyOrderQueue(
                        null,
                        stockToBuy.userId(),
                        stockToBuy.price(),
                        stockId,
                        null,
                        null,
                        null,
                        null
                )).collect(Collectors.toList());

        stockBuyOrderQueueRepository.insertAll(buyOrderQueueList);

    }

    @Override
    public void saveSellOrderToQueue(StockOrderToSellDto stockToSell) {

        List<StockSellOrderQueue> sellOrderQueueList = stockToSell.stockOrderDetail()
                .stream()
                .map(detail -> new StockSellOrderQueue(
                        null,
                        stockToSell.userId(),
                        detail.userStockId(),
                        detail.price(),
                        null,
                        null,
                        null,
                        null
                )).toList();

        stockSellOrderQueueRepository.insertAll(sellOrderQueueList);

    }

    @Override
    public List<StockBuyOrderQueue> loadAndOccupyBuyOrdersByLimit(int limit) {
        return stockBuyOrderQueueRepository.findAndOccupyByLimit(limit);
    }

    @Override
    public Optional<StockSellOrderQueue> loadAndOccupySellOrderByPriceAndStockId(BigDecimal price, Long stockId) {
        return stockSellOrderQueueRepository.findAndOccupyByPriceAndStockId(price, stockId);
    }

    @Override
    public void unOccupyBuyOrder(StockBuyOrderQueue buyOrder) {
        stockBuyOrderQueueRepository.updateOccupied(buyOrder.id(), null, null);
    }

}