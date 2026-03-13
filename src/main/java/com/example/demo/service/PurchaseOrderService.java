package com.example.demo.service;

import com.example.demo.model.PurchaseOrder;
import com.example.demo.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrder addNewPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("PurchaseOrder not found with id: " + id));
    }

    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder purchaseOrder) {
        PurchaseOrder existingPurchaseOrder = getPurchaseOrderById(id);
        existingPurchaseOrder.setOrderDate(purchaseOrder.getOrderDate());
        existingPurchaseOrder.setStatus(purchaseOrder.getStatus());
        existingPurchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount());
        existingPurchaseOrder.setSupplier(purchaseOrder.getSupplier());
        existingPurchaseOrder.setCreatedBy(purchaseOrder.getCreatedBy());
        return purchaseOrderRepository.save(existingPurchaseOrder);
    }

    public void deletePurchaseOrder(Long id) {
        PurchaseOrder existingPurchaseOrder = getPurchaseOrderById(id);
        purchaseOrderRepository.delete(existingPurchaseOrder);
    }

    public List<PurchaseOrder> findByStatus(String status) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByStatus(status);
        if (purchaseOrders != null && !purchaseOrders.isEmpty()) {
            return purchaseOrders;
        } else {
            return null;
        }
    }

    public List<PurchaseOrder> findByOrderDate(LocalDate orderDate) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByOrderDate(orderDate);
        if (purchaseOrders != null && !purchaseOrders.isEmpty()) {
            return purchaseOrders;
        } else {
            return null;
        }
    }

    public List<PurchaseOrder> findBySupplier(Long supplierId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findBySupplierSupplierId(supplierId);
        if (purchaseOrders != null && !purchaseOrders.isEmpty()) {
            return purchaseOrders;
        } else {
            return null;
        }
    }
}
