package com.example.demo.controller;

import com.example.demo.model.Location;
import com.example.demo.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // Save a location; parentId is optional (for province/district/sector/cell/village hierarchy)
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
}


