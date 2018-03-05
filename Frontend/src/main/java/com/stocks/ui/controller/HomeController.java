package com.stocks.ui.controller;

import com.stocks.ui.dal.Stock;
import com.stocks.ui.model.StockDal;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * Starting page of the Stocks Admin application.
 */
@Controller
public class HomeController {

    @Autowired
    StockDal stockDal;

    @RequestMapping("/")
    public String getHomePage(@RequestParam(name = "page", defaultValue = "1") final int pageNumber,
                              Model model) throws IOException, ParseException {
        final List<Stock> stocks = stockDal.getStocks(pageNumber);

        model.addAttribute("stocks", stocks);
        model.addAttribute("page", pageNumber);

        return "index";
    }
}
