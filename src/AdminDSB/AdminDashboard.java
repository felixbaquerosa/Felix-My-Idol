package AdminDSB;

import static AdminDSB.pendingAccounts.pendings;
import Config.*;
import LoginDSB.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import net.proteanit.sql.DbUtils;

public class AdminDashboard extends javax.swing.JFrame {

    public File selectedFile;
    public String path2 = null;
    public String destination = "";
    public String oldPath;
    public String path;

    public AdminDashboard() {
        initComponents();
        displayUsers();
        displayProducts();
        pendingorders();
        activeOrders();
        jButton21.setEnabled(false);
    }

    private void activeOrders() {
        try {
            ResultSet rs = new DBConnector().getData("select * from orders where o_approve = 'True'");
            orders1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private void pendingorders() {
        try {
            ResultSet rs = new DBConnector().getData("select * from orders where o_approve = 'False'");
            pendingOrders.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private void displayUsers() {
        try {
            Session sess = Session.getInstance();
            ResultSet rs = new DBConnector().getData("select id,email,username,contact,type,status from inventory where status in ('active', 'inactive') and id != '" + sess.getId() + "'");
            usersTB.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private void displayProducts() {
        try {
            ResultSet rs = new DBConnector().getData("select * from products");
            productsTB.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    public void approveOrder() throws NoSuchAlgorithmException {
        int rowIndex = pendingOrders.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(null, "PLEASE SELECT AN INDEX!");
        } else {
            try {
                DBConnector cn = new DBConnector();
                TableModel tbl = pendingOrders.getModel();

                ResultSet rs = cn.getData("select * from orders where o_id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                if (rs.next()) {
                    int quantityValue = Integer.parseInt(rs.getString("o_quantity"));
                    int stocksValue = Integer.parseInt(rs.getString("o_stocks"));
                    int newStocks = stocksValue - quantityValue;
                    cn.updateData("update orders set o_stocks = '" + newStocks + "' , o_approve = 'True' where o_id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                    cn.updateData("update products set p_stocks = '" + newStocks + "' where p_id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                }

                JOptionPane.showMessageDialog(this, "ORDER APPROVED SUCCESSFULLY!");
                displayUsers();
                displayProducts();
                pendingorders();
                activeOrders();
                jTabbedPane1.setSelectedIndex(1);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "ORDER APPROVED FAILED!");
                System.out.println(ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "INVALID STOCKS VALUE!");
                System.out.println(ex.getMessage());
            }
        }
    }

    public void doneOrder() throws NoSuchAlgorithmException {
        int rowIndex = orders1.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(null, "PLEASE SELECT AN INDEX!");
        } else {
            try {
                DBConnector cn = new DBConnector();
                TableModel tbl = orders1.getModel();

                ResultSet rs = cn.getData("select * from orders where o_id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                if (rs.next()) {
                    cn.updateData("update orders set o_approve = 'Delivered' where o_id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                }

                JOptionPane.showMessageDialog(this, "DELIVERY SUCCESSFULLY!");
                displayUsers();
                displayProducts();
                pendingorders();
                activeOrders();
                jTabbedPane1.setSelectedIndex(1);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DELIVERY FAILED!");
                System.out.println(ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "DELIVERY FAILED!");
                System.out.println(ex.getMessage());
            }
        }
    }

    public void deleteProduct() throws NoSuchAlgorithmException, SQLException {
        int confirmation = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT?", "CONFIRMATION", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            DBConnector cn = new DBConnector();
            String query = "DELETE FROM products WHERE p_id = '" + id.getText() + "'";
            try (PreparedStatement pstmt = cn.getConnection().prepareStatement(query)) {
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "PRODUCT DELETED SUCCESSFULLY!");
                displayUsers();
                displayProducts();
                pendingorders();
                activeOrders();
                jTabbedPane1.setSelectedIndex(1);
            }
        }
    }

    public void addProduct() throws NoSuchAlgorithmException {
        try {
            String xpname = pname1.getText().trim();
            String xpprice = pprice1.getText().trim();
            String xpstocks = pstocks1.getText().trim();
            String xpstatus = pstatus1.getSelectedItem() == null ? "" : pstatus1.getSelectedItem().toString().trim();

            if (xpname.isEmpty() || xpprice.isEmpty() || xpstocks.isEmpty() || xpstatus.isEmpty()) {
                JOptionPane.showMessageDialog(null, "PLEASE FILL ALL THE FIELDS!");
            } else if (destination == null || destination.isEmpty()) {
                JOptionPane.showMessageDialog(null, "PLEASE INSERT AN IMAGE FIRST!");
            } else {
                DBConnector cn = new DBConnector();
                cn.insertData("insert into products (p_name, p_price, p_stocks, p_status, p_image) "
                        + "values ('" + xpname + "', '" + xpprice + "', '"
                        + xpstocks + "', '" + xpstatus + "', '" + destination + "')");

                if (destination != null && path != null) {
                    Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);

                    JOptionPane.showMessageDialog(this, "PRODUCT CREATED SUCCESSFULLY!");
                    displayUsers();
                    displayProducts();
                    pendingorders();
                    activeOrders();
                    jTabbedPane1.setSelectedIndex(1);

                    pname1.setText("");
                    pprice1.setText("");
                    pstocks1.setText("");
                    icon1.setIcon(null);
                } else {
                    JOptionPane.showMessageDialog(null, "PLEASE INSERT AN IMAGE FIRST!");
                }
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error creating product!");
            System.out.println(ex.getMessage());
        }
    }

    public void updateProduct() throws NoSuchAlgorithmException {
        try {
            String xpname1 = pn.getText().trim();
            String xpprice1 = pp.getText().trim();
            String xpstocks1 = ps.getText().trim();
            String xpstatus1 = pstats.getSelectedItem() == null ? "" : pstats.getSelectedItem().toString().trim();

            if (xpname1.isEmpty() || xpprice1.isEmpty() || xpstocks1.isEmpty() || xpstatus1.isEmpty()) {
                JOptionPane.showMessageDialog(null, "PLEASE FILL ALL THE FIELDS!");
            } else if (destination == null || destination.isEmpty()) {
                JOptionPane.showMessageDialog(null, "PLEASE INSERT AN IMAGE FIRST!");
            } else {
                DBConnector cn = new DBConnector();
                cn.updateData("update products set p_name = '" + xpname1 + "', p_price = '" + xpprice1 + "',p_stocks='" + xpstocks1 + "', "
                        + "p_status='" + xpstatus1 + "', p_image= '" + destination + "' where p_id = '" + id.getText() + "'");

                if (destination != null && path != null) {
                    Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);

                    JOptionPane.showMessageDialog(this, "PRODUCT CREATED SUCCESSFULLY!");
                    displayUsers();
                    displayProducts();
                    pendingorders();
                    activeOrders();
                    jTabbedPane1.setSelectedIndex(1);

                    pn.setText("");
                    pp.setText("");
                    ps.setText("");
                    icon2.setIcon(null);
                } else {
                    JOptionPane.showMessageDialog(null, "PLEASE INSERT AN IMAGE FIRST!");
                }
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error creating product!");
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

    private int FileExistenceChecker(String path) {
        File file = new File(path);
        String fileName = file.getName();

        Path filePath = Paths.get("src/ProductsImage", fileName);
        boolean fileExists = Files.exists(filePath);

        if (fileExists) {
            return 1;
        } else {
            return 0;
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        usersTB = new javax.swing.JTable();
        aname = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        productsTB = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        aname2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        aname3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        pname1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pprice1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        pstocks1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        pstatus1 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        icon1 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        aname4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        remove = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        pp = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        ps = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        pstats = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        icon2 = new javax.swing.JLabel();
        select = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        pn = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        aname5 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pendingOrders = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        jButton32 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        aname6 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        orders1 = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jButton35 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1098, 699));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1098, 699));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usersTB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(usersTB);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 1040, 450));

        aname.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        aname.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-admin-80.png"))); // NOI18N
        aname.setText("ADMINS NAME");
        jPanel2.add(aname, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, -1));

        jButton1.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton1.setText("LOGOUT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 630, 110, -1));

        jLabel7.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel7.setText("ADMINS DASHBOARD");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 210, 30));

        jButton5.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton5.setText("MY ACCOUNT");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 630, 110, -1));

