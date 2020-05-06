package com.manoj.project.ppmtool.Exception;

public class ProjectNotFoundExceptionResponse {
    private String projectNotFound;

    public ProjectNotFoundExceptionResponse(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }

    public String getProjectIdentifier() {
        return projectNotFound;
    }

    public void setProjectIdentifier(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }
}
