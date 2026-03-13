package com.example.demo.repository;

import com.example.demo.model.Location;
import com.example.demo.model.ELocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    Boolean existsByCode(String code);

    List<Location> findByParentIdAndType(UUID parentId, ELocationType type);
}
