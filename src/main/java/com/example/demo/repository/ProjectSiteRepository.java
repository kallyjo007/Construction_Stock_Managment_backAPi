package com.example.demo.repository;

import com.example.demo.model.ProjectSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSiteRepository extends JpaRepository<ProjectSite, Long> {
    List<ProjectSite> findByNameContaining(String name);

    List<ProjectSite> findByStatus(String status);
}
