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
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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

    public ArrayList<String> getProjectTeam(String projectName) {
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
                // Add each team member to list. 
                for (String member : teamMembers) {
                    projectTeam.add(member.trim());
                }
            } else {
                System.out.println("Project " + projectName + " does not exist.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projectTeam;
    }

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

    public String getProjectKey(String projectName, String username) {
        String query = "SELECT projectkey FROM Project WHERE name = ? AND owner = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, projectName);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // If project owner and user match. 
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

    public boolean isUserInAnyTeam(String username) {
        String query = "SELECT name, team FROM Project";
        try {
            preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String projectName = resultSet.getString("name");
                String team = resultSet.getString("team");
                // Takım listesini virgülle ayır
                String[] teamMembers = team.split(",");
                // Kullanıcı adını kontrol et
                for (String member : teamMembers) {
                    if (member.trim().equals(username)) {
                        System.out.println("User " + username + " is in team of project " + projectName);
                        return true; // Kullanıcı projenin takımında bulundu
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("User " + username + " is not in any team.");
        return false; // Kullanıcı hiçbir projenin takımında bulunamadı
    }

    public void updateProjectTeam(String key, String team) {
        String sorgu = "UPDATE Project SET team = CONCAT(team, ?) WHERE projectkey = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, team + ",");
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
            System.out.println("İşlem başarılı");
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("İşlem başarısız");
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

//Girilen bilgilere göre doktor bilgilerini doktor database'ine aktaracak metodu yazdım.
    public void addUser(String name, String password) throws SQLException {
        statement = con.createStatement();

        String sorgu = "Insert Into UserInfos(username,password)VALUES ('" + name + "','" + password + "')";
        statement.executeUpdate(sorgu);
    }

//Girilen biigilere göre hasta bilgilerini hasta database'ine aktaracak metodu yazdım.
    public void addPatient(String name, String email, String password, String gender, int age, int id) throws SQLException {
        statement = con.createStatement();

        String sorgu = "Insert Into patients(name,email,password,gender,age,id)VALUES ('" + name + "','" + email + "','" + password + "','" + gender + "','" + age + "','" + id + "')";
        statement.executeUpdate(sorgu);
    }

//Doktorun girdiği bilgilere göre reçete bilgilerini reçete database'ine aktardım.
    public void prescribe(String name, String email, String medicine, String barcod, String daily, String weekly, String time, String hungry, String doctor) throws SQLException {
        statement = con.createStatement();

        String sorgu = "Insert Into prescribe(name,email,medicine,barcod,daily,weekly,time,hungry,doctor)VALUES ('" + name + "','" + email + "','" + medicine + "','" + barcod + "','" + daily + "','" + weekly + "','" + time + "','" + hungry + "','" + doctor + "')";
        statement.executeUpdate(sorgu);
    }

//doktor giriş yapacakken gireceği email ve passwordu doğru ise (önceden kayıt yapmışsa ve database'de o bilgilere karşılık
    //doktor bulunuyorsa) giriş yapabilecek.
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

//hasta giriş yapacakken gireceği email ve passwordu doğru ise (önceden kayıt yapmışsa ve database'de o bilgilere karşılık
    //hasta bulunuyorsa) giriş yapabilecek.
    public boolean controlEnterPatient(String email, String password) {
        String sorgu = "Select * From patients where email = ? and password = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//Girilen hasta bilgileri databasede bulunuyorsa doktorun randevu oluşturabilmesi için
    //bu metodu oluşturdum.

    public boolean controlPatientForPrescribe(String name, String email) {
        String sorgu = "Select * From patients where email = ? and name =?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);

            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//randevu bilgilerini bir arraylistte topladım ve doktor classında bu bilgileri tabloya aktardım.

//sisteme giriş yapan hastanın email adresi yardımıyla randevu bilgilerini çektim.
//verilen emaile ve doctor id'sine sahip reçete bilgisini silen metodu yazdım.
    public void deletePrescribe(String email, int id) {
        String sorgu = "Delete From prescribe Where email = ? and doctor =?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //doktor hesap oluşturmaya çalışırken girdiği (unique olması gereken) id ya da email'i başka 
    //bir kişide bulunup bulunmadığını kontrol eden boolean değer döndüren method yazdım.
    //Başka kişide bulunuyorsa o kişinin hesap oluşturabilmesi için yeni email ya da şifre girmesi gerekecek
    //Enter class'ında kontrolü yaptım.
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

    //hasta hesap oluşturmaya çalışırken girdiği (unique olması gereken) id ya da email'i başka 
    //bir kişide bulunup bulunmadığını kontrol eden boolean değer döndüren method yazdım.
    //Başka kişide bulunuyorsa o kişinin hesap oluşturabilmesi için yeni email ya da şifre girmesi gerekecek
    //Enter class'ında kontrolü yaptım.
    public boolean isThereThisPatient(String email, int id) {
        String sorgu = "Select * from patients where email = ? or id =?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, id);

            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//hasta şifresini update etmek istediği zaman gireceği email hastanın database bilgisinde
    //bulunuyorsa güncelleme yapabilmesini sağlayan metodu yazmak istedim.

    public boolean isThereThisEmailOnPatientForUpdatePass(String email) {
        String sorgu = "Select * from patients where email = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, email);

            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//doktor şifresini update etmek istediği zaman gireceği email doktorun database bilgisinde
    //bulunuyorsa güncelleme yapabilmesini sağlayan metodu yazmak istedim.

    public boolean isThereThisEmailOnDoctorsForUpdatePass(String email) {
        String sorgu = "Select * from doctors where email = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, email);

            ResultSet executeQuery = preparedStatement.executeQuery();
            return executeQuery.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
//doktorun şifresini güncelleyebilmesini sağlayan metodu yazdım.

    public void updatePasswordDoctor(String email, String newPassword) {
        String sorgu = "Update doctors set password = ? where email =?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("işlem basarısız");
        }

    }

//hastanın şifresini güncelleyebilmesini sağlayan metodu yazdım.
    public void updatePasswordPatient(String email, String newPassword) {
        String sorgu = "Update patients set password = ? where email =?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("işlem basarısız");
        }

    }

    //Enter tuşuna bastıktan sonra doktor bilgilerinin ekranda gözükmesi 
    //için bu metodu oluşturdum.
    //Enter tuşuna bastıktan sonra hasta bilgilerinin ekranda gözükmesi 
    //için bu metodu oluşturdum.
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

    private long idCounter = 0;

    public synchronized String createID() {
        return String.valueOf(idCounter++);
    }
}
