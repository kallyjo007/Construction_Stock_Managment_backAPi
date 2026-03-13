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

import com.example.demo.model.Warehouse;
import com.example.demo.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewWarehouse(@RequestBody Warehouse warehouse) {
        try {
            Warehouse savedWarehouse = warehouseService.addNewWarehouse(warehouse);
            return new ResponseEntity<>(savedWarehouse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Alternative save that links Warehouse to a Location by id (UUID as String)
    @PostMapping(value = "/save-with-location", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveWarehouseWithLocation(
            @RequestBody Warehouse warehouse,
            @RequestParam String locationId) {

        String result = warehouseService.saveWarehouseWithLocation(warehouse, locationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        return new ResponseEntity<>(warehouseService.getAllWarehouses(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWarehouseById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(warehouseService.getWarehouseById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateWarehouse(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        try {
            return new ResponseEntity<>(warehouseService.updateWarehouse(id, warehouse), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long id) {
        try {
            warehouseService.deleteWarehouse(id);
            return new ResponseEntity<>("Warehouse deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        List<Warehouse> warehouses = warehouseService.searchByName(name);
        if (warehouses != null) {
            return new ResponseEntity<>(warehouses, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Warehouses with that name are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByAddress(@RequestParam String address) {
        List<Warehouse> warehouses = warehouseService.findByAddress(address);
        if (warehouses != null) {
            return new ResponseEntity<>(warehouses, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Warehouses with that address are not found", HttpStatus.NOT_FOUND);
        }
    }
}
