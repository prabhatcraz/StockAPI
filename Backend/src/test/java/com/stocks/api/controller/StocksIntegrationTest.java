package com.stocks.api.controller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for stock APIs
 */
public class StocksIntegrationTest {
    private final String URL = "http://localhost:8080/api/stocks/";

    @Test
    public void testPutAndGetSingleStock() throws IOException, ParseException {
        final CloseableHttpClient client = HttpClients.createDefault();

        final String name = UUID.randomUUID().toString();
        final String symbol = UUID.randomUUID().toString();
        final Double price = new Double(123.45);

        // CREATING

        final JSONObject payload = new JSONObject();
        payload.put("name", name);
        payload.put("price", price);
        payload.put("symbol", symbol);

        final HttpPost post = new HttpPost(URL);
        final StringEntity entity = new StringEntity(payload.toJSONString());
        entity.setContentType("application/json");
        post.setEntity(entity);
        final CloseableHttpResponse postResponse = client.execute(post);

        assertEquals(200, postResponse.getStatusLine().getStatusCode());
        final String stockId = (String) translateResponseToJson(postResponse).get("id");

        // GETTING
        final HttpGet request = new HttpGet(URL + stockId);
        final CloseableHttpResponse getResponse = client.execute(request);

        final JSONObject getResponseObj = translateResponseToJson(getResponse);

        assertEquals(200, getResponse.getStatusLine().getStatusCode());
        assertEquals(stockId, getResponseObj.get("id"));
        assertEquals(price, getResponseObj.get("price"));

        // UPDATING
        final HttpPut put = new HttpPut(URL + stockId);
        final Double newPrice = new Double(321.54);
        put.setHeader("Accept", "application/json");

        put.setEntity(new StringEntity(String.valueOf(newPrice)));

        final CloseableHttpResponse putResponse = client.execute(put);

        assertEquals(200, putResponse.getStatusLine().getStatusCode());

        // GETTING AGAIN
        final CloseableHttpResponse updatedResponse = client.execute(request);

        final JSONObject updateStock = translateResponseToJson(updatedResponse);

        assertEquals(200, updatedResponse.getStatusLine().getStatusCode());
        assertEquals(stockId, updateStock.get("id"));
        assertEquals(newPrice, updateStock.get("price"));
    }

    private JSONObject translateResponseToJson(final CloseableHttpResponse response) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
        String output = "";

        while (true){
            String s = br.readLine();
            if(s==null) break;
            output += s;
        }

        return (JSONObject) new JSONParser().parse(output);
    }
}
