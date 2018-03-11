package com.stocks.api.dal;

import com.stocks.api.model.Stock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A helper class that can generate a list of {@link Stock}s which would be used during
 * application start by {@link InMemoryStockDal}.
 */
public class StockInfoLoader {

    private final String filePath;

    public StockInfoLoader(final String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads from a file and creates a map of stock.
     *
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Map<String, Stock> readFromFile() throws IOException, ParseException {
        final Map<String, Stock> stocks = new ConcurrentHashMap<>();  // So that order is preserved.

        final ClassLoader classLoader = getClass().getClassLoader();
        final JSONParser parser = new JSONParser();
        final String file = classLoader.getResource(filePath).getFile();
        final Object obj = parser.parse(new FileReader(file));
        final JSONArray jsonArray = (JSONArray) obj;

        jsonArray.forEach(o -> {
            final JSONObject json = (JSONObject) o;
            final String id = (String) json.get("id");
            final String symbol = (String) json.get("symbol");
            final Double price = (Double) json.get("currentPrice");
            final String name = (String) json.get("name");

            stocks.put(id, new Stock()
                    .withId(id)
                    .withSymbol(symbol)
                    .withName(name)
                    .withPrice(price)
                    .withLastUpdateDate(new Date()));
        });

        return stocks;
    }
}
