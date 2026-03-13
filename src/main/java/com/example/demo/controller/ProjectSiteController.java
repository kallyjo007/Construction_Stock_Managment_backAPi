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

import com.example.demo.model.ProjectSite;
import com.example.demo.service.ProjectSiteService;

@RestController
@RequestMapping("/api/project-sites")
public class ProjectSiteController {

    @Autowired
    private ProjectSiteService projectSiteService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewProjectSite(@RequestBody ProjectSite projectSite) {
        try {
            ProjectSite savedProjectSite = projectSiteService.addNewProjectSite(projectSite);
            return new ResponseEntity<>(savedProjectSite, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProjectSite>> getAllProjectSites() {
        return new ResponseEntity<>(projectSiteService.getAllProjectSites(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProjectSiteById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(projectSiteService.getProjectSiteById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProjectSite(@PathVariable Long id, @RequestBody ProjectSite projectSite) {
        try {
            return new ResponseEntity<>(projectSiteService.updateProjectSite(id, projectSite), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteProjectSite(@PathVariable Long id) {
        try {
            projectSiteService.deleteProjectSite(id);
            return new ResponseEntity<>("ProjectSite deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        List<ProjectSite> projectSites = projectSiteService.searchByName(name);
        if (projectSites != null) {
            return new ResponseEntity<>(projectSites, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Project sites with that name are not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByStatus(@RequestParam String status) {
        List<ProjectSite> projectSites = projectSiteService.findByStatus(status);
        if (projectSites != null) {
            return new ResponseEntity<>(projectSites, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Project sites with that status are not found", HttpStatus.NOT_FOUND);
        }
    }
}
