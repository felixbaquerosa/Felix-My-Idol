package RegDSB;

import Config.*;
import LoginDSB.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.swing.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class RegisterDashboard extends javax.swing.JFrame {

    public RegisterDashboard() {
        initComponents();
    }

    private String xemail, xusername;

    private boolean duplicateChecker() throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from inventory where email = '" + email.getText() + "' or username = '" + username.getText() + "'");

        if (rs.next()) {
            xemail = rs.getString("email");
            if (xemail.equals(email.getText())) {
                JOptionPane.showMessageDialog(this, "EMAIL HAS BEEN USED!", "OH NO!", ERROR_MESSAGE);
            }

            xusername = rs.getString("username");
            if (xusername.equals(username.getText())) {
                JOptionPane.showMessageDialog(this, "USERNAME HAS BEEN USED!", "OH NO!", ERROR_MESSAGE);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean validationChecker() {
        if (username.getText().isEmpty() || password.getText().isEmpty() || email.getText().isEmpty() || contact.getText().isEmpty()) {
            errorMessage("FILL ALL THE REQUIREMENTS!");
            return false;
        } else if (password.getText().length() < 8) {
            errorMessage("PASSWORD MUST BE AT LEAST 8 CHARACTERS!");
            return false;
        } else if (!contact.getText().matches("\\d+")) {
            errorMessage("CONTACT MUST CONTAIN ONLY DIGITS!");
            return false;
        } else {
            return true;
        }
    }

    private void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "ERROR!", JOptionPane.ERROR_MESSAGE);
    }

    private void successMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        contact = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        type = new javax.swing.JComboBox<>();
        password = new javax.swing.JPasswordField();
        showPass = new javax.swing.JCheckBox();

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

        type.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USER" }));
        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel1.add(type, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 240, -1));

        password.setForeground(new java.awt.Color(102, 102, 102));
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD");
        password.setMinimumSize(new java.awt.Dimension(43, 25));
        password.setPreferredSize(new java.awt.Dimension(43, 25));
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel1.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 240, 30));

        showPass.setBackground(new java.awt.Color(255, 255, 255));
        showPass.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        showPass.setText("SHOW PASSWORD");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });
        jPanel1.add(showPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 360, -1, -1));

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

    private void contactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusGained
        contact.setText("");
    }//GEN-LAST:event_contactFocusGained

    private void typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_typeActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if (duplicateChecker()) {
            } else if (!validationChecker()) {
            } else {
                String pass = passwordHashing.hashPassword(password.getText());
                new DBConnector().insertData("insert into inventory (email,username,password,contact,type,status) "
                        + "values ('" + email.getText() + "','" + username.getText() + "', '" + pass + "'"
                        + ",'" + contact.getText() + "','" + type.getSelectedItem() + "','PENDING')");

                JOptionPane.showMessageDialog(this, "REGISTRATION SUCCESSFULL!", "SUCCESS", INFORMATION_MESSAGE);

                new LoginDashboard().setVisible(true);
                dispose();

            }
        } catch (SQLException er) {
            System.out.println("Eror: " + er.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        char echoChar = showPass.isSelected() ? (char) 0 : '*';
        password.setEchoChar(echoChar);
    }//GEN-LAST:event_showPassActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JComboBox<String> type;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
