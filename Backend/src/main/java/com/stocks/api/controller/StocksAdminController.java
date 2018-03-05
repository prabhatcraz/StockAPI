package com.stocks.api.controller;

import com.stocks.api.dal.StockDal;
import com.stocks.api.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A {@link org.springframework.stereotype.Controller} that handles the basic requests of getting
 * stock information, creating new stocks and updating the existing ones.
 */
@RestController
public class StocksAdminController {

    private static Logger logger = LoggerFactory.getLogger(StocksAdminController.class);
    private final StockDal stockDal;

    @Autowired
    public StocksAdminController(StockDal stockDal) {
        this.stockDal = stockDal;
    }

    @RequestMapping(value = "/api/stock/{stockId}", method = RequestMethod.GET)
    public Stock get(@PathVariable final String stockId) {
        logger.debug(stockId);
        return stockDal.getStock(stockId);
    }

    @RequestMapping(value = "/api/stock/{stockId}", method = RequestMethod.PUT)
    public Stock update(@PathVariable final String stockId, @RequestBody Stock stock) {
        logger.debug(String.format("Creating stock %s", stockId));
        stockDal.putStock(stock);
        return stock;
    }

    @RequestMapping(value = "/api/stock/{stockId}", method = RequestMethod.POST)
    public Stock create(@PathVariable final String stockId, @RequestBody Stock stock) {
        logger.debug(String.format("Creating stock %s", stockId));
        stockDal.putStock(stock);
        return stock;
    }

    @RequestMapping(value = "/api/stocks", method = RequestMethod.GET)
    public List<Stock> getAll(@RequestParam(name = "page", defaultValue = "1") final int pageNumber) {
        if(pageNumber < 1) throw new IllegalArgumentException("Page number can not be less than 1");
        return stockDal.getStocks(pageNumber);
    }
}
