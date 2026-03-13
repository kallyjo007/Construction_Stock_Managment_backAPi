package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role addNewRole(Role role) {
        Optional<Role> existRole = roleRepository.findByRoleName(role.getRoleName());
        if (existRole.isPresent()) {
            throw new RuntimeException("Role with name " + role.getRoleName() + " already exists");
        }
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    public Role updateRole(Long id, Role role) {
        Role existingRole = getRoleById(id);
        existingRole.setRoleName(role.getRoleName());
        existingRole.setDescription(role.getDescription());
        return roleRepository.save(existingRole);
    }

    public void deleteRole(Long id) {
        Role existingRole = getRoleById(id);
        roleRepository.delete(existingRole);
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElse(null);
    }
}
