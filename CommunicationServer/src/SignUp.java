
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author merveog
 */
public class SignUp extends javax.swing.JFrame {

    /**
     * Creates new form SignUp
     */
    public SignUp() {
        initComponents();
    }

    //Başlangıçta min 1 maksimum 3 tane rakam olmalı. Devamında min 1 maksimum 9 büyük harf içermeli.Sonrasında küçük harf olmalı.
    public boolean isCorrectPassword(String password) {
        Matcher matcher;

        matcher = Pattern.compile("^[0-9]{1,3}[A-Z]{1,9}[a-z]{1,8}").matcher(password);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnSignUp = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnLogIn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtPasswordAgain = new javax.swing.JPasswordField();
        btnGoLogIn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(393, 383));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 0));
        jLabel1.setText("SIGN UP");

        jLabel2.setText("User Name");

        jLabel4.setText("Password");

        btnSignUp.setBackground(new java.awt.Color(0, 102, 0));
        btnSignUp.setForeground(new java.awt.Color(255, 255, 255));
        btnSignUp.setText("Sign Up");
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });

        jLabel5.setText("Do you already have an account? ");

        btnLogIn.setText("Log In");
        btnLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogInActionPerformed(evt);
            }
        });

        jLabel6.setText("Password Again");

        btnGoLogIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back (1).png"))); // NOI18N
        btnGoLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoLogInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnGoLogIn)
                .addGap(91, 91, 91)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogIn))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtPasswordAgain, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnSignUp, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtUserName, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoLogIn)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel6)
                .addGap(17, 17, 17)
                .addComponent(txtPasswordAgain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSignUp)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnLogIn))
                .addContainerGap(145, Short.MAX_VALUE))
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

    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed

        String name;

        String password = "";
        boolean isThereFreeSpace = false;
        DataConnection connect = new DataConnection();

        if (txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            isThereFreeSpace = true;
        }
        if (isThereFreeSpace) {
            JOptionPane.showMessageDialog(rootPane, "Fill All Areas", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            name = txtUserName.getText();

            String password1 = Arrays.toString(txtPassword.getPassword());
            String password2 = Arrays.toString(txtPasswordAgain.getPassword());

            if (password1.equals(password2)) {

                password = txtPassword.getText();
                System.out.println(password);

                boolean thereThisUser = connect.isThereThisUser(name, password);
                if (!thereThisUser) {
                    try {
                        connect.addUser(name, password);
                    } catch (SQLException ex) {
                        Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    JOptionPane.showMessageDialog(rootPane, "IT IS SUCCESSFULL DEAR User", "GREAT", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();

                    Login login = new Login();
                    login.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(rootPane, "This user or password already exist!", "Warning", JOptionPane.WARNING_MESSAGE);

                }
            } else {
                txtPassword.setText(null);
                txtPasswordAgain.setText(null);

                JOptionPane.showMessageDialog(rootPane, "Passwords did not match. Try again", "Warning", JOptionPane.WARNING_MESSAGE);

            }

        }
    }//GEN-LAST:event_btnSignUpActionPerformed

    private void btnLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogInActionPerformed
        this.dispose();

        Login login = new Login();
        login.setVisible(true);
    }//GEN-LAST:event_btnLogInActionPerformed

    private void btnGoLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoLogInActionPerformed
        Login login = new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnGoLogInActionPerformed

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
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGoLogIn;
    private javax.swing.JButton btnLogIn;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtPasswordAgain;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
