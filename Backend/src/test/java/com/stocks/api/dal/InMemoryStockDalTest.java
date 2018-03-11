package com.stocks.api.dal;

import com.stocks.api.model.Stock;
import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link InMemoryStockDal}.
 */
public class InMemoryStockDalTest {
    private InMemoryStockDal stockDal;

    @Before
    public void setUp() throws IOException, ParseException {
        final StockInfoLoader infoLoader = new StockInfoLoader("data/info.json");
        stockDal = new InMemoryStockDal(infoLoader, 5);
    }

    @Test
    public void testGetById() {
        final Stock stock = stockDal.getById("1");

        assertEquals("Apple Inc", stock.getName());
        assertEquals("AAPL", stock.getSymbol());
        assertEquals(new Double(904.72), stock.getPrice());
        assertEquals("1", stock.getId());
    }

    @Test
    public void testNonExistentStock() {
        TestCase.assertNull(stockDal.getById("non existenet id"));
        TestCase.assertNull(stockDal.getBySymbol("non existent symbol"));
    }

    @Test
    public void testGetBySymbol() {
        final Stock stock = stockDal.getBySymbol("AAPL");

        assertEquals("Apple Inc", stock.getName());
        assertEquals("AAPL", stock.getSymbol());
        assertEquals(new Double(904.72), stock.getPrice());
        assertEquals("1", stock.getId());
    }

    @Test
    public void testGetAPage() {
        final JSONObject stockList = stockDal.getStocks(1);
        final List stocks = (List) stockList.get("items");
        assertEquals(5, stocks.size());

        assertEquals(3, stockList.get("maxPage"));
        assertEquals(1, stockList.get("page"));
    }

    @Test
    public void testPutStock() {
        final String id = UUID.randomUUID().toString();
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol);

        stockDal.putStock(stock);

        final Stock savedStock = stockDal.getById(id);

        assertEquals(id, savedStock.getId());
        assertEquals(name, savedStock.getName());
        assertEquals(price, savedStock.getPrice());
        assertEquals(symbol, savedStock.getSymbol());
        assertNotNull(stock.getLastUpdateDate());
    }
}
