package com.manoj.project.ppmtool.controller;

import com.manoj.project.ppmtool.domain.ProjectTask;
import com.manoj.project.ppmtool.services.ExceptionValidatorService;
import com.manoj.project.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BackLogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ExceptionValidatorService exceptionValidatorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity addPTtoBacklog(@PathVariable String backlog_id,@Valid @RequestBody ProjectTask projectTask, BindingResult bindingResult){

        ResponseEntity<?> error = exceptionValidatorService.validBindingResult(bindingResult);
        if(error!=null)return error;
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id,projectTask);
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public ResponseEntity<List<ProjectTask>> getProjectBackLog(@PathVariable String backlog_id){
        return new ResponseEntity<List<ProjectTask>>(projectTaskService.findByBacklogId(backlog_id),HttpStatus.OK);
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id,@PathVariable String pt_id){
        return new ResponseEntity<>(projectTaskService.findByProjectSequence(backlog_id,pt_id),HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@PathVariable String backlog_id,@PathVariable String pt_id,@Valid @RequestBody ProjectTask projectTask, BindingResult bindingResult){
        ResponseEntity<?> error = exceptionValidatorService.validBindingResult(bindingResult);
        if(error!=null)return error;
        return new ResponseEntity<>(projectTaskService.updateProjectTask(projectTask,backlog_id,pt_id),HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@PathVariable String backlog_id,@PathVariable String pt_id){
        projectTaskService.deletePTByProjectSequence(backlog_id,pt_id);
        return new ResponseEntity<>("project task '"+pt_id+"' was delete successfully",HttpStatus.OK);

    }

}
