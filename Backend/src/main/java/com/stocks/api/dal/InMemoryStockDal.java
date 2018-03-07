package com.stocks.api.dal;

import com.stocks.api.manipulator.StockManager;
import com.stocks.api.model.Stock;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An in-memory implementation of {@link StockDal}. It generates the initial
 * list from a file with the help of {@link StockInfoLoader}.
 */
public class InMemoryStockDal implements StockDal {
    private static Logger logger = LoggerFactory.getLogger(InMemoryStockDal.class);

    private final Map<String, Stock> stocks;

    private final int pageSize;

    @Autowired
    public InMemoryStockDal(final StockInfoLoader stockInfoLoader, final int pageSize) throws IOException, ParseException {
        this.stocks = stockInfoLoader.readFromFile();

        this.pageSize = pageSize;
    }

    @Override
    public JSONObject getStocks(final int pageNumber) {
        List<Stock> currentPage = stocks.values().stream()
                .skip((pageNumber-1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        final JSONObject object = new JSONObject();
        object.put("page", pageNumber);
        object.put("items", currentPage);
        object.put("maxPage", (stocks.size() / pageSize) + 1);
        return object;
    }

    @Override
    public Stock getById(final String id) {
        return stocks.get(id);
    }

    @Override
    public Stock getBySymbol(final String symbol) {
        List<Stock> matchingStocks = stocks.values()
                .stream()
                .filter(s-> s.getSymbol().equals(symbol))
                .collect(Collectors.toList());

        if(matchingStocks.size() > 0) return matchingStocks.get(0);
        return null;
    }

    @Override
    public void putStock(final Stock stock) {
        stocks.put(stock.getId(), stock);
    }
}
