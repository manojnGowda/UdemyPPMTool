package com.manoj.project.ppmtool.repository;

import com.manoj.project.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project,Long> {

    Project findByProjectIdentifier(String identifier);

    Iterable<Project> findAllByProjectLeader(String username);
}