        jButton9.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton9.setText("EDIT");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 630, 110, -1));

        jButton7.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton7.setText("PENDING");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 630, 110, -1));

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("CREATE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 630, 110, -1));

        jButton13.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton13.setText("MANAGE PRODUCTS");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 630, 150, -1));

        jButton11.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton11.setText("PRINT");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 130, 130, -1));

        jLabel6.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel6.setText("ADMINS DASHBOARD");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 470, 150));

        jTabbedPane1.addTab("tab1", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        productsTB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        productsTB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsTBMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(productsTB);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 1040, 470));

        jButton6.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton6.setText("PRINT");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 120, -1));

        aname2.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        aname2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-admin-80.png"))); // NOI18N
        aname2.setText("ADMINS NAME");
        jPanel3.add(aname2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, -1));

        jButton3.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton3.setText("ORDERS");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 140, 120, -1));

        jLabel8.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel8.setText("ADMINS DASHBOARD");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 210, 30));

        jButton10.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton10.setText("MANAGE USERS");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 140, 130, -1));

        jButton16.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton16.setText("PENDING ORDERS");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 140, 140, -1));

        jButton17.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton17.setText("ADD");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 110, -1));

        jButton4.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton4.setText("UPDATE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 110, 30));

        jLabel9.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel9.setText("ADMINS DASHBOARD");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 470, 150));

        jTabbedPane1.addTab("tab1", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Product Name");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 490, 230, -1));

        aname3.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        aname3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-admin-80.png"))); // NOI18N
        aname3.setText("ADMINS NAME");
        jPanel4.add(aname3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, -1));

        jLabel10.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel10.setText("ADMINS DASHBOARD");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 210, 30));

        jButton21.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton21.setText("REMOVE");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 430, 170, -1));

        jLabel11.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel11.setText("ADMINS DASHBOARD");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 470, 150));

        pname1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pname1ActionPerformed(evt);
            }
        });
        jPanel4.add(pname1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 510, 230, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Product Price");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 490, 230, -1));

        pprice1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(pprice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 510, 230, 30));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Product Stocks");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, 230, -1));

        pstocks1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(pstocks1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 580, 230, 30));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Product Status");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 560, 230, -1));

        pstatus1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AVAILABLE", "NOT AVAILABLE" }));
        jPanel4.add(pstatus1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 580, 230, 30));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.add(icon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 200));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 470, 220));

        jButton24.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton24.setText("SELECT");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 170, -1));

        jButton22.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton22.setText("BACK");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton22, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 630, 110, -1));

        jButton25.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton25.setText("ADD");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton25, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 630, 110, -1));

        jTabbedPane1.addTab("tab1", jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Product Name");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 490, 230, -1));

        aname4.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        aname4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-admin-80.png"))); // NOI18N
        aname4.setText("ADMINS NAME");
        jPanel6.add(aname4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, -1));

        jLabel16.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel16.setText("ADMINS DASHBOARD");
        jPanel6.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 210, 30));

        remove.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        remove.setText("REMOVE");
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jPanel6.add(remove, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 430, 170, -1));

        jLabel17.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel17.setText("ADMINS DASHBOARD");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 470, 150));

        id.setEditable(false);
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });
        jPanel6.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 430, 110, 30));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Product Price");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 490, 230, -1));

        pp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel6.add(pp, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 510, 230, 30));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Product Stocks");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, 230, -1));

        ps.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel6.add(ps, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 580, 230, 30));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Product Status");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 560, 230, -1));

        pstats.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AVAILABLE", "NOT AVAILABLE" }));
        jPanel6.add(pstats, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 580, 230, 30));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(icon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 200));

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 470, 220));

        select.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        select.setText("SELECT");
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel6.add(select, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 170, -1));

        jButton27.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton27.setText("BACK");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 630, 110, -1));

        jButton28.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton28.setText("UPDATE");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton28, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 630, 110, -1));

        pn.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pnActionPerformed(evt);
            }
        });
        jPanel6.add(pn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 510, 230, 30));

        jButton29.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton29.setText("DELETE");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton29, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 630, 110, -1));

        jTabbedPane1.addTab("tab1", jPanel6);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        aname5.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        aname5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-admin-80.png"))); // NOI18N
        aname5.setText("ADMINS NAME");
        jPanel8.add(aname5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, -1));

        jLabel22.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel22.setText("ADMINS DASHBOARD");
        jPanel8.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 210, 30));

        jButton30.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton30.setText("BACK");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton30, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 600, 110, -1));

        jButton31.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton31.setText("APPROVE");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 110, -1));

        pendingOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(pendingOrders);

        jPanel8.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 1030, 440));

        jLabel23.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel23.setText("ADMINS DASHBOARD");
        jPanel8.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 470, 150));

        jButton32.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton32.setText("PRINT");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton32, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 110, 110, -1));

        jTabbedPane1.addTab("tab1", jPanel8);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        aname6.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        aname6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-admin-80.png"))); // NOI18N
        aname6.setText("ADMINS NAME");
        jPanel9.add(aname6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, -1));

        jLabel24.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel24.setText("ADMINS DASHBOARD");
        jPanel9.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 210, 30));

        jButton33.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton33.setText("DELIVERED");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 110, -1));

        orders1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(orders1);

        jPanel9.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 1030, 440));

        jLabel25.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/samot.png"))); // NOI18N
        jLabel25.setText("ADMINS DASHBOARD");
        jPanel9.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 470, 150));

        jButton35.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton35.setText("BACK");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton35, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 600, 110, -1));

        jButton34.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton34.setText("PRINT");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton34, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 110, 110, -1));

        jTabbedPane1.addTab("tab1", jPanel9);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 700));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        Session sess = Session.getInstance();
        aname.setText("" + sess.getUsername());
        aname2.setText("" + sess.getUsername());
        displayUsers();
        displayProducts();
    }//GEN-LAST:event_formWindowActivated

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        try {
            doneOrder();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        try {
            approveOrder();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        try {
            deleteProduct();
        } catch (NoSuchAlgorithmException | SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void pnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        try {
            updateProduct();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        displayProducts();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                selectedFile = fileChooser.getSelectedFile();
                destination = "src/ProductsImage/" + selectedFile.getName();
                path = selectedFile.getAbsolutePath();

                if (FileExistenceChecker(path) == 1) {
                    JOptionPane.showMessageDialog(null, "File Already Exist, Rename or Choose another!");
                    destination = "";
                    path = "";
                } else {
                    icon2.setIcon(ResizeImage(path, null, icon2));
                    remove.setEnabled(true);
                    select.setEnabled(false);
                }
            } catch (Exception ex) {
                System.out.println("File Error!");
            }
        }
    }//GEN-LAST:event_selectActionPerformed

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        destination = "";
        icon2.setIcon(null);
        path = "";
        select.setEnabled(true);
        remove.setEnabled(false);
    }//GEN-LAST:event_removeActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        try {
            addProduct();
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                selectedFile = fileChooser.getSelectedFile();
                destination = "src/ProductsImage/" + selectedFile.getName();
                path = selectedFile.getAbsolutePath();

                if (FileExistenceChecker(path) == 1) {
                    JOptionPane.showMessageDialog(null, "File Already Exist, Rename or Choose another!");
                    destination = "";
                    path = "";
                } else {
                    icon1.setIcon(ResizeImage(path, null, icon1));
                    remove.setEnabled(true);
                    select.setEnabled(false);
                }
            } catch (Exception ex) {
                System.out.println("File Error!");
            }
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void pname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pname1ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        destination = "";
        icon1.setIcon(null);
        path = "";
        jButton24.setEnabled(true);
        jButton21.setEnabled(false);
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        MessageFormat header = new MessageFormat("Total Accounts Registered Reports");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            productsTB.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException er) {
            System.out.println("" + er.getMessage());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void productsTBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTBMouseClicked
        int rowIndex = productsTB.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(null, "PLEASE SELECT AN INDEX!");
        } else {
            try {
                TableModel tbl = productsTB.getModel();
                ResultSet rs = new DBConnector().getData("select * from products where p_id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                if (rs.next()) {
                    id.setText("" + rs.getString("p_id"));
                    pn.setText("" + rs.getString("p_name"));
                    pp.setText("" + rs.getString("p_price"));
                    ps.setText("" + rs.getString("p_stocks"));
                    pstats.setSelectedItem("" + rs.getString("p_status"));
                    icon2.setIcon(ResizeImage(rs.getString("p_image"), null, icon2));
                    oldPath = rs.getString("p_image");
                    path = rs.getString("p_image");

                    if (rs.getString("p_image") != null) {
                        select.setEnabled(false);
                        remove.setEnabled(true);
                    } else {
                        select.setEnabled(true);
                        remove.setEnabled(false);
                    }

                }
            } catch (SQLException er) {
                System.out.println("ERROR: " + er.getMessage());
            }
        }
    }//GEN-LAST:event_productsTBMouseClicked

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        MessageFormat header = new MessageFormat("Total Accounts Registered Reports");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            usersTB.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException er) {
            System.out.println("" + er.getMessage());
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        select.setEnabled(false);
        remove.setEnabled(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new createAccounts().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        new pendingAccounts().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new myAccount().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LoginDashboard ld = new LoginDashboard();
        ld.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        MessageFormat header = new MessageFormat("Total Accounts Registered Reports");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            pendingOrders.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException er) {
            System.out.println("" + er.getMessage());
        }
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        MessageFormat header = new MessageFormat("Total Accounts Registered Reports");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            orders1.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException er) {
            System.out.println("" + er.getMessage());
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aname;
    private javax.swing.JLabel aname2;
    private javax.swing.JLabel aname3;
    private javax.swing.JLabel aname4;
    private javax.swing.JLabel aname5;
    private javax.swing.JLabel aname6;
    private javax.swing.JLabel icon1;
    private javax.swing.JLabel icon2;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable orders1;
    private javax.swing.JTable pendingOrders;
    private javax.swing.JTextField pn;
    private javax.swing.JTextField pname1;
    private javax.swing.JTextField pp;
    private javax.swing.JTextField pprice1;
    private javax.swing.JTable productsTB;
    private javax.swing.JTextField ps;
    private javax.swing.JComboBox<String> pstats;
    private javax.swing.JComboBox<String> pstatus1;
    private javax.swing.JTextField pstocks1;
    private javax.swing.JButton remove;
    private javax.swing.JButton select;
    private javax.swing.JTable usersTB;
    // End of variables declaration//GEN-END:variables
}
