package com.stocks.api.controller;

import com.stocks.api.dal.InMemoryStockDal;
import com.stocks.api.dal.StockDal;
import com.stocks.api.dal.StockInfoLoader;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Provides dependencies for {@link StocksAdminController}.
 */
@Configuration
public class StocksAdminControllerConfig {
    private final int PAGE_SIZE = 10;

    @Bean
    public StockDal getStockDal() throws IOException, ParseException {
        return new InMemoryStockDal(new StockInfoLoader(), PAGE_SIZE);
    }
}
