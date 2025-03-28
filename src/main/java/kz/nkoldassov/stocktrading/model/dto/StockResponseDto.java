package kz.nkoldassov.stocktrading.model.dto;

import java.util.List;

public record StockResponseDto(List<StockDto> details) {

    public static StockResponseDto of(List<StockDto> details) {
        return new StockResponseDto(details);
    }

}