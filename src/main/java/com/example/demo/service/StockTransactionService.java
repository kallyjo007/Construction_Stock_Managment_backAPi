package com.example.demo.service;

import com.example.demo.model.StockTransaction;
import com.example.demo.repository.StockTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockTransactionService {

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    public StockTransaction addNewStockTransaction(StockTransaction stockTransaction) {
        return stockTransactionRepository.save(stockTransaction);
    }

    public List<StockTransaction> getAllStockTransactions() {
        return stockTransactionRepository.findAll();
    }

    public StockTransaction getStockTransactionById(Long id) {
        return stockTransactionRepository.findById(id).orElseThrow(() -> new RuntimeException("StockTransaction not found with id: " + id));
    }

    public StockTransaction updateStockTransaction(Long id, StockTransaction stockTransaction) {
        StockTransaction existingTransaction = getStockTransactionById(id);
        existingTransaction.setType(stockTransaction.getType());
        existingTransaction.setQuantity(stockTransaction.getQuantity());
        existingTransaction.setReference(stockTransaction.getReference());
        existingTransaction.setTransactionDate(stockTransaction.getTransactionDate());
        existingTransaction.setMaterial(stockTransaction.getMaterial());
        existingTransaction.setWarehouse(stockTransaction.getWarehouse());
        existingTransaction.setProjectSite(stockTransaction.getProjectSite());
        existingTransaction.setUser(stockTransaction.getUser());
        return stockTransactionRepository.save(existingTransaction);
    }

    public void deleteStockTransaction(Long id) {
        StockTransaction existingTransaction = getStockTransactionById(id);
        stockTransactionRepository.delete(existingTransaction);
    }

    public List<StockTransaction> findByType(String type) {
        List<StockTransaction> transactions = stockTransactionRepository.findByType(type);
        if (transactions != null && !transactions.isEmpty()) {
            return transactions;
        } else {
            return null;
        }
    }

    public List<StockTransaction> findByMaterial(Long materialId) {
        List<StockTransaction> transactions = stockTransactionRepository.findByMaterialMaterialId(materialId);
        if (transactions != null && !transactions.isEmpty()) {
            return transactions;
        } else {
            return null;
        }
    }

    public List<StockTransaction> findByWarehouse(Long warehouseId) {
        List<StockTransaction> transactions = stockTransactionRepository.findByWarehouseWarehouseId(warehouseId);
        if (transactions != null && !transactions.isEmpty()) {
            return transactions;
        } else {
            return null;
        }
    }

    public List<StockTransaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<StockTransaction> transactions = stockTransactionRepository.findByTransactionDateBetween(startDate, endDate);
        if (transactions != null && !transactions.isEmpty()) {
            return transactions;
        } else {
            return null;
        }
    }
}
