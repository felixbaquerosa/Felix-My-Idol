package AdminDSB;

import Config.*;
import LoginDSB.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.*;
import javax.swing.table.*;

public class AdminDashboard extends javax.swing.JFrame {
    
    DefaultTableModel adminTable;
    
    public AdminDashboard() {
        initComponents();
        updateData();
    }
    
    public AdminDashboard(String adminsName) {
        initComponents();
        adminName.setText(adminsName);
        updateData();
    }
    
    public static void updateData() {
        try {
            ResultSet rs = new DBConnector().getData("SELECT * FROM inventory WHERE status = 'ACTIVE' OR status = 'INACTIVE'");
            while (rs.next()) {
                String xid = String.valueOf(rs.getInt("id"));
                String xemail = rs.getString("email");
                String xcontact = rs.getString("contact");
                String xusername = rs.getString("user");
                String xpassword = rs.getString("pass");
                String xtype = rs.getString("type");
                String xstatus = rs.getString("status");
                
                String tbData[] = {xid, xemail, xcontact, xusername, xpassword, xtype, xstatus};
                DefaultTableModel tblModel = (DefaultTableModel) data.getModel();
                tblModel.addRow(tbData);
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        }
        
    }
    
    public static boolean deleteAccount() {
        try (Connection cn = new DBConnector().getConnection()) {
            DefaultTableModel tblModel = (DefaultTableModel) data.getModel();
            
            if (data.getSelectedRowCount() == 1) {
                int selectedRow = data.getSelectedRow();
                int idToDelete = Integer.parseInt(tblModel.getValueAt(selectedRow, 0).toString());
                
                tblModel.removeRow(selectedRow);
                
                PreparedStatement delete = cn.prepareStatement("DELETE FROM inventory WHERE id = ?");
                delete.setInt(1, idToDelete);
                int rowsDeleted = delete.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "ACCOUNT SUCCESSFULLY DELETED!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
                return rowsDeleted > 0;
            } else {
                if (data.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "TABLE IS EMPTY!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "PLEASE SELECT A SINGLE ROW TO DELETE!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
            return false;
        }
    }
    
    public void retrieveData() {
        int selectedViewRowIndex = data.getSelectedRow();
        
        if (selectedViewRowIndex != -1) {
            DefaultTableModel tblModel = (DefaultTableModel) data.getModel();
            int selectedModelIndex = data.convertRowIndexToModel(selectedViewRowIndex);
            
            String tbl_id = tblModel.getValueAt(selectedModelIndex, 0).toString();
            String tbl_email = tblModel.getValueAt(selectedModelIndex, 1).toString();
            String tbl_contact = tblModel.getValueAt(selectedModelIndex, 2).toString();
            String tbl_username = tblModel.getValueAt(selectedModelIndex, 3).toString();
            String tbl_password = tblModel.getValueAt(selectedModelIndex, 4).toString();
            String tbl_type = tblModel.getValueAt(selectedModelIndex, 5).toString();
            String tbl_status = tblModel.getValueAt(selectedModelIndex, 6).toString();
            
            id.setText(tbl_id);
            email.setText(tbl_email);
            contact.setText(tbl_contact);
            username.setText(tbl_username);
            password.setText(tbl_password);
            type.setSelectedItem(tbl_type);
            status.setSelectedItem(tbl_status);
        }
    }
    
    public void updateAccount() {
        try (Connection cn = new DBConnector().getConnection()) {
            
            DefaultTableModel tblModel = (DefaultTableModel) data.getModel();
            
            if (data.getSelectedRowCount() == 1) {
                
                String xid = id.getText();
                String xemail = email.getText();
                String xcontact = contact.getText();
                String xusername = username.getText();
                String xpassword = password.getText();
                String xtype = (String) type.getSelectedItem();
                String xstatus = (String) status.getSelectedItem();
                
                PreparedStatement update = cn.prepareStatement("UPDATE inventory SET email = ?, contact = ?, user = ?, pass = ?, type = ?, status = ? WHERE id = ?");
                
                update.setString(1, xemail);
                update.setString(2, xcontact);
                update.setString(3, xusername);
                update.setString(4, xpassword);
                update.setString(5, xtype);
                update.setString(6, xstatus);
                update.setString(7, xid);
                
                int rowsAffected = update.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "ACCOUNT SUCCESSFULLY UPDATED!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
                    DefaultTableModel model = (DefaultTableModel) AdminDashboard.data.getModel();
                    model.setRowCount(0);
                    updateData();
                } else {
                    JOptionPane.showMessageDialog(null, "FAILED TO UPDATE!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
                
            } else {
                if (data.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "TABLE IS EMPTY!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "PLEASE SELECT A SINGLE ROW TO UPDATE!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        }
    }
    
    public void search(String info) {
        adminTable = (DefaultTableModel) data.getModel();
        TableRowSorter<DefaultTableModel> sort = new TableRowSorter<>(adminTable);
        data.setRowSorter(sort);
        sort.setRowFilter(RowFilter.regexFilter(info));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        data = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        adminName = new javax.swing.JLabel();
        serts = new javax.swing.JTextField();
        id = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        contact = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        password = new javax.swing.JTextField();
        type = new javax.swing.JComboBox<>();
        status = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1102, 657));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1102, 657));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Yu Gothic", 1, 15)); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 70, -1, -1));

        data.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "EMAIL", "CONTACT", "USERNAMES", "PASSWORDS", "TYPE", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        data.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(data);
        if (data.getColumnModel().getColumnCount() > 0) {
            data.getColumnModel().getColumn(0).setResizable(false);
            data.getColumnModel().getColumn(1).setResizable(false);
            data.getColumnModel().getColumn(2).setResizable(false);
            data.getColumnModel().getColumn(3).setResizable(false);
            data.getColumnModel().getColumn(4).setResizable(false);
            data.getColumnModel().getColumn(5).setResizable(false);
            data.getColumnModel().getColumn(6).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 640, 470));

        jLabel4.setText("jLabel4");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        adminName.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        adminName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-admin-80.png"))); // NOI18N
        adminName.setText("ADMINS NAME");
        jPanel2.add(adminName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, -1));

        serts.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        serts.setText("SEARCH");
        serts.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sertsFocusGained(evt);
            }
        });
        serts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sertsMouseClicked(evt);
            }
        });
        serts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sertsActionPerformed(evt);
            }
        });
        serts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sertsKeyReleased(evt);
            }
        });
        jPanel2.add(serts, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 160, 240, 30));

        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText("ID");
        id.setEnabled(false);
        jPanel2.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 210, 60, 30));

        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL");
        email.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailFocusGained(evt);
            }
        });
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });
        jPanel2.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 210, 240, 30));

        contact.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact.setText("CONTACT");
        contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFocusGained(evt);
            }
        });
        jPanel2.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 250, 240, 30));

        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setText("USERNAME");
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFocusGained(evt);
            }
        });
        jPanel2.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 290, 240, 30));

        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD");
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel2.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 330, 240, 30));

        type.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USER" }));
        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel2.add(type, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 370, 110, 30));

        status.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE" }));
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
            }
        });
        jPanel2.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 370, 110, 30));

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("CREATE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 460, 110, -1));

        jButton4.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton4.setText("DELETE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 460, 110, -1));

        jButton6.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton6.setText("PENDING");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 510, 110, -1));

        jButton3.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton3.setText("UPDATE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 510, 110, -1));

        jButton1.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton1.setText("LOGOUT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 570, 110, -1));

        jLabel7.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel7.setText("ADMINS DASHBOARD");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 210, 30));

        jLabel6.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel6.setText("ADMINS DASHBOARD");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 470, 150));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 660));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new LoginDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new createAccount().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        updateAccount();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        deleteAccount();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        new pendingAccounts().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void dataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataMouseClicked
        retrieveData();
    }//GEN-LAST:event_dataMouseClicked

    private void sertsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sertsActionPerformed

    }//GEN-LAST:event_sertsActionPerformed

    private void sertsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sertsKeyReleased
        String searchString = serts.getText();
        search(searchString);
    }//GEN-LAST:event_sertsKeyReleased

    private void typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_typeActionPerformed

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusActionPerformed

    private void sertsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sertsFocusGained
    }//GEN-LAST:event_sertsFocusGained

    private void sertsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sertsMouseClicked
        serts.setText("");
    }//GEN-LAST:event_sertsMouseClicked

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
    }//GEN-LAST:event_emailMouseClicked

    private void emailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFocusGained
        email.setText("");
    }//GEN-LAST:event_emailFocusGained

    private void contactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusGained
        contact.setText("");
    }//GEN-LAST:event_contactFocusGained

    private void usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusGained
        username.setText("");
    }//GEN-LAST:event_usernameFocusGained

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminName;
    private javax.swing.JTextField contact;
    public static javax.swing.JTable data;
    private javax.swing.JTextField email;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField password;
    private javax.swing.JTextField serts;
    private javax.swing.JComboBox<String> status;
    private javax.swing.JComboBox<String> type;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
