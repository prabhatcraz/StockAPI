package com.stocks.api.manipulator;

import com.google.common.base.Preconditions;
import com.stocks.api.dal.StockDal;
import com.stocks.api.exceptions.ResourceAlreadyExistsException;
import com.stocks.api.exceptions.ResourceNotFoundException;
import com.stocks.api.model.Stock;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages stocks.
 */
public class StockManager {
    private static Logger logger = LoggerFactory.getLogger(StockManager.class);

    private final StockDal stockDal;

    @Autowired
    public StockManager(final StockDal stockDal) {
        this.stockDal = stockDal;
    }

    /**
     * Updates the fields of a stock which are supplied in input.
     * @param input
     * @return
     */
    public Stock update(final String id, final Stock input) {
        Stock stock = stockDal.getById(id);

        final String message = String.format("No stock found with id %s", id);
        Optional.of(stock).orElseThrow(()-> new ResourceNotFoundException(message));

        stock = stock.withName(input.getName() == null ? stock.getName() : input.getName());
        stock = stock.withSymbol(input.getSymbol() == null ? stock.getSymbol() : input.getSymbol());
        stock = stock.withPrice(input.getPrice() == null ? stock.getPrice() : input.getPrice());
        stock = stock.withLastUpdateDate(new Date());

        stockDal.putStock(stock);
        return stock;
    }

    public Stock get(final String id) {
        final Stock stock = stockDal.getById(id);
        final String message = String.format("No stock found with id %s", id);
        return Optional.of(stock).orElseThrow(()-> new ResourceNotFoundException(message));
    }

    public JSONObject get(final int pageNumber) {
        return stockDal.getStocks(pageNumber);
    }

    /**
     * Creates a stock if the symbol of the stock does not exist in our database.
     *
     * @param stock
     * @return Stock
     */
    public Stock create(final Stock stock) {
        if(isAlreadyPresent(stock.getSymbol())) {
           final String message = String.format("Stock with symbol %s already exists", stock.getSymbol());
            throw new ResourceAlreadyExistsException(message);
        }

        Preconditions.checkNotNull(stock.getSymbol(), "Symbol of company should not be null");
        Preconditions.checkNotNull(stock.getName(), "Name of company should not be null");
        Preconditions.checkNotNull(stock.getPrice(), "Price of stock should not be null");

        // Id must be generated within the system even if its supplied by the client.
        return new Stock(null, null, null, null, null)
                .withId(UUID.randomUUID().toString())
                .withName(stock.getName())
                .withSymbol(stock.getSymbol())
                .withPrice(stock.getPrice())
                .withLastUpdateDate(new Date());
    }


    private boolean isAlreadyPresent(final String symbol) {
        Stock s = stockDal.getBySymbol(symbol);
        return s != null;
    }
}
