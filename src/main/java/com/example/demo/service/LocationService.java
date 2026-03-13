package com.example.demo.service;

import com.example.demo.model.Location;
import com.example.demo.model.ELocationType;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // Save a location with optional parent (self-referencing hierarchy)
    public String saveLocation(Location location, String parentId) {

        if (parentId != null) {
            Location parent = locationRepository
                    .findById(UUID.fromString(parentId))
                    .orElse(null);

            if (parent != null) {
                location.setParent(parent);
            }
        }

        Boolean exists = locationRepository.existsByCode(location.getCode());

        if (exists) {
            return "Location with that code already exists";
        } else {
            locationRepository.save(location);
            return "Location saved successfully";
        }
    }

    // Get all locations by parent ID and type (e.g., all SECTORs in a DISTRICT)
    public List<Location> findByParentIdAndType(UUID parentId, ELocationType type) {
        return locationRepository.findByParentIdAndType(parentId, type);
    }
}
