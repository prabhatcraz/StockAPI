package com.stocks.api.dal;


import com.stocks.api.model.Stock;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * An interface to store and retrieve stock data into Storage.
 */
public interface StockDal {
    /**
     * Gets a list of stocks from storage in a paginated manner.
     * @param pageNumber
     * @return list of {@link Stock}s.
     */
    JSONObject getStocks(int pageNumber);

    /**
     * Retrieves a {@link Stock} from storage by its ID.
     * @param id
     * @return Stock
     */
    Stock getById(String id);

    /**
     * Retrieves a stock by its Symbol.
     * @param symbol
     * @return
     */
    Stock getBySymbol(String symbol);
    /**
     * Puts a stock in storage.
     * @param stock
     */
    void putStock(Stock stock);
}
