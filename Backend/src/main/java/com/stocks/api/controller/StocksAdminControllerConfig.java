package com.stocks.api.controller;

import com.stocks.api.dal.StockDal;
import com.stocks.api.manipulator.StockManager;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Provides dependencies for {@link StocksAdminController}.
 */
@Configuration
public class StocksAdminControllerConfig {

    @Bean
    @Autowired
    public StockManager getStockManager(final StockDal stockDal) throws IOException, ParseException {
        return new StockManager(stockDal);
    }
}
