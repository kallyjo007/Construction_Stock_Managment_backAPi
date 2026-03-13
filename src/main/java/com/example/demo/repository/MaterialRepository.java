package com.example.demo.repository;

import com.example.demo.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByName(String name);

    List<Material> findByNameContaining(String name);

    List<Material> findByCategory_CategoryId(Long categoryId);

    List<Material> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Material> findByQuantityLessThanEqual(int quantity);
}
