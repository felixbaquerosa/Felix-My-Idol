package UserDSB;

import Config.DBConnector;
import Config.Session;
import LoginDSB.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.*;
import static javax.xml.bind.DatatypeConverter.parseInteger;
import net.proteanit.sql.DbUtils;

public class UserDashboard extends javax.swing.JFrame {

    public File selectedFile;
    public String path2 = null;
    public String destination = "";
    public String oldPath;
    public String path;

    public UserDashboard() {
        initComponents();
        displayProducts();
    }

    private void displayProducts() {
        try {
            ResultSet rs = new DBConnector().getData("select p_id, p_name, p_price, p_stocks, p_status from products");
            products.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    public void placeOrder() throws NoSuchAlgorithmException {
        try {
            Session sess = Session.getInstance();
            String adds = address.getText().trim();
            String mt = method.getSelectedItem() == null ? "" : method.getSelectedItem().toString().trim();

            if (name.getText().isEmpty() || price.getText().isEmpty() || stocks.getText().isEmpty() || status.getText().isEmpty()
                    || adds.isEmpty()) {
                JOptionPane.showMessageDialog(null, "PLEASE FILL ALL THE FIELDS!");
            } else {
                int quantityValue = (int) quantity.getValue();
                int stocksInt = Integer.parseInt(stocks.getText());
                int priceInt = Integer.parseInt(price.getText());
                double totalPrice = quantityValue * priceInt;
                double totalCost = stocksInt * priceInt;
                double totalProfit = totalPrice - totalCost;

                System.out.println("Profit: " + totalProfit);

                DBConnector cn = new DBConnector();

                int newStocks = stocksInt - quantityValue;
                cn.updateData("update orders set o_stocks = '" + newStocks + "' where o_id = '" + id.getText() + "'");

                cn.insertData("insert into orders (o_cname,o_name, o_price, o_stocks, o_status, o_method, o_quantity, o_address, total_profit, o_approve) "
                        + "values ('" + sess.getUsername() + "','" + name.getText() + "', '" + price.getText() + "', '"
                        + stocks.getText() + "', '" + status.getText() + "', '" + mt + "', '" + quantityValue + "',"
                        + "'" + adds + "', '" + totalProfit + "', 'False')");

                JOptionPane.showMessageDialog(this, "PRODUCT CREATED SUCCESSFULLY!");

                jTabbedPane1.setSelectedIndex(0);

                address.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error creating product!");
            System.out.println(ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid stocks value!");
            System.out.println(ex.getMessage());
        }
    }

    public static int getHeightFromWidth(String imagePath, int desiredWidth) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();

            int newHeight = (int) ((double) desiredWidth / originalWidth * originalHeight);

            return newHeight;
        } catch (IOException ex) {
            System.out.println("No image found!");
        }

        return -1;
    }

    private ImageIcon ResizeImage(String ImagePath, byte[] pic, JLabel label) {
        ImageIcon MyImage = null;
        if (ImagePath != null) {
            MyImage = new ImageIcon(ImagePath);
        } else {
            MyImage = new ImageIcon(pic);
        }

        int newHeight = getHeightFromWidth(ImagePath, label.getWidth());

        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), newHeight, Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        products = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        adminName = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        adminName1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        price = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        status = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jButton28 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        productImage = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        stocks = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        address = new javax.swing.JTextField();
        method = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        quantity = new javax.swing.JSpinner();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1098, 699));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1098, 699));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        products.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(products);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 1030, 440));

        jButton2.setText("MY ACCOUNT");
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 620, 120, 30));

        jLabel4.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel4.setText("USERS DASHBOARD");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 240, 30));

        adminName.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        adminName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/boy.png"))); // NOI18N
        adminName.setText("USERS NAME");
        jPanel1.add(adminName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jButton1.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton1.setText("LOGOUT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 20, 100, -1));

        jLabel6.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel6.setText("ADMINS DASHBOARD");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 470, 150));

        jButton3.setText("PLACE ORDER");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 620, 120, 30));

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel5.setText("USERS DASHBOARD");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 240, 30));

        adminName1.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        adminName1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/boy.png"))); // NOI18N
        adminName1.setText("USERS NAME");
        jPanel2.add(adminName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jButton5.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton5.setText("LOGOUT");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 20, 100, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Product Name");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 200, 230, -1));

        name.setEditable(false);
        name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameActionPerformed(evt);
            }
        });
        jPanel2.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 220, 230, 30));

        price.setEditable(false);
        price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(price, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 220, 230, 30));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Product Price");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 200, 230, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Product Stocks");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 270, 230, -1));

        status.setEditable(false);
        status.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 290, 230, 30));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Product Status");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 270, 230, -1));

        id.setEditable(false);
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });
        jPanel2.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 470, 30));

        jButton28.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton28.setText("CONFIRM");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton28, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 480, 110, -1));

        jButton27.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton27.setText("BACK");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 480, 110, -1));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(productImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 270));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 470, 290));

        jLabel7.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel7.setText("ADMINS DASHBOARD");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 470, 150));

        stocks.setEditable(false);
        stocks.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(stocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 290, 230, 30));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Payment Method");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 230, -1));

        address.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(address, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 430, 470, 30));

        method.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CASH ON DELIVERY" }));
        jPanel2.add(method, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 360, 230, 30));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Quantity");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 340, 230, -1));
        jPanel2.add(quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 360, 230, 30));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Address");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 410, 470, -1));

        jTabbedPane1.addTab("tab1", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 700));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new LoginDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameActionPerformed

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        try {
            placeOrder();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        displayProducts();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void productsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsMouseClicked
        int rowIndex = products.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(null, "PLEASE SELECT AN INDEX!");
        } else {
            try {
                TableModel tbl = products.getModel();
                ResultSet rs = new DBConnector().getData("select * from products where p_id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                if (rs.next()) {
                    id.setText("" + rs.getString("p_id"));
                    name.setText("" + rs.getString("p_name"));
                    price.setText("" + rs.getString("p_price"));
                    stocks.setText("" + rs.getString("p_stocks"));
                    status.setText("" + rs.getString("p_status"));
                    productImage.setIcon(ResizeImage(rs.getString("p_image"), null, productImage));
                    oldPath = rs.getString("p_image");
                    path = rs.getString("p_image");

                }
            } catch (SQLException er) {
                System.out.println("ERROR: " + er.getMessage());
            }
        }
    }//GEN-LAST:event_productsMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address;
    private javax.swing.JLabel adminName;
    private javax.swing.JLabel adminName1;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> method;
    private javax.swing.JTextField name;
    private javax.swing.JTextField price;
    private javax.swing.JLabel productImage;
    private javax.swing.JTable products;
    private javax.swing.JSpinner quantity;
    private javax.swing.JTextField status;
    private javax.swing.JTextField stocks;
    // End of variables declaration//GEN-END:variables
}
