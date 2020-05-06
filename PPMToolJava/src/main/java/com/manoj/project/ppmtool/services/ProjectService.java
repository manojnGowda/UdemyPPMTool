package com.manoj.project.ppmtool.services;

import com.manoj.project.ppmtool.Exception.ProjectIdException;
import com.manoj.project.ppmtool.domain.Backlog;
import com.manoj.project.ppmtool.domain.Project;
import com.manoj.project.ppmtool.repository.BacklogRepository;
import com.manoj.project.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId()==null){
                Backlog backlog= new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }else{
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project Id '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
        }
    }

    public Project findByProjectIdentifier(String projectIdentifier){

        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Project Id '"+projectIdentifier.toUpperCase()+"' doesnt exists");
        }
        return project;
    }

    public Iterable<Project>findAllProject(){
        return projectRepository.findAll();
    }

    public void deleteProjectById(String projectIdentifier){

        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Project Id '"+projectIdentifier.toUpperCase()+"' doesnt exists");
        }
        projectRepository.delete(project);
    }
}
