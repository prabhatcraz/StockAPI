package com.stocks.ui.dal;

import lombok.Builder;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.Date;

/**
 * We have another model in Backend module as well. The reason we choose to create a different
 * model here is so that Backend and Frontend remain decoupled. This model contains properties
 * which are directly displayed on UI or at least help create value for a UI element.
 */
@Builder
public class Stock {
    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private String symbol;
    @Getter
    private Double price;
    @Getter
    private Date lastUpdateDate;

    /**
     * Constructor required by lombok.
     */
    public Stock(final String id, final String name, final String symbol, final Double price, final Date lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Takes a {@link JSONObject} and creates a {@link Stock} object.
     *
     * @param o
     * @return
     */
    public static Stock fromJson(final JSONObject o) {
        return Stock.builder()
                .id((String) o.get("id"))
                .name((String) o.get("name"))
                .symbol((String) o.get("symbol"))
                .lastUpdateDate(new Date((String) o.get("lastUpdateDate")))
                .price(Double.parseDouble((String) o.get("price")))
                .build();
    }
}
