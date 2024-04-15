/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdminDSB;

import static AdminDSB.AdminDashboard.data;
import static AdminDSB.AdminDashboard.updateData;
import Config.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class pendingAccounts extends javax.swing.JFrame {

    public pendingAccounts() {
        initComponents();
        pendingData();
    }

    public void pendingData() {
        try {
            String query = "SELECT * FROM inventory WHERE status = 'PENDING'";
            ResultSet rs = new DBConnector().getData(query);
            while (rs.next()) {
                String xid = String.valueOf(rs.getInt("id"));
                String xemail = rs.getString("email");
                String xcontact = rs.getString("contact");
                String xusername = rs.getString("user");
                String xpassword = rs.getString("pass");
                String xtype = rs.getString("type");
                String xstatus = rs.getString("status");

                String tbData[] = {xid, xemail, xcontact, xusername, xpassword, xtype, xstatus};
                DefaultTableModel tblModel = (DefaultTableModel) pendings.getModel();
                tblModel.addRow(tbData);
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        }
    }

    public void acceptAccount() {
        try (Connection cn = new DBConnector().getConnection()) {
            DefaultTableModel tblModel = (DefaultTableModel) pendings.getModel();

            if (pendings.getSelectedRowCount() == 1) {
                int selectedRowIndex = pendings.getSelectedRow();
                String accountId = (String) pendings.getValueAt(selectedRowIndex, 0);

                PreparedStatement update = cn.prepareStatement("UPDATE inventory SET status = ? WHERE id = ?");
                update.setString(1, "ACTIVE");
                update.setString(2, accountId);

                int rowsAffected = update.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "ACCOUNT HAS BEEN APPROVED SUCCESSFULLY!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
                    tblModel.setRowCount(0);
                    pendingData();
                } else {
                    JOptionPane.showMessageDialog(null, "FAILED TO APPROVED!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                if (pendings.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "TABLE IS EMPTY!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "PLEASE SELECT A SINGLE ROW TO UPDATE!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        }
    }

    public void declineAccount() {
        try (Connection cn = new DBConnector().getConnection()) {
            DefaultTableModel tblModel = (DefaultTableModel) pendings.getModel();

            if (pendings.getSelectedRowCount() == 1) {
                int selectedRowIndex = pendings.getSelectedRow();
                String accountId = (String) pendings.getValueAt(selectedRowIndex, 0);

                PreparedStatement update = cn.prepareStatement("UPDATE inventory SET status = ? WHERE id = ?");
                update.setString(1, "DECLINED");
                update.setString(2, accountId);

                int rowsAffected = update.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "ACCOUNT HAS BEEN DECLINED!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
                    tblModel.setRowCount(0);
                    pendingData();
                } else {
                    JOptionPane.showMessageDialog(null, "FAILED TO DECLINE!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                if (pendings.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "TABLE IS EMPTY!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "PLEASE SELECT A SINGLE ROW TO UPDATE!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        declineBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        acceptBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pendings = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(750, 520));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(749, 572));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        declineBtn.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        declineBtn.setText("DECLINE");
        declineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineBtnActionPerformed(evt);
            }
        });
        jPanel1.add(declineBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(651, 120, 90, -1));

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("BACK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 470, 90, -1));

        acceptBtn.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        acceptBtn.setText("ACCEPT");
        acceptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptBtnActionPerformed(evt);
            }
        });
        jPanel1.add(acceptBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, 90, 30));

        pendings.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        pendings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "EMAIL", "CONTACT", "USERNAMES", "PASSWORDS", "TYPE", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pendings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendingsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(pendings);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 620, 420));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-pending.gif"))); // NOI18N
        jLabel17.setText("PENDING ACCOUNTS");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 260, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 520));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 520));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pendingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingsMouseClicked

    }//GEN-LAST:event_pendingsMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        new AdminDashboard().setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void acceptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptBtnActionPerformed
        acceptAccount();
    }//GEN-LAST:event_acceptBtnActionPerformed

    private void declineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineBtnActionPerformed
        declineAccount();
    }//GEN-LAST:event_declineBtnActionPerformed

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
            java.util.logging.Logger.getLogger(pendingAccounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pendingAccounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pendingAccounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pendingAccounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pendingAccounts().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptBtn;
    private javax.swing.JButton declineBtn;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable pendings;
    // End of variables declaration//GEN-END:variables
}
