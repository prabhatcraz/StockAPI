package com.stocks.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocks.api.exceptions.ResourceAlreadyExistsException;
import com.stocks.api.exceptions.ResourceNotFoundException;
import com.stocks.api.manipulator.StockManager;
import com.stocks.api.model.ErrorResponse;
import com.stocks.api.model.Stock;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests {@link StocksAdminController}.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(StocksAdminController.class)
public class StocksAdminControllerTest {
    private MockMvc mockMvc;

    @MockBean StockManager stockManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new StocksAdminController(stockManager))
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    // HAPPY CASES
    @Test
    public void testGetStock() throws Exception {
        mockMvc.perform(get("/api/stocks/1"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void testGetStocks() throws Exception {
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void testPutStock() throws Exception {
        final Double price = 1.23;

        final String requestBody = new ObjectMapper().writeValueAsString(price);
        mockMvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
        ;

        verify(stockManager, times(1)).updatePrice(anyString(), any(Double.class));
    }

    @Test
    public void testPostStock() throws Exception {
        final Stock stock = new Stock().withPrice(1.23);
        when(stockManager.create(any())).thenReturn(stock);

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(200))
        ;


        verify(stockManager, times(1)).create(any());
    }

    @Test
    public void testGetAll() throws Exception {
        given(stockManager.get(any(int.class))).willReturn(new JSONObject());

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/stocks?page=2"))
                .andExpect(status().is(200));

    }

    // HTTP ERROR CODES and ERROR MESSAGES

    @Test
    public void testGetWithException() throws Exception {
        final String errorMessage = "Something is wrong";

        doThrow(new RuntimeException(errorMessage)).when(stockManager).get(any());

        mockMvc.perform(get("/api/stocks/1"))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;
    }

    @Test
    public void testGetAllWithException() throws Exception {
        final String errorMessage = "Something is wrong";

        doThrow(new RuntimeException(errorMessage)).when(stockManager).get(anyInt());

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;
    }

    @Test
    public void testPutStockWithException() throws Exception {
        final Stock stock = new Stock();

        final String errorMessage = "Something is wrong";

        doThrow(new RuntimeException(errorMessage)).when(stockManager).updatePrice(any(), any());

        final String requestBody = new ObjectMapper().writeValueAsString(new Double(1.3));
        mockMvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(500))
                .andExpect(result -> {
                        String content = result.getResponse().getContentAsString();
                        ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                        assertEquals(errorResponse.getErroMessage(), errorMessage);
                        assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;

        verify(stockManager, times(1)).updatePrice(any(), any());
    }

    @Test
    public void testPostStockWithException() throws Exception {
        final Stock stock = new Stock().withPrice(2.3).withSymbol("a symbol").withName("a name");

        final String errorMessage = "Something is wrong";

        when(stockManager.create(any())).thenThrow(new RuntimeException(errorMessage));

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;

        verify(stockManager, times(1)).create(any());
    }

    @Test
    public void testPostStockWrongUrl() throws Exception {
        final Stock stock = new Stock().withPrice(1.23);

        final String errorMessage = "a post error message";

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(405))
        ;

        verifyNoMoreInteractions(stockManager);
    }

    @Test
    public void testCreatingExistingStock() throws Exception {
        final String errorMessage= "It already exists";
        when(stockManager.create(any())).thenThrow(new ResourceAlreadyExistsException(errorMessage));

        final Stock stock = new Stock().withPrice(2.3).withSymbol("a symbol").withName("a name");

        final String requestBody = new ObjectMapper().writeValueAsString(stock);

        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(409))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "409");
                })
        ;
    }

    @Test
    public void testUpdateNonExistingStock() throws Exception {
        final String errorMessage= "It does not exists";
        doThrow(new ResourceNotFoundException(errorMessage)).when(stockManager).updatePrice(any(), any());

        final String requestBody = new ObjectMapper().writeValueAsString(new Double(1.2));

        mockMvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(404))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "404");
                })
        ;
    }


    @Test
    public void testGetAllWithNegativePageNumber() throws Exception {
        given(stockManager.get(any(int.class))).willReturn(new JSONObject());

        mockMvc.perform(get("/api/stocks?page=-2"))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), "Page number can not be less than 1");
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;
    }
}
