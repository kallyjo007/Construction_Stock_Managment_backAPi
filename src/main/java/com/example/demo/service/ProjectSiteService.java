package com.example.demo.service;

import com.example.demo.model.ProjectSite;
import com.example.demo.repository.ProjectSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectSiteService {

    @Autowired
    private ProjectSiteRepository projectSiteRepository;

    public ProjectSite addNewProjectSite(ProjectSite projectSite) {
        return projectSiteRepository.save(projectSite);
    }

    public List<ProjectSite> getAllProjectSites() {
        return projectSiteRepository.findAll();
    }

    public ProjectSite getProjectSiteById(Long id) {
        return projectSiteRepository.findById(id).orElseThrow(() -> new RuntimeException("ProjectSite not found with id: " + id));
    }

    public ProjectSite updateProjectSite(Long id, ProjectSite projectSite) {
        ProjectSite existingProjectSite = getProjectSiteById(id);
        existingProjectSite.setName(projectSite.getName());
        existingProjectSite.setLocation(projectSite.getLocation());
        existingProjectSite.setStartDate(projectSite.getStartDate());
        existingProjectSite.setStatus(projectSite.getStatus());
        return projectSiteRepository.save(existingProjectSite);
    }

    public void deleteProjectSite(Long id) {
        ProjectSite existingProjectSite = getProjectSiteById(id);
        projectSiteRepository.delete(existingProjectSite);
    }

    public List<ProjectSite> searchByName(String name) {
        List<ProjectSite> projectSites = projectSiteRepository.findByNameContaining(name);
        if (projectSites != null && !projectSites.isEmpty()) {
            return projectSites;
        } else {
            return null;
        }
    }

    public List<ProjectSite> findByStatus(String status) {
        List<ProjectSite> projectSites = projectSiteRepository.findByStatus(status);
        if (projectSites != null && !projectSites.isEmpty()) {
            return projectSites;
        } else {
            return null;
        }
    }
}
