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

import java.util.List;

import com.example.demo.model.PurchaseOrderItem;
import com.example.demo.service.PurchaseOrderItemService;

@RestController
@RequestMapping("/api/purchase-order-items")
public class PurchaseOrderItemController {

    @Autowired
    private PurchaseOrderItemService purchaseOrderItemService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewPurchaseOrderItem(@RequestBody PurchaseOrderItem purchaseOrderItem) {
        try {
            PurchaseOrderItem savedItem = purchaseOrderItemService.addNewPurchaseOrderItem(purchaseOrderItem);
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PurchaseOrderItem>> getAllPurchaseOrderItems() {
        return new ResponseEntity<>(purchaseOrderItemService.getAllPurchaseOrderItems(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPurchaseOrderItemById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(purchaseOrderItemService.getPurchaseOrderItemById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePurchaseOrderItem(@PathVariable Long id, @RequestBody PurchaseOrderItem purchaseOrderItem) {
        try {
            return new ResponseEntity<>(purchaseOrderItemService.updatePurchaseOrderItem(id, purchaseOrderItem), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePurchaseOrderItem(@PathVariable Long id) {
        try {
            purchaseOrderItemService.deletePurchaseOrderItem(id);
            return new ResponseEntity<>("PurchaseOrderItem deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByPurchaseOrder(@RequestParam Long orderId) {
        List<PurchaseOrderItem> items = purchaseOrderItemService.findByPurchaseOrder(orderId);
        if (items != null) {
            return new ResponseEntity<>(items, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Purchase order items for that order are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/material", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByMaterial(@RequestParam Long materialId) {
        List<PurchaseOrderItem> items = purchaseOrderItemService.findByMaterial(materialId);
        if (items != null) {
            return new ResponseEntity<>(items, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Purchase order items for that material are not found", HttpStatus.NOT_FOUND);
        }
    }
}
