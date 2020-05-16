package com.manoj.project.ppmtool.services;

import com.manoj.project.ppmtool.Exception.ProjectIdException;
import com.manoj.project.ppmtool.Exception.ProjectNotFoundException;
import com.manoj.project.ppmtool.domain.Backlog;
import com.manoj.project.ppmtool.domain.Project;
import com.manoj.project.ppmtool.domain.User;
import com.manoj.project.ppmtool.repository.BacklogRepository;
import com.manoj.project.ppmtool.repository.ProjectRepository;
import com.manoj.project.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project,String username){

        if(project.getId()!=null){
            Project existing = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existing!=null && !existing.getProjectLeader().equals(username)){
                throw new ProjectNotFoundException("Project doesnt below to your account");
            }else if(existing==null|| !existing.getId().toString().equals(project.getId().toString())){
                throw new ProjectNotFoundException("Project id: "+project.getId()+" doesnt exists");
            }
        }

        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(username);
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

    public Project findByProjectIdentifier(String projectIdentifier,String username){

        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Project Id '"+projectIdentifier.toUpperCase()+"' doesnt exists");
        }
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public Iterable<Project>findAllProject(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectById(String projectIdentifier,String username){
        Project project = findByProjectIdentifier(projectIdentifier,username);
        projectRepository.delete(project);
    }
}
