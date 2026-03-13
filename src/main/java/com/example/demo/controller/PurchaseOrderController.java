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

import com.example.demo.model.PurchaseOrder;
import com.example.demo.service.PurchaseOrderService;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        try {
            PurchaseOrder savedPurchaseOrder = purchaseOrderService.addNewPurchaseOrder(purchaseOrder);
            return new ResponseEntity<>(savedPurchaseOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        return new ResponseEntity<>(purchaseOrderService.getAllPurchaseOrders(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPurchaseOrderById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(purchaseOrderService.getPurchaseOrderById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePurchaseOrder(@PathVariable Long id, @RequestBody PurchaseOrder purchaseOrder) {
        try {
            return new ResponseEntity<>(purchaseOrderService.updatePurchaseOrder(id, purchaseOrder), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePurchaseOrder(@PathVariable Long id) {
        try {
            purchaseOrderService.deletePurchaseOrder(id);
            return new ResponseEntity<>("PurchaseOrder deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByStatus(@RequestParam String status) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.findByStatus(status);
        if (purchaseOrders != null) {
            return new ResponseEntity<>(purchaseOrders, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Purchase orders with that status are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/supplier", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBySupplier(@RequestParam Long supplierId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.findBySupplier(supplierId);
        if (purchaseOrders != null) {
            return new ResponseEntity<>(purchaseOrders, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Purchase orders with that supplier are not found", HttpStatus.NOT_FOUND);
        }
    }
}
