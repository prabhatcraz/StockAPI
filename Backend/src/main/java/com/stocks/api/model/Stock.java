package com.stocks.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.Date;

/**
 * A model to represent a Stock object.
 */
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Getter private String id;

    @Getter private String name;

    @Getter private String symbol;

    @Getter private Double price;

    @Getter private Date lastUpdateDate;
}
