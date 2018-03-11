package com.stocks.api.controller;

import com.stocks.api.manipulator.StockManager;
import com.stocks.api.model.Stock;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A {@link org.springframework.stereotype.Controller} that handles the basic requests of getting
 * stock information, creating new stocks and updating the existing ones.
 */
@RestController
public class StocksAdminController {

    private static Logger logger = LoggerFactory.getLogger(StocksAdminController.class);

    private StockManager stockManager;

    @Autowired
    public StocksAdminController(final StockManager stockManager) {
        this.stockManager = stockManager;
    }

    @RequestMapping(value = "/api/stocks/{stockId}", method = RequestMethod.GET)
    public Stock get(@PathVariable final String stockId) {
        return stockManager.get(stockId);
    }

    @RequestMapping(value = "/api/stocks/{stockId}", method = RequestMethod.PUT, headers="Accept=application/json")
    public Stock update(@PathVariable final String stockId, @RequestBody Stock stock) {
        return stockManager.update(stockId, stock);
    }

    @RequestMapping(value = "/api/stocks", method = RequestMethod.POST, headers="Accept=application/json")
    public Stock create(@RequestBody Stock stock) {
        return stockManager.create(stock);
    }

    @RequestMapping(value = "/api/stocks", method = RequestMethod.GET)
    public JSONObject getAll(@RequestParam(name = "page", defaultValue = "1") final int pageNumber) {
        if(pageNumber < 1) throw new IllegalArgumentException("Page number can not be less than 1");
        return stockManager.get(pageNumber);
    }
}
