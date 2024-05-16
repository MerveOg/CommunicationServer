/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 *
 * @author merve
 */
public class DataConnection {

    private String kullanici_adi = "root";
    private String parola = "";

    private String db_ismi = "CommunicationServer";

    private String host = "localhost";

    private int port = 3306;

    private Connection con = null;

    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    public DataConnection() {
        //jdbc:mysql://localhost:3306/demo
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db_ismi + "?useUnicode=true&characterEncoding=utf8";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver could not find");
        }

        try {
            con = DriverManager.getConnection(url, kullanici_adi, parola);
            System.out.println("Connection succeed.");
        } catch (SQLException ex) {
            System.out.println("Connection unsucceed.");
            //ex.printStackTrace();
        }

    }

    //For to show project team on list. 
    public ArrayList<String> getProjectTeam(String projectName, String excludedName) {
        ArrayList<String> projectTeam = new ArrayList<>();
        String query = "SELECT team FROM Project WHERE name = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, projectName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String team = resultSet.getString("team");
                // Split team members with ","
                String[] teamMembers = team.split(",");
                // Add each team member to list except the excluded one
                for (String member : teamMembers) {
                    String trimmedMember = member.trim();
                    if (!trimmedMember.equals(excludedName)) {
                        projectTeam.add(trimmedMember);
                    }
                }
            } else {
                System.out.println("Project " + projectName + " does not exist.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projectTeam;
    }

    //For to show user projects on list.
    public ArrayList<String> getUserProjects(String username) {
        ArrayList<String> userProjects = new ArrayList<>();
        String query = "SELECT name, team FROM Project";
        try {
            preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String projectName = resultSet.getString("name");
                String team = resultSet.getString("team");
                // Split team members with ","
                String[] teamMembers = team.split(",");
                // Check user name
                for (String member : teamMembers) {
                    if (member.trim().equals(username)) {
                        System.out.println("User " + username + " is in team of project " + projectName);
                        userProjects.add(projectName);
                        break; // Projeye zaten eklendi, döngüyü sonlandır
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (userProjects.isEmpty()) {
            System.out.println("User " + username + " is not in any team.");
        }
        return userProjects;
    }

    //Checled if a user own given project or not. 
    public boolean isProjectOwnedByUser(String projectName, String username) {
        String query = "SELECT projectkey FROM Project WHERE name = ? AND owner = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // If project owner and user match 
                System.out.println("User " + username + " owns the project " + projectName);
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("User " + username + " does not own the project " + projectName);
        return false;
    }

    //for to get project key of given projectname and username
    public String getProjectKey(String projectName, String username) {
        String query = "SELECT projectkey FROM Project WHERE name = ? AND owner = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String projectKey = resultSet.getString("projectkey");
                System.out.println("User " + username + " owns the project " + projectName + " with key " + projectKey);
                return projectKey;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("User " + username + " does not own the project " + projectName);
        return null; // Project key couldn't find. 
    }

    //To check if user in any team or not. 
    public boolean isUserInAnyTeam(String username) {
        String query = "SELECT name, team FROM Project";
        try {
            preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String projectName = resultSet.getString("name");
                String team = resultSet.getString("team");
                String[] teamMembers = team.split(",");
                for (String member : teamMembers) {
                    if (member.trim().equals(username)) {
                        System.out.println("User " + username + " is in team of project " + projectName);
                        return true; // User is in project team. 
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; 
    }

    //When a new client join a project, update team of project. 
    public void updateProjectTeam(String key, String team) {
        String sorgu = "UPDATE Project SET team = CONCAT(team, ?) WHERE projectkey = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, team + ",");
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getProjectName(String key) {
        String projectName = null;
        String query = "SELECT name FROM Project WHERE projectkey = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                projectName = resultSet.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projectName;
    }

    public boolean isThereThisProject(String key) {
        String sorgu = "Select * from Project where projectkey = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, key);
            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void addProject(String name, String key, String owner, String team) throws SQLException {
        statement = con.createStatement();

        String sorgu = "Insert Into Project(name,projectkey,owner,team)VALUES ('" + name + "','" + key + "','" + owner + "','" + team + "')";
        statement.executeUpdate(sorgu);
    }

    public void addUser(String name, String password) throws SQLException {
        statement = con.createStatement();

        String sorgu = "Insert Into UserInfos(username,password)VALUES ('" + name + "','" + password + "')";
        statement.executeUpdate(sorgu);
    }
    
    public boolean controlLogIn(String username, String password) {
        String sorgu = "Select*From UserInfos where username = ? and password = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isThereThisProjectName(String projectName) {
        String sorgu = "Select * from Project where name = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, projectName);

            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isThereThisUser(String username, String password) {
        String sorgu = "Select * from UserInfos where username = ? or password =? ";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }

}
