package com.stocks.api.manipulator;

import com.stocks.api.dal.StockDal;
import com.stocks.api.exceptions.ResourceAlreadyExistsException;
import com.stocks.api.model.Stock;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class StockManagerTest {
    @MockBean StockDal mockStockDal;
    @Captor ArgumentCaptor<Stock> stockArgCaptor;

    private StockManager stockManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        stockManager = new StockManager(mockStockDal);
    }

    @Test
    public void testCreate() {
        final String id = UUID.randomUUID().toString();
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        stockManager.create(stock);

        verify(mockStockDal, times(1)).putStock(stockArgCaptor.capture());
        final Stock createdStock = stockArgCaptor.getValue();

        assertNotEquals(id, createdStock.getId());
        assertFalse(lastUpdate == createdStock.getLastUpdateDate());

        assertEquals(name, createdStock.getName());
        assertEquals(price, createdStock.getPrice());
        assertEquals(symbol, createdStock.getSymbol());
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testCreateAlreadyPresent() {
        final String id = UUID.randomUUID().toString();
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        when(mockStockDal.getBySymbol(symbol)).thenReturn(new Stock().withId(id));

        final Stock createdStock = stockManager.create(stock);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNoName() {
        final String id = UUID.randomUUID().toString();
        final String name = null;
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        Stock createdStock = stockManager.create(stock);
        createdStock.equals(stock);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNoSymbol() {
        final String id = UUID.randomUUID().toString();
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = null;
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withLastUpdateDate(lastUpdate);

        Stock createdStock = stockManager.create(stock);
        createdStock.equals(stock);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNoPrice() {
        final String id = UUID.randomUUID().toString();
        final String name = "a stock name";

        final String symbol = "a stock symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        Stock createdStock = stockManager.create(stock);
        createdStock.equals(stock);
    }

    @Test
    public void testUpdate() {
        final String id = UUID.randomUUID().toString();
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        when(mockStockDal.getById(id)).thenReturn(stock);

        final Double newPrice = 22.03;
        stockManager.updatePrice(id, newPrice);

        verify(mockStockDal, times(1)).putStock(stockArgCaptor.capture());
        final Stock updatedStock = stockArgCaptor.getValue();

        assertEquals(id, updatedStock.getId());

        assertEquals(newPrice, updatedStock.getPrice());
    }

    @Test
    public void testGet() {
        final String id = UUID.randomUUID().toString();
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        when(mockStockDal.getById(id)).thenReturn(stock);

        final Stock retrievedStock = stockManager.get(id);

        verify(mockStockDal, times(1)).getById(id);
        assertEquals(id, retrievedStock.getId());
        assertEquals(name, retrievedStock.getName());
        assertEquals(price, retrievedStock.getPrice());
        assertEquals(symbol, retrievedStock.getSymbol());
        assertEquals(lastUpdate, retrievedStock.getLastUpdateDate());
    }

    @Test
    public void testGetByPage() {
        when(mockStockDal.getStocks(anyInt())).thenReturn(new JSONObject());

        stockManager.get(1);

        verify(mockStockDal, times(1)).getStocks(1);
    }
}
