package com.manoj.project.ppmtool.services;

import com.manoj.project.ppmtool.Exception.ProjectNotFoundException;
import com.manoj.project.ppmtool.domain.Backlog;
import com.manoj.project.ppmtool.domain.Project;
import com.manoj.project.ppmtool.domain.ProjectTask;
import com.manoj.project.ppmtool.repository.BacklogRepository;
import com.manoj.project.ppmtool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        Backlog backlog =backlogRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(backlog==null){
            throw new ProjectNotFoundException("The project id'"+projectIdentifier.toUpperCase()+"' doesnt exist");
        }
        projectTask.setBacklog(backlog);
        backlog.setPTSequence(backlog.getPTSequence()+1);
        projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+backlog.getPTSequence());
        projectTask.setProjectIdentifier(backlog.getProjectIdentifier());
        if(projectTask.getPriority()==null || projectTask.getPriority()==0){
            projectTask.setPriority(3);
        }
        if(projectTask.getStatus()==null || projectTask.getStatus()==""){
            projectTask.setStatus("TO_DO");
        }
        return projectTaskRepository.save(projectTask);
    }

    public List<ProjectTask> findByBacklogId(String backlog_id) {
        List<ProjectTask> projectTaskList =  projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id.toUpperCase());
        if(projectTaskList.isEmpty()){
            throw new ProjectNotFoundException("The project id'"+backlog_id.toUpperCase()+"' doesnt exist");
        }
        return projectTaskList;
    }

    public ProjectTask findByProjectSequence(String backlogId,String projectSequence){
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId.toUpperCase());
        if(backlog==null){
            throw new ProjectNotFoundException("The project id'"+backlogId.toUpperCase()+"' doesnt exist");
        }
        ProjectTask task = projectTaskRepository.findByProjectSequence(projectSequence);
        if(task==null){
            throw new ProjectNotFoundException("The project task'"+projectSequence.toUpperCase()+"' doesnt exist");
        }
        if(!task.getProjectIdentifier().equals(backlogId.toUpperCase())){
            throw new ProjectNotFoundException("The project task'"+projectSequence.toUpperCase()+"' doesnt exist in project "+backlogId);
        }
        return task;
    }

    public ProjectTask updateProjectTask(ProjectTask updatedTask,String backlogId,String ptId){
        ProjectTask projectTask = findByProjectSequence(backlogId,ptId);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlogId,String projectSequence){
        ProjectTask projectTask = findByProjectSequence(backlogId,projectSequence);

        projectTaskRepository.delete(projectTask);
    }
}
