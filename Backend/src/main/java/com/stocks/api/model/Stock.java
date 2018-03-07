package com.stocks.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * A model to represent a Stock object.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private Double price;

    @Getter
    @Setter
    private Date lastUpdateDate;
}
