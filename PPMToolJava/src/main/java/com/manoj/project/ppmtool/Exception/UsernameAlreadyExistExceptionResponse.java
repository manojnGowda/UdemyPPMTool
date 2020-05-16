package com.manoj.project.ppmtool.Exception;

public class UsernameAlreadyExistExceptionResponse {
    private String username;

    public UsernameAlreadyExistExceptionResponse(String username) {
        this.username = username;
    }

    public String getProjectIdentifier() {
        return username;
    }

    public void setProjectIdentifier(String projectNotFound) {
        this.username = projectNotFound;
    }
}
