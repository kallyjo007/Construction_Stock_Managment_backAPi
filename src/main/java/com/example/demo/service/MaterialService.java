package com.example.demo.service;

import com.example.demo.model.Material;
import com.example.demo.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public Material addNewMaterial(Material material) {
        Optional<Material> existMaterial = materialRepository.findByName(material.getName());
        if (existMaterial.isPresent()) {
            throw new RuntimeException("Material with name " + material.getName() + " already exists");
        }
        return materialRepository.save(material);
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
    }

    public Material updateMaterial(Long id, Material material) {
        Material existingMaterial = getMaterialById(id);
        existingMaterial.setName(material.getName());
        existingMaterial.setUnit(material.getUnit());
        existingMaterial.setPrice(material.getPrice());
        existingMaterial.setMinLevel(material.getMinLevel());
        existingMaterial.setQuantity(material.getQuantity());
        existingMaterial.setCategory(material.getCategory());
        return materialRepository.save(existingMaterial);
    }

    public void deleteMaterial(Long id) {
        Material existingMaterial = getMaterialById(id);
        materialRepository.delete(existingMaterial);
    }

    public List<Material> searchByName(String name) {
        List<Material> materials = materialRepository.findByNameContaining(name);
        if (materials != null && !materials.isEmpty()) {
            return materials;
        } else {
            return null;
        }
    }

    public List<Material> findByCategory(Long categoryId) {
        List<Material> materials = materialRepository.findByCategory_CategoryId(categoryId);
        if (materials != null && !materials.isEmpty()) {
            return materials;
        } else {
            return null;
        }
    }

    public List<Material> findByPriceRange(Double minPrice, Double maxPrice) {
        List<Material> materials = materialRepository.findByPriceBetween(minPrice, maxPrice);
        if (materials != null && !materials.isEmpty()) {
            return materials;
        } else {
            return null;
        }
    }

    public List<Material> findLowStockMaterials(int minLevel) {
        List<Material> materials = materialRepository.findByQuantityLessThanEqual(minLevel);
        if (materials != null && !materials.isEmpty()) {
            return materials;
        } else {
            return null;
        }
    }
}
