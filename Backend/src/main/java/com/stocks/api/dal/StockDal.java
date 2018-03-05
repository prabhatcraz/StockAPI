package com.stocks.api.dal;


import com.stocks.api.model.Stock;

import java.util.List;

/**
 * An interface to store and retrieve stock data.
 */
public interface StockDal {
    List<Stock> getStocks(int pageNumber);

    Stock getStock(String name);

    void putStock(Stock stock);
}
