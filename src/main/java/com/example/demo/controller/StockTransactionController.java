package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.StockTransaction;
import com.example.demo.service.StockTransactionService;

@RestController
@RequestMapping("/api/stock-transactions")
public class StockTransactionController {

    @Autowired
    private StockTransactionService stockTransactionService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewStockTransaction(@RequestBody StockTransaction stockTransaction) {
        try {
            StockTransaction savedTransaction = stockTransactionService.addNewStockTransaction(stockTransaction);
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockTransaction>> getAllStockTransactions() {
        return new ResponseEntity<>(stockTransactionService.getAllStockTransactions(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStockTransactionById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(stockTransactionService.getStockTransactionById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStockTransaction(@PathVariable Long id, @RequestBody StockTransaction stockTransaction) {
        try {
            return new ResponseEntity<>(stockTransactionService.updateStockTransaction(id, stockTransaction), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteStockTransaction(@PathVariable Long id) {
        try {
            stockTransactionService.deleteStockTransaction(id);
            return new ResponseEntity<>("StockTransaction deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/type", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByType(@RequestParam String type) {
        List<StockTransaction> transactions = stockTransactionService.findByType(type);
        if (transactions != null) {
            return new ResponseEntity<>(transactions, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Stock transactions with that type are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/material", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByMaterial(@RequestParam Long materialId) {
        List<StockTransaction> transactions = stockTransactionService.findByMaterial(materialId);
        if (transactions != null) {
            return new ResponseEntity<>(transactions, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Stock transactions for that material are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/warehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByWarehouse(@RequestParam Long warehouseId) {
        List<StockTransaction> transactions = stockTransactionService.findByWarehouse(warehouseId);
        if (transactions != null) {
            return new ResponseEntity<>(transactions, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Stock transactions for that warehouse are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByDateRange(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        List<StockTransaction> transactions = stockTransactionService.findByDateRange(startDate, endDate);
        if (transactions != null) {
            return new ResponseEntity<>(transactions, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Stock transactions in that date range are not found", HttpStatus.NOT_FOUND);
        }
    }
}
