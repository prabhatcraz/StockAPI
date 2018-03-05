package com.stocks.ui.dal;

import com.stocks.ui.model.StockDal;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StockDalTest {
    @Test
    public void testGetStock() throws IOException {
        StockDal stockDal = new StockDal("http://localhost:8080/");

        stockDal.getStock("1");
    }

    @Test
    public void testGetStocks() throws IOException, ParseException {
        final StockDal stockDal = new StockDal("http://localhost:8080/");

        final List<Stock> stocks = stockDal.getStocks(null);

        assertEquals(10, stocks.size());
    }
}
