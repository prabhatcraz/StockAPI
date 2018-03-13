package com.stocks.api.dal;

import com.stocks.api.model.Stock;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link StockInfoLoader}
 */
public class StockInfoLoaderTest {

    @Test
    public void testReadFromFile() throws IOException, ParseException {
        final StockInfoLoader stockInfoLoader = new StockInfoLoader("/data/info.json");
        Map<String, Stock> stockList =stockInfoLoader.readFromFile();

        assertEquals(12, stockList.keySet().size());
    }

}
