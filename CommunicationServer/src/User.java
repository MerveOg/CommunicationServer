/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author merveog
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class User extends javax.swing.JFrame {

    DataConnection cn = new DataConnection();
    DefaultListModel listModel = new DefaultListModel();
    DefaultListModel listModel2 = new DefaultListModel();

    /**
     * Creates new form User
     */
    public User() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listOfProjects = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        txtProjectName = new javax.swing.JTextField();
        btnCreateProject = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnJoinProject = new javax.swing.JButton();
        txtProjectKey = new javax.swing.JTextField();
        lblSpecialKey = new javax.swing.JLabel();
        btnGoProject = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblUserName.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblUserName.setText("Username");

        listOfProjects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOfProjectsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listOfProjects);

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel2.setText("Project Name:");

        btnCreateProject.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnCreateProject.setText("Create Project");
        btnCreateProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateProjectActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel3.setText("My Projects");

        btnJoinProject.setText("Join Project");
        btnJoinProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinProjectActionPerformed(evt);
            }
        });

        btnGoProject.setText("Go To Project");
        btnGoProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoProjectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGoProject))
                    .addComponent(lblSpecialKey, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCreateProject))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtProjectName))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(txtProjectKey)
                        .addGap(18, 18, 18)
                        .addComponent(btnJoinProject))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                .addGap(23, 23, 23))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addComponent(lblUserName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lblUserName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(btnGoProject))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnCreateProject)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSpecialKey, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProjectKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnJoinProject))
                .addGap(49, 49, 49))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreateProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateProjectActionPerformed
        listOfProjects.setModel(listModel);
        lblSpecialKey.setText(UUID.randomUUID().toString());

//        String randomUUID = UUID.randomUUID().toString();
//String firstSixChars = randomUUID.substring(0, 6);
//lblSpecialKey.setText(firstSixChars);
        String projectOwner = lblUserName.getText();
        String projectKey = lblSpecialKey.getText();
        String projectName = txtProjectName.getText();

        if (projectName.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Project name can't be empty! Please give a name to project");

        } else {
            listModel.addElement(txtProjectName.getText());

            try {
                cn.addProject(projectName, projectKey, projectOwner, projectOwner + ",");
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnCreateProjectActionPerformed

    private void btnJoinProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinProjectActionPerformed
        listOfProjects.setModel(listModel);
        boolean isValidKey2 = cn.isThereThisProject("d13231f4-9fda-42c0-aeec-1f9c7479743c");
        boolean isValidKey = cn.isThereThisProject(txtProjectKey.getText());
        System.out.println(isValidKey2);
        if (isValidKey2) {
            cn.updateProjectTeam("d13231f4-9fda-42c0-aeec-1f9c7479743c", lblUserName.getText());
            listModel.addElement(cn.getProjectName("d13231f4-9fda-42c0-aeec-1f9c7479743c"));
        }

    }//GEN-LAST:event_btnJoinProjectActionPerformed

    private void btnGoProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoProjectActionPerformed
        if (listOfProjects.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(rootPane, "First choose a project to go! ");
        } else {
            String projectName = listOfProjects.getSelectedValue();
            String projectKey = cn.getProjectKey(projectName, lblUserName.getText());
            Chat chat = new Chat(lblUserName.getText(), projectName, projectKey);

            ArrayList<String> projectTeam = cn.getProjectTeam(projectName);
            chat.listOfTeam.setModel(listModel2);

            //chat.lblUserName.setText(lblUserName.getText());
            for (String member : projectTeam) {
                listModel2.addElement(member);
            }

            chat.lblName.setText(projectName);
            if (projectKey != null) {
                chat.lblKey.setText(projectKey);

                chat.setVisible(true);
                this.dispose();
            } else {
                chat.setVisible(true);
                this.dispose();
            }
        }


    }//GEN-LAST:event_btnGoProjectActionPerformed

    private void listOfProjectsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOfProjectsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_listOfProjectsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateProject;
    private javax.swing.JButton btnGoProject;
    private javax.swing.JButton btnJoinProject;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSpecialKey;
    public javax.swing.JLabel lblUserName;
    public javax.swing.JList<String> listOfProjects;
    private javax.swing.JTextField txtProjectKey;
    private javax.swing.JTextField txtProjectName;
    // End of variables declaration//GEN-END:variables
}
