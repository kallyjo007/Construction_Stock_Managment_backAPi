package com.example.demo.service;

import com.example.demo.model.Warehouse;
import com.example.demo.model.Location;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private LocationRepository locationRepository;

    public Warehouse addNewWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    // Save warehouse with a specific location id (self-referencing Location hierarchy)
    public String saveWarehouseWithLocation(Warehouse warehouse, String locationId) {
        Location location = locationRepository
                .findById(java.util.UUID.fromString(locationId))
                .orElse(null);

        if (location != null) {
            warehouse.setLocation(location);
        }

        warehouseRepository.save(warehouse);

        return "Warehouse saved successfully";
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
    }

    public Warehouse updateWarehouse(Long id, Warehouse warehouse) {
        Warehouse existingWarehouse = getWarehouseById(id);
        existingWarehouse.setName(warehouse.getName());
        existingWarehouse.setAddress(warehouse.getAddress());
        existingWarehouse.setCapacity(warehouse.getCapacity());
        return warehouseRepository.save(existingWarehouse);
    }

    public void deleteWarehouse(Long id) {
        Warehouse existingWarehouse = getWarehouseById(id);
        warehouseRepository.delete(existingWarehouse);
    }

    public List<Warehouse> searchByName(String name) {
        List<Warehouse> warehouses = warehouseRepository.findByNameContaining(name);
        if (warehouses != null && !warehouses.isEmpty()) {
            return warehouses;
        } else {
            return null;
        }
    }

    public List<Warehouse> findByAddress(String address) {
        List<Warehouse> warehouses = warehouseRepository.findByAddress(address);
        if (warehouses != null && !warehouses.isEmpty()) {
            return warehouses;
        } else {
            return null;
        }
    }
}
