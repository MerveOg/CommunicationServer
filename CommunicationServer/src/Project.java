/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author merveog
 */
import java.util.ArrayList;

public class Project {

    private String projectName;
    private String projectKey;
    private String projectOwner;
    private ArrayList<String> teamMembers;

    public Project(String projectName, String projectKey, String projectOwner, ArrayList<String> teamMembers) {
        this.projectName = projectName;
        this.projectKey = projectKey;
        this.projectOwner = projectOwner;
        this.teamMembers = teamMembers;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    public ArrayList<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<String> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
