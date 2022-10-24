package com.example.demo.controller;


import com.example.demo.dao.SushiOrderRepo;
import com.example.demo.model.SushiOrder;
import com.example.demo.response.ResponseHandler;
import com.example.demo.service.SushiOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SpringBootApplication
public class SushiOrderController {
    @Autowired
    private final SushiOrderService sushiOrderService;

    public SushiOrderController(SushiOrderService sushiOrderService) {
        this.sushiOrderService = sushiOrderService;
    }
    @PostMapping(value = "/api/orders")
    @ResponseBody
    public ResponseEntity addSushiOrder(@RequestBody SushiOrder sushiOrder){
        sushiOrderService.addSushiOrder(sushiOrder);
        return ResponseHandler.generatePostResponse("Order Created",
                HttpStatus.CREATED, sushiOrder
        );
    }
    @GetMapping(value = "/api/orders/status")
    @ResponseBody
    public ResponseEntity getSushiOrders(){
        List<List<SushiOrder>> r1 = sushiOrderService.displaySushiOrders();
        return ResponseHandler.generateGetResponse(
                HttpStatus.OK, r1
                );
    }
    @DeleteMapping(value = "/api/orders/{order_id}")
    @ResponseBody
    public ResponseEntity deleteSushiOrders(@PathVariable(value = "order_id", required = true) int orderId){
        int deleteResponseCode = sushiOrderService.deleteSushiOrders(orderId);
        return ResponseHandler.generateDeleteResponse(deleteResponseCode);

    }

    @PutMapping(value = "/api/orders/{order_id}/pause")
    @ResponseBody
    public ResponseEntity pauseSushiOrders(@PathVariable(value = "order_id", required = true) int orderId){
        int pauseResponseCode = sushiOrderService.pauseSushiOrders(orderId);
        return ResponseHandler.generatePauseResponse(pauseResponseCode);

    }

}


