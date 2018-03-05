package com.stocks.ui.model;

import com.stocks.ui.dal.Stock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockDal {
    private static final String USER_AGENT = "STOCK_FRONTEND";
    private final String baseUrl;
    private final CloseableHttpClient client;

    @Autowired
    public StockDal(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClientBuilder.create().build();
    }

    public Stock getStock(final String id) throws IOException {
        final String url = baseUrl + "api/stock/" + id;

        RestTemplate restTemplate = new RestTemplate();
        JSONObject result = restTemplate.getForObject(url, JSONObject.class);

        System.out.println(result.toJSONString());
        return Stock.fromJson(result);
    }

    public List<Stock> getStocks(final int pageNumber) throws ParseException {
        final String url = baseUrl + "api/stocks";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        JSONArray obj = (JSONArray) new JSONParser().parse(result);
        final List<Stock> stocks = new ArrayList<>();

        for (int i = 0; i < obj.size(); i++) {

            JSONObject o = (JSONObject) obj.get(i);

            final String id = (String) o.get("id");
            final Long instant = (Long) o.get("lastUpdateDate");
            final Double price = (Double) o.get("price");
            final String name = (String) o.get("name");
            final String symbol = (String) o.get("symbol");

            stocks.add(Stock.builder()
                    .id(id)
                    .symbol(symbol)
                    .lastUpdateDate(new Date(instant))
                    .price(price)
                    .name(name)
                    .build());
        }

        return stocks;
    }
}
