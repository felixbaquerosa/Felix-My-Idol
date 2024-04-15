package LoginDSB;

import AdminDSB.*;
import Config.*;
import RegDSB.*;
import UserDSB.*;
import java.sql.*;
import javax.swing.*;

public class LoginDashboard extends javax.swing.JFrame {

    public LoginDashboard() {
        initComponents();
    }

    public static String loginDB(String user, String pass) {
        try (Connection cn = new DBConnector().getConnection()) {
            PreparedStatement checkStmt = cn.prepareStatement("SELECT * FROM inventory WHERE (user = ? AND pass = ?) AND (type = 'ADMIN' OR type = 'USER')");
            checkStmt.setString(1, user);
            checkStmt.setString(2, pass);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("status");
                if (status.equals("ACTIVE")) {
                    String userType = resultSet.getString("type");
                    return userType;
                } else if (status.equals("PENDING")) {
                    return "PENDING";
                } else if (status.equals("DECLINED")) {
                    return "DECLINED";
                } else {
                    return "INACTIVE"; 
                }
            } else {
                return null;
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        showPass = new javax.swing.JCheckBox();
        password = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(635, 423));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Yu Gothic", 1, 15)); // NOI18N
        jLabel7.setText("SIGN-IN");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 380, -1, -1));

        username.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        username.setForeground(new java.awt.Color(102, 102, 102));
        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setText("USERNAME");
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFocusGained(evt);
            }
        });
        username.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameMouseClicked(evt);
            }
        });
        jPanel1.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 420, 265, 29));

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("EXIT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 30, 90, -1));

        showPass.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        showPass.setText("SHOW PASSWORD");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });
        jPanel1.add(showPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 510, -1, -1));

        password.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD\n");
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel1.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 470, 265, 30));

        jButton3.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton3.setText("LOGIN");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 510, 85, -1));

        jLabel1.setFont(new java.awt.Font("Yu Gothic", 1, 25)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/inventory system.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, -1, 360));

        jButton4.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton4.setText("SIGN UP");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 30, 90, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String userType = loginDB(username.getText(), password.getText());

        if (userType != null) {
            if (userType.equals("PENDING")) {
                JOptionPane.showMessageDialog(null, "PLEASE WAIT FOR THE ADMIN APPROVAL!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            } else if (userType.equals("DECLINED")) {
                JOptionPane.showMessageDialog(null, "YOUR ACCOUNT HAS BEEN DECLINED!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            } else {

                JOptionPane.showMessageDialog(this, "LOGIN SUCCESSFUL!");

                if (userType.equalsIgnoreCase("ADMIN")) {
                    new AdminDashboard(username.getText()).setVisible(true);
                    dispose();
                } else if (userType.equalsIgnoreCase("USER")) {
                    new UserDashboard(username.getText()).setVisible(true);
                    dispose();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "ACCOUNT NOT FOUND!");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed

        if (showPass.isSelected()) {
            password.setEchoChar((char) 0);
        } else {
            password.setEchoChar('*');
        }
    }//GEN-LAST:event_showPassActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void usernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameMouseClicked
        username.setText("");
    }//GEN-LAST:event_usernameMouseClicked

    private void usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusGained

    }//GEN-LAST:event_usernameFocusGained

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new RegisterDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
