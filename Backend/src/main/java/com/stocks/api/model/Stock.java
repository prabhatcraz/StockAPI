package com.stocks.api.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/**
 * A model to represent a Stock object.
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
        this.lastUpdateDate = new Date();
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Constructor required by Spring
     */
    public Stock() {
        this.lastUpdateDate = new Date();
    }
}
