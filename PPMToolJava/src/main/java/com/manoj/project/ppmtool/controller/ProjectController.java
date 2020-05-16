package com.manoj.project.ppmtool.controller;

import com.manoj.project.ppmtool.domain.Project;
import com.manoj.project.ppmtool.domain.User;
import com.manoj.project.ppmtool.services.ExceptionValidatorService;
import com.manoj.project.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ExceptionValidatorService exceptionValidatorService;

    @PostMapping("")
    public ResponseEntity<?>createNewProject(@Valid @RequestBody Project project, BindingResult bindingResult, Principal principal){
        ResponseEntity response = exceptionValidatorService.validBindingResult(bindingResult);
        if(response!=null) return response;

        Project result = projectService.saveOrUpdateProject(project,principal.getName());
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?>getProjectById(@PathVariable String projectIdentifier,Principal principal){
        Project project = projectService.findByProjectIdentifier(projectIdentifier,principal.getName());
        return new ResponseEntity<>(project,HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project>getAllProject(Principal principal){
        return projectService.findAllProject(principal.getName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteProjectById(@PathVariable String id,Principal principal){
        projectService.deleteProjectById(id,principal.getName());
        return new ResponseEntity<>("Project Id '"+id+"' has been deleted",HttpStatus.OK);
    }

}
