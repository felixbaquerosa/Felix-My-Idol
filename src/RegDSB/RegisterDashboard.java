package RegDSB;

import Config.*;
import LoginDSB.*;
import java.sql.*;
import javax.swing.*;

public class RegisterDashboard extends javax.swing.JFrame {

    public RegisterDashboard() {
        initComponents();
    }

    public static boolean registerAccount(String user, String pass, String email, String contact, String type) {
        try (Connection cn = new DBConnector().getConnection()) {

            if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                JOptionPane.showMessageDialog(null, "INVALID GMAIL ADDRESS!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!contact.matches("09\\d{9}")) {
                JOptionPane.showMessageDialog(null, "CONTACT NUMBER MUST BE 11 DIGITS STARTING WITH 09!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")) {
                JOptionPane.showMessageDialog(null, "PASSWORD MUST BE AT LEAST 8 CHARACTERS LONG AND CONTAIN AT LEAST ONE DIGIT, ONE LOWERCASE LETTER, ONE UPPERCASE LETTER, ONE SPECIAL CHARACTER, AND NO WHITESPACES!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            PreparedStatement checker = cn.prepareStatement("SELECT COUNT(*) FROM inventory WHERE email = ? OR user = ? OR contact = ?");
            checker.setString(1, email);
            checker.setString(2, user);
            checker.setString(3, contact);
            ResultSet rs = checker.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                JOptionPane.showMessageDialog(null, "ACCOUNT DUPLICATED!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            cn.setAutoCommit(false);

            PreparedStatement insert = cn.prepareStatement("INSERT INTO inventory (email, contact, user, pass, type, status) VALUES (?, ?, ?, ?, ?, 'PENDING')");
            insert.setString(1, email);
            insert.setString(2, contact);
            insert.setString(3, user);
            insert.setString(4, pass);
            insert.setString(5, type);
            int rows = insert.executeUpdate();

            cn.commit();

            JOptionPane.showMessageDialog(null, "ACCOUNT SUCCESSFULLY CREATED!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
            return rows > 0;

        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        contact = new javax.swing.JTextField();
        password = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        endUser = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(440, 536));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(440, 536));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("REGISTER");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 420, 110, -1));

        jButton1.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton1.setText("BACK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 420, 110, -1));

        contact.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        contact.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact.setText("CONTACT#\n");
        contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFocusGained(evt);
            }
        });
        jPanel1.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 240, -1));

        password.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD");
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel1.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 240, -1));

        username.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setText("USERNAME");
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFocusGained(evt);
            }
        });
        jPanel1.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 240, -1));

        email.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL ");
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });
        jPanel1.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 240, -1));

        jLabel5.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-sign-up-30.png"))); // NOI18N
        jLabel5.setText("REGISTER FORM");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        jLabel9.setFont(new java.awt.Font("Yu Gothic", 1, 10)); // NOI18N
        jLabel9.setText("MAKE SURE TO DOUBLE CHECK YOUR INFORMATION");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        endUser.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        endUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USER" }));
        endUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endUserActionPerformed(evt);
            }
        });
        jPanel1.add(endUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, 100, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 540));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new LoginDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
        email.setText("");
    }//GEN-LAST:event_emailMouseClicked

    private void usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusGained
        username.setText("");
    }//GEN-LAST:event_usernameFocusGained

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained

    private void contactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusGained
        contact.setText("");
    }//GEN-LAST:event_contactFocusGained

    private void endUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endUserActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String selectedUserType = endUser.getSelectedItem().toString();

        if (registerAccount(username.getText(), password.getText(), email.getText(), contact.getText(), selectedUserType)) {

            JOptionPane.showMessageDialog(this, "LOGIN SUCCESSFULLY!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
            new LoginDashboard().setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "INVALID USERNAME, PASSWORD, OR ACCOUNT IS INACTIVE!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField contact;
    private javax.swing.JTextField email;
    private javax.swing.JComboBox<String> endUser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField password;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
