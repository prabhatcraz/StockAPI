package com.stocks.api.dal;

import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Provides bindings for {@link StockDal}.
 */
@Configuration
public class StockDalConfig {
    private final int PAGE_SIZE = 5;
    private static final String DEMO_DATA_FILE = "/data/stocks.json";

    @Bean
    public StockDal getStockDal(final StockInfoLoader infoLoader) throws IOException, ParseException {
        return new InMemoryStockDal(infoLoader, PAGE_SIZE);
    }

    @Bean
    public StockInfoLoader getStockInfoLoader() {
        return new StockInfoLoader(DEMO_DATA_FILE);
    }
}
