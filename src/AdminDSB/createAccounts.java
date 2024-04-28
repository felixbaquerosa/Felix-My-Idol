package AdminDSB;

import Config.DBConnector;
import Config.passwordHashing;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class createAccounts extends javax.swing.JFrame {

    public createAccounts() {
        initComponents();
    }

    private String xemail, xusername;

    private boolean duplicateChecker() throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from inventory where email = '" + email.getText() + "' or username = '" + username.getText() + "'");
        if (rs.next()) {
            xemail = rs.getString("email");
            if (xemail.equals(email.getText())) {
                errorMessage("EMAIL HAS BEEN USED!");
            }
            xusername = rs.getString("username");
            if (xusername.equals(username.getText())) {
                errorMessage("USERNAME HAS BEEN USED!");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        contact = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        type = new javax.swing.JComboBox<>();
        status = new javax.swing.JComboBox<>();
        showPass = new javax.swing.JCheckBox();
        password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(320, 480));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel15.setText("SEARCH");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 90, 90, -1));

        email.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL ");
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });
        jPanel1.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 240, -1));

        username.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setText("USERNAME");
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFocusGained(evt);
            }
        });
        jPanel1.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 240, -1));

        contact.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        contact.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact.setText("CONTACT#\n");
        contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFocusGained(evt);
            }
        });
        jPanel1.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 240, -1));

        jLabel16.setFont(new java.awt.Font("Yu Gothic", 1, 10)); // NOI18N
        jLabel16.setText("PLEASE DOUBLE CHECK DETAILS UPON CONFIRMING!");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 290, 40));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-sign-up-30.png"))); // NOI18N
        jLabel17.setText("SIGN UP");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 130, 40));

        jButton1.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton1.setText("CANCEL");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 80, -1));

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("CONFIRM");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 430, -1, -1));

        type.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USER" }));
        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel2.add(type, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 240, -1));

        status.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE", "PENDING" }));
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
            }
        });
        jPanel2.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 240, -1));

        showPass.setBackground(new java.awt.Color(255, 255, 255));
        showPass.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        showPass.setText("SHOW PASSWORD");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });
        jPanel2.add(showPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 130, -1));

        password.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD");
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel2.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 240, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 480));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 490));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
    }//GEN-LAST:event_typeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new AdminDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if (duplicateChecker()) {
            } else if (!validationChecker()) {
            } else {
                String pass = passwordHashing.hashPassword(password.getText());
                new DBConnector().insertData("insert into inventory (email,username,password,contact,type,status) "
                        + "values ('" + email.getText() + "','" + username.getText() + "', '" + pass + "'"
                        + ",'" + contact.getText() + "','" + type.getSelectedItem() + "', '" + status.getSelectedItem() + "')");

                JOptionPane.showMessageDialog(this, "REGISTRATION SUCCESSFULL!", "SUCCESS", INFORMATION_MESSAGE);

                new AdminDashboard().setVisible(true);
                dispose();
            }
        } catch (SQLException er) {
            System.out.println("Eror: " + er.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed

    }//GEN-LAST:event_statusActionPerformed

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        char echoChar = showPass.isSelected() ? (char) 0 : '*';
        password.setEchoChar(echoChar);
    }//GEN-LAST:event_showPassActionPerformed

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new createAccounts().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField contact;
    private javax.swing.JTextField email;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JPasswordField password;
    private javax.swing.JCheckBox showPass;
    public javax.swing.JComboBox<String> status;
    private javax.swing.JComboBox<String> type;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
