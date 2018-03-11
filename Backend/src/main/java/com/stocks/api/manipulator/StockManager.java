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
 * Manages operations related to stocks.
 */
public class StockManager {
    private static Logger logger = LoggerFactory.getLogger(StockManager.class);

    private final StockDal stockDal;

    @Autowired
    public StockManager(final StockDal stockDal) {
        this.stockDal = stockDal;
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

        // Id is an internal property and should be generated within the system.
        final Stock stockToCreate = stock
                .withId(UUID.randomUUID().toString())
                .withLastUpdateDate(new Date());

        stockDal.putStock(stockToCreate);
        return stockToCreate;
    }

    /**
     * Gets a {@link Stock} by its id.
     * @param id
     * @return
     */
    public Stock get(final String id) {
        final Stock stock = stockDal.getById(id);
        final String message = String.format("No stock found with id %s", id);
        return Optional
                .of(stock)
                .orElseThrow(()-> new ResourceNotFoundException(message));
    }

    /**
     * Updates the price of a single {@link Stock}.
     * @param id
     * @param price
     */
    public void updatePrice(final String id, final Double price) {
        final Stock stock = stockDal.getById(id);

        final String message = String.format("No stock found with id %s", id);
        Optional.of(stock).orElseThrow(()-> new ResourceNotFoundException(message));

        stockDal.putStock(stock.withPrice(price));
    }

    /**
     * Gets a {@link JSONObject} with information for the page.
     * {
     *      "page": 2,
     *      "items": <<list of stocks>>,
     *      "maxPage": 4
     * }
     * @param pageNumber
     * @return
     */
    public JSONObject get(final int pageNumber) {
        return stockDal.getStocks(pageNumber);
    }

    private boolean isAlreadyPresent(final String symbol) {
        Stock s = stockDal.getBySymbol(symbol);
        return s != null;
    }
}
