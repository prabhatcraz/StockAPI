package com.stocks.api.dal;

import com.stocks.api.model.Stock;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An in-memory implementation of {@link StockDal}. It generates the initial
 * list from a file with the help of {@link StockInfoLoader}.
 */
public class InMemoryStockDal implements StockDal {
    private final Map<String, Stock> stocks;

    private final int pageSize;

    @Autowired
    public InMemoryStockDal(StockInfoLoader stockInfoLoader, int pageSize) throws IOException, ParseException {
        this.stocks = stockInfoLoader.readFromFile();


        for (Stock stock : this.stocks.values()) {
            System.out.println(stock.getSymbol());
        }
        this.pageSize = pageSize;
    }

    @Override
    public List<Stock> getStocks(final int pageNumber) {
        return stocks.values().stream()
                .skip((pageNumber-1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public Stock getStock(final String id) {
        return stocks.get(id);
    }

    @Override
    public void putStock(final Stock stock) {
        stocks.put(stock.getId(), stock);
    }
}
