package com.example.demo.controller;

import com.example.demo.model.Location;
import com.example.demo.model.ELocationType;
import com.example.demo.service.LocationService;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    // Save a location; parentId is optional (for
    // province/district/sector/cell/village hierarchy)
    @PostMapping("/save")
    public ResponseEntity<?> saveLocation(
            @RequestBody Location location,
            @RequestParam(required = false) String parentId) {

        String result = locationService.saveLocation(location, parentId);

        if ("Location saved successfully".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
    }

    // Get all locations
    @GetMapping("/list")
    public ResponseEntity<List<Location>> getAllLocations() {
        return new ResponseEntity<>(locationRepository.findAll(), HttpStatus.OK);
    }

    // Get all locations by parent ID and type (e.g., all SECTORs in a DISTRICT)
    @GetMapping("/parent/{parentId}/type/{type}")
    public ResponseEntity<?> getLocationsByParentAndType(
            @PathVariable UUID parentId,
            @PathVariable ELocationType type) {

        List<Location> locations = locationService.findByParentIdAndType(parentId, type);

        if (locations != null && !locations.isEmpty()) {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No locations found with parent ID " + parentId + " and type " + type,
                    HttpStatus.NOT_FOUND);
        }
    }
}
