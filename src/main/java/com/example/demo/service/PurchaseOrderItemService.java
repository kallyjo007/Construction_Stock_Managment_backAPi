package com.example.demo.service;

import com.example.demo.model.PurchaseOrderItem;
import com.example.demo.repository.PurchaseOrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderItemService {

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    public PurchaseOrderItem addNewPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        return purchaseOrderItemRepository.save(purchaseOrderItem);
    }

    public List<PurchaseOrderItem> getAllPurchaseOrderItems() {
        return purchaseOrderItemRepository.findAll();
    }

    public PurchaseOrderItem getPurchaseOrderItemById(Long id) {
        return purchaseOrderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("PurchaseOrderItem not found with id: " + id));
    }

    public PurchaseOrderItem updatePurchaseOrderItem(Long id, PurchaseOrderItem purchaseOrderItem) {
        PurchaseOrderItem existingItem = getPurchaseOrderItemById(id);
        existingItem.setQuantity(purchaseOrderItem.getQuantity());
        existingItem.setPrice(purchaseOrderItem.getPrice());
        existingItem.setSubtotal(purchaseOrderItem.getSubtotal());
        existingItem.setPurchaseOrder(purchaseOrderItem.getPurchaseOrder());
        existingItem.setMaterial(purchaseOrderItem.getMaterial());
        return purchaseOrderItemRepository.save(existingItem);
    }

    public void deletePurchaseOrderItem(Long id) {
        PurchaseOrderItem existingItem = getPurchaseOrderItemById(id);
        purchaseOrderItemRepository.delete(existingItem);
    }

    public List<PurchaseOrderItem> findByPurchaseOrder(Long orderId) {
        List<PurchaseOrderItem> items = purchaseOrderItemRepository.findByPurchaseOrderOrderId(orderId);
        if (items != null && !items.isEmpty()) {
            return items;
        } else {
            return null;
        }
    }

    public List<PurchaseOrderItem> findByMaterial(Long materialId) {
        List<PurchaseOrderItem> items = purchaseOrderItemRepository.findByMaterialMaterialId(materialId);
        if (items != null && !items.isEmpty()) {
            return items;
        } else {
            return null;
        }
    }
}
