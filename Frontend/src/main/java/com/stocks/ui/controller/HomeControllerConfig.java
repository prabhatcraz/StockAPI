package com.stocks.ui.controller;

import com.stocks.ui.model.StockDal;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HomeControllerConfig {
    @Bean
    public StockDal getStockDal() throws IOException, ParseException {
        return new StockDal("http://localhost:8080/");
    }
}
