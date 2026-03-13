package com.example.demo.repository;

import com.example.demo.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findByType(String type);

    List<StockTransaction> findByMaterialMaterialId(Long materialId);

    List<StockTransaction> findByWarehouseWarehouseId(Long warehouseId);

    List<StockTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
