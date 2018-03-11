package com.stocks.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocks.api.exceptions.ResourceAlreadyExistsException;
import com.stocks.api.manipulator.StockManager;
import com.stocks.api.model.ErrorResponse;
import com.stocks.api.model.Stock;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests {@link StocksAdminController}.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(StocksAdminController.class)
public class StocksAdminControllerTest {

//    @Autowired
    private MockMvc mockMvc;

    @MockBean
    StockManager stockManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new StocksAdminController(stockManager))
                .setControllerAdvice(new ApiErrorHandler())
                .build();

    }

    @Test
    public void testGetStock() throws Exception {
        mockMvc.perform(get("/api/stocks/1")).andExpect(status().isOk());
    }

    @Test
    public void testGetStocks() throws Exception {
        mockMvc.perform(get("/api/stocks")).andExpect(status().isOk());
    }

    @Test
    public void testPutStock() throws Exception {
        final Stock stock = new Stock().withPrice(1.23);
        when(stockManager.update(any(), any())).thenReturn(stock);

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(requestBody))
        ;

        verify(stockManager, times(1)).update(any(), any());
    }

    @Test
    public void testPostStock() throws Exception {
        final Stock stock = new Stock().withPrice(1.23);
        when(stockManager.update(any(), any())).thenReturn(stock);

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(200))
        ;


        verify(stockManager, times(1)).create(any());
    }

    @Test
    public void testPutStock404() throws Exception {
        final Stock stock = new Stock().withPrice(1.23);
        when(stockManager.update(any(), any())).thenReturn(stock);

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(put("/api/stock/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(404))
        ;
    }

    @Test
    public void testPutStock500() throws Exception {
        final Stock stock = new Stock();

        final String errorMessage = "Something is wrong";

        given(stockManager.update(any(), any())).willThrow(new RuntimeException(errorMessage));

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(500))
                .andExpect(result -> {
                        String content = result.getResponse().getContentAsString();
                            ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                            assertEquals(errorResponse.getErroMessage(), errorMessage);
                            assertEquals(errorResponse.getErrorCode(), "500");
                        }
                    )
        ;

        verify(stockManager, times(1)).update(any(), any());
    }

    @Test
    public void testPostStock405() throws Exception {
        final Stock stock = new Stock().withPrice(1.23);

        final String errorMessage = "a post error message";

        given(stockManager.create(any())).willThrow(new ResourceAlreadyExistsException(errorMessage));

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(405))
        ;

        verifyNoMoreInteractions(stockManager);
    }
}
