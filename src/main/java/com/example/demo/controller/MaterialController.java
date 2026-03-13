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

import com.example.demo.model.Material;
import com.example.demo.service.MaterialService;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewMaterial(@RequestBody Material material) {
        try {
            Material savedMaterial = materialService.addNewMaterial(material);
            return new ResponseEntity<>(savedMaterial, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Material>> getAllMaterials() {
        return new ResponseEntity<>(materialService.getAllMaterials(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMaterialById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(materialService.getMaterialById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMaterial(@PathVariable Long id, @RequestBody Material material) {
        try {
            return new ResponseEntity<>(materialService.updateMaterial(id, material), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        try {
            materialService.deleteMaterial(id);
            return new ResponseEntity<>("Material deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        List<Material> materials = materialService.searchByName(name);
        if (materials != null) {
            return new ResponseEntity<>(materials, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Materials with that name are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByCategory(@RequestParam Long categoryId) {
        List<Material> materials = materialService.findByCategory(categoryId);
        if (materials != null) {
            return new ResponseEntity<>(materials, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Materials with that category are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/price-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<Material> materials = materialService.findByPriceRange(minPrice, maxPrice);
        if (materials != null) {
            return new ResponseEntity<>(materials, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Materials within that price range are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/low-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findLowStockMaterials(@RequestParam int minLevel) {
        List<Material> materials = materialService.findLowStockMaterials(minLevel);
        if (materials != null) {
            return new ResponseEntity<>(materials, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Low stock materials are not found", HttpStatus.NOT_FOUND);
        }
    }
}
