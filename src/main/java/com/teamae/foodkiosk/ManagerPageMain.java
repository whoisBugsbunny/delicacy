/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.teamae.foodkiosk;

import com.teamae.dbfiles.DBConnect;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;

/**
 *
 * @author Dell India
 */
//histogram
//online qr pay
//table UI
public class ManagerPageMain extends javax.swing.JFrame {

    /* function to switch panel */
    public void switchPanel(JPanel panel) {
        jLayeredPane1.removeAll();
        jLayeredPane1.add(panel);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();
    }
    int currentID = 0000;
    String currentName = "none";
    Boolean currentOwner = false;
    String currentPass = "Password";
    String currentAbout = "About";

    Connection conn = DBConnect.getConn();

    int graphTotalInventory = 1;
    int sumOfItemSold = 0;
    int totalSales = 0;

    private void getInventoryValues() {
        try {
            int rowCount;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM inventory");

            if (rs.next()) {
                rowCount = rs.getInt(1);
                System.out.println(rowCount);
                graphTotalInventory = rowCount;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getSumOfColumnSql(String columnName, String tableName) {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT SUM(" + columnName + ") AS total_sum FROM " + tableName;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                int sum = rs.getInt("total_sum");
                return sum;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);

        }
        return -1;
    }

    public void getinventoryTable() {

        try {
            String query = "SELECT * FROM inventory";
            Statement stat1 = conn.createStatement();
            ResultSet rs = stat1.executeQuery(query);
            DefaultTableModel tb1Model = (DefaultTableModel) InventoryTable.getModel();
            tb1Model.setRowCount(0);
            while (rs.next()) {
                int itemID = rs.getInt("ItemID");
                String itemName = rs.getString("ItemName");
                String vegetarian = String.valueOf(rs.getBoolean("Veg"));
                int price = rs.getInt("Price");
                int avalible = rs.getInt("AvalibleItem");

                String tbData[] = {String.valueOf(itemID), itemName, vegetarian, String.valueOf(price), String.valueOf(avalible)};
                tb1Model.addRow(tbData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getsalesTable() {
        try {
            String query = "SELECT * FROM sales";
            Statement stat1 = conn.createStatement();
            ResultSet rs = stat1.executeQuery(query);
            DefaultTableModel tb1Model = (DefaultTableModel) salestable.getModel();
            tb1Model.setRowCount(0);
            while (rs.next()) {
                int ID = rs.getInt("SoldItemID");
                String Name = rs.getString("ItemName");
                int price = rs.getInt("AtPrice");
                String time = rs.getString("TimeStamp");
                String method = rs.getBoolean("PaymentMethod") ? "Online" : "Cash";

                String tbData[] = {String.valueOf(ID), Name, String.valueOf(price), time, method};
                tb1Model.addRow(tbData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTransections(String selectAllFromTransaction_query_OrderByTimeAsc) {
        try {
            String query = "SELECT * FROM transection " + selectAllFromTransaction_query_OrderByTimeAsc + " ORDER BY time ASC";
            Statement stat1 = conn.createStatement();
            ResultSet rs = stat1.executeQuery(query);
            DefaultTableModel tb1Model = (DefaultTableModel) transectiontable.getModel();
            tb1Model.setRowCount(0);
            while (rs.next()) {
                int ID = rs.getInt("transectionID");
                int numItem = rs.getInt("totalItems");
                double tamount = rs.getDouble("totalAmount");
                double tax = rs.getDouble("tax");
                double dis = rs.getDouble("discount");
                double bamount = rs.getDouble("billAmount");
                String time = rs.getString("time");
                String method = rs.getBoolean("medium_qr") ? "Online" : "Cash";

                String tbData[] = {String.valueOf(ID), String.valueOf(numItem), String.valueOf(tamount), String.valueOf(tax), String.valueOf(dis), String.valueOf(bamount), method, time};
                tb1Model.addRow(tbData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getemployeeTable() {

        try {
            String query = "SELECT * FROM employeerecord";
            Statement stat1 = conn.createStatement();
            ResultSet rs = stat1.executeQuery(query);
            DefaultTableModel tb1Model = (DefaultTableModel) EmployeeTable.getModel();
            tb1Model.setRowCount(0);
            while (rs.next()) {
                int ID = rs.getInt("ManagerID");
                String Name = rs.getString("Name");
                String owner = String.valueOf(rs.getBoolean("Owner"));
                String pass = "********";
                String joDate = rs.getString("Joining");
                if (currentOwner) {
                    pass = rs.getString("Password");
                }
                String about = rs.getString("About");

                String tbData[] = {String.valueOf(ID), Name, about, pass, owner, joDate};
//                DefaultTableModel tb1Model = (DefaultTableModel)EmployeeTable.getModel();
                tb1Model.addRow(tbData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showProfile(int id) {

        try {
            String query = "SELECT * FROM employeerecord where ManagerID=" + id;
            Statement stat1 = conn.createStatement();
            ResultSet rs = stat1.executeQuery(query);
            DefaultTableModel tb1Model = (DefaultTableModel) EmployeeTable.getModel();
            tb1Model.setRowCount(0);
            if (rs.next()) {
                int ID = rs.getInt("ManagerID");
                String Name = rs.getString("Name");
                String owner = ownerOrManager(rs.getBoolean("Owner"));
//                String pass = rs.getString("Password");
                String about = rs.getString("About");

                profileid.setText("#" + String.valueOf(ID));
                profilename.setText(Name);
                profileboutme.setText(about);
                myprofilehead.setText(owner + " Profile");
//                profilename.setText(pass);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveProfile(int id) {
        try {
            String changedname = profilename.getText();
            String changedabout = profileboutme.getText();
            String query = "UPDATE employeerecord SET Name='" + changedname + "',About='" + changedabout + "' where ManagerID=" + id;
            System.out.println();
            Statement stat1 = conn.createStatement();
            stat1.executeUpdate(query);
            currentName = changedname;
            currentAbout = changedabout;
            JOptionPane.showMessageDialog(null, "Details Saved");
            greetinghome.setText("Welcome, " + changedname);

        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String ownerOrManager(Boolean isOwner) {
        if (isOwner) {
            return "Owner";
        } else {
            return "Manager";
        }
    }

    private double transectiontableColSum(int col) {
        double totalPrice = 0.0;
        for (int i = 0; i < transectiontable.getRowCount(); i++) {
            String priceStr = transectiontable.getValueAt(i, col).toString();
            double price = Double.parseDouble(priceStr);
            totalPrice += price;
        }
        return (totalPrice);
    }

    public void transectionTableUpdateValue() {
        ttotalb2.setText(String.valueOf(transectiontableColSum(5)));
        noofitemsold2.setText(String.format("%.0f", transectiontableColSum(1)));
    }

    /**
     * Creates new form ManagerPageMain
     */
    public ManagerPageMain() {
        this(0, "none", false, "password", "about");
    }

    public ManagerPageMain(int id, String name, Boolean owner, String password, String about) {
        initComponents();
        setExtendedState(MenuPage.MAXIMIZED_BOTH);
        currentID = id;
        currentName = name;
        currentOwner = owner;
        currentPass = password;
        currentAbout = about;
        isownerlabel.setText(currentName + " (" + ownerOrManager(currentOwner) + ") / " + String.valueOf(currentID));
        greetinghome.setText("Welcome, " + name);
        employeenoowner.setSelected(true);
        if (!owner) {
            editEmployeePanel.setVisible(false);
        }
        getInventoryValues();
        sumOfItemSold = getSumOfColumnSql("SoldItem", "inventory");
        totalitemsold.setText(String.valueOf(sumOfItemSold));
        totalSales = getSumOfColumnSql("AtPrice", "sales");
        totalsales.setText(String.valueOf(totalSales));
        double temp = totalSales / (sumOfItemSold * 1.0);
        avgprice.setText(String.format("%.2f", temp));
        showPieChart();
        showLineChart();
        showHistogram();
        showBarChart();
        getsalesTable();
    }

    private void showPieChart() {

        //create dataset
        String[] topsale = new String[3];
        int[] sold = new int[3];
        try {
            String query = "select * from inventory order by SoldItem DESC LIMIT 3";
            Statement stat1 = conn.createStatement();
            ResultSet rs = stat1.executeQuery(query);
            int i = 0;
            while (rs.next()) {
                String itemName = rs.getString("ItemName");
                int soldItem = rs.getInt("SoldItem");
                topsale[i] = itemName;
                sold[i] = soldItem;
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultPieDataset barDataset = new DefaultPieDataset();
        barDataset.setValue(topsale[0], Double.valueOf(sold[0]));
        barDataset.setValue(topsale[1], Double.valueOf(sold[1]));
        barDataset.setValue(topsale[2], Double.valueOf(sold[2]));
        barDataset.setValue("Others", Double.valueOf(sumOfItemSold));

        //create chart
        JFreeChart piechart = ChartFactory.createPieChart("Best Performing Items", barDataset, false, true, false);//explain

        PiePlot piePlot = (PiePlot) piechart.getPlot();

        //changing pie chart blocks colors
        piePlot.setSectionPaint(1, new Color(255, 255, 102));
        piePlot.setSectionPaint(2, new Color(102, 255, 102));
        piePlot.setSectionPaint(3, new Color(255, 102, 153));
        piePlot.setSectionPaint(4, new Color(0, 204, 204));

        piePlot.setBackgroundPaint(Color.white);

        //create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        panelPieChart.removeAll();
        panelPieChart.add(barChartPanel, BorderLayout.CENTER);
        panelPieChart.validate();
    }

    private int getSales(String date) {
        try {
            int rowCount;
            Statement stmt = conn.createStatement();

            String fromDateTime = date + "-01 00:00:00";
            String toDateTime = date + "-31 23:59:59";
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sales WHERE TimeStamp >= '" + fromDateTime + "' AND TimeStamp <= '" + toDateTime + "'");
            if (rs.next()) {
                rowCount = rs.getInt(1);
                System.out.println(rowCount);
                return rowCount;
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private void showLineChart() {
        //create dataset for the graph

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(getSales("2023-01"), "Amount", "jan");
        dataset.setValue(getSales("2023-02"), "Amount", "feb");
        dataset.setValue(getSales("2023-03"), "Amount", "mar");
        dataset.setValue(getSales("2023-04"), "Amount", "apr");
        dataset.setValue(getSales("2023-05"), "Amount", "may");
        dataset.setValue(getSales("2023-06"), "Amount", "jun");
        dataset.setValue(getSales("2023-07"), "Amount", "jul");
        dataset.setValue(getSales("2023-08"), "Amount", "Aug");
        dataset.setValue(getSales("2023-09"), "Amount", "Sep");
        dataset.setValue(getSales("2023-10"), "Amount", "Oct");
        dataset.setValue(getSales("2023-11"), "Amount", "Nov");
        dataset.setValue(getSales("2023-12"), "Amount", "Dec");

        //create chart
        JFreeChart linechart = ChartFactory.createLineChart("Linear Growth", "Monthly", "Amount",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        //create plot object
        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
        // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
        lineCategoryPlot.setBackgroundPaint(Color.white);

        //create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(232, 82, 30);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        //create chartPanel to display chart(graph)
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        panelLineChart.removeAll();
        panelLineChart.add(lineChartPanel, BorderLayout.CENTER);
        panelLineChart.validate();
    }

    private void showHistogram() {

        double[] salesData = {15.0, 22.5, 10.0, 18.0, 12.5, 8.0, 14.0, 20.0, 7.5, 9.0};

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Food Sales", salesData, graphTotalInventory);

        JFreeChart chart = ChartFactory.createHistogram("Food Sales Histogram", "Sales Amount", "Frequency", dataset, PlotOrientation.VERTICAL, false, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.getRenderer().setSeriesPaint(0, new Color(232, 82, 30));
        ChartPanel barpChartPanel2 = new ChartPanel(chart);
        PanelHistogram.removeAll();
        PanelHistogram.add(barpChartPanel2, BorderLayout.CENTER);
        PanelHistogram.validate();
    }

    private void showBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(getSales("2023-01"), "Amount", "jan");
        dataset.setValue(getSales("2023-02"), "Amount", "feb");
        dataset.setValue(getSales("2023-03"), "Amount", "mar");
        dataset.setValue(getSales("2023-04"), "Amount", "apr");
        dataset.setValue(getSales("2023-05"), "Amount", "may");
        dataset.setValue(getSales("2023-06"), "Amount", "jun");
        dataset.setValue(getSales("2023-07"), "Amount", "jul");
        dataset.setValue(getSales("2023-08"), "Amount", "Aug");
        dataset.setValue(getSales("2023-09"), "Amount", "Sep");
        dataset.setValue(getSales("2023-10"), "Amount", "Oct");
        dataset.setValue(getSales("2023-11"), "Amount", "Nov");
        dataset.setValue(getSales("2023-12"), "Amount", "Dec");

        JFreeChart chart = ChartFactory.createBarChart("Sales Per Month", "Monthly", "Amount",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        //categoryPlot.setRangeGridlinePaint(Color.BLUE);
        categoryPlot.setBackgroundPaint(Color.WHITE);
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        Color clr3 = new Color(232, 82, 30);
        renderer.setSeriesPaint(0, clr3);

        ChartPanel barpChartPanel = new ChartPanel(chart);
        PanelBarChart.removeAll();
        PanelBarChart.add(barpChartPanel, BorderLayout.CENTER);
        PanelBarChart.validate();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupVegBtns = new javax.swing.ButtonGroup();
        groupownerbtn = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        homebtn = new javax.swing.JLabel();
        employeebtn = new javax.swing.JLabel();
        reportbtn = new javax.swing.JLabel();
        inventorybtn = new javax.swing.JLabel();
        inventorybtn1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        logout = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        isownerlabel = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        home = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        greetinghome = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        totalsales = new javax.swing.JLabel();
        totalitemsold = new javax.swing.JLabel();
        avgprice = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        salestable = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        panelPieChart = new javax.swing.JPanel();
        panelLineChart = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        PanelHistogram = new javax.swing.JPanel();
        PanelBarChart = new javax.swing.JPanel();
        transections = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        greetinghome1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        transectiontable = new javax.swing.JTable();
        jPanel36 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        ttotalb = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        noofitemsold = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        ttotalb2 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        noofitemsold2 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        fromdateq = new javax.swing.JTextField();
        todateq = new javax.swing.JTextField();
        searchtransquery = new javax.swing.JButton();
        employee = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        EmployeeTable = new javax.swing.JTable();
        editEmployeePanel = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        employeeid = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        employeename = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        employeepass = new javax.swing.JPasswordField();
        jPanel21 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        employeeisowner = new javax.swing.JRadioButton();
        employeenoowner = new javax.swing.JRadioButton();
        jPanel25 = new javax.swing.JPanel();
        employeesave = new javax.swing.JButton();
        employeeaddbtn1 = new javax.swing.JButton();
        employeedel = new javax.swing.JButton();
        employeeclear = new javax.swing.JButton();
        myprofile = new javax.swing.JPanel();
        headpanel = new javax.swing.JPanel();
        myprofilehead = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        profileid = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        profilename = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        profileboutme = new javax.swing.JTextArea();
        jPanel31 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        SaveEditedprofile = new javax.swing.JButton();
        CancelEdit2 = new javax.swing.JButton();
        inventory = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        InventoryTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        inventoryid = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        inventoryname = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        inventoryveg = new javax.swing.JRadioButton();
        inventorynonveg = new javax.swing.JRadioButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        inventoryprice = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        inventoryavail = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        SaveEditedInventory = new javax.swing.JButton();
        inventoryaddbtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        inventoryclear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dashboard");
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(1200, 800));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 800));

        jPanel2.setBackground(new java.awt.Color(30, 30, 30));
        jPanel2.setForeground(new java.awt.Color(221, 221, 221));

        jLabel4.setFont(new java.awt.Font("Segoe Script", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(232, 82, 30));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("delicacy");
        jLabel4.setToolTipText("");

        jPanel6.setBackground(new java.awt.Color(30, 30, 30));

        homebtn.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        homebtn.setForeground(new java.awt.Color(221, 221, 221));
        homebtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        homebtn.setText("Home");
        homebtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        homebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homebtnMouseClicked(evt);
            }
        });

        employeebtn.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        employeebtn.setForeground(new java.awt.Color(221, 221, 221));
        employeebtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        employeebtn.setText("Employee");
        employeebtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeebtnMouseClicked(evt);
            }
        });

        reportbtn.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        reportbtn.setForeground(new java.awt.Color(221, 221, 221));
        reportbtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reportbtn.setText("My Profile");
        reportbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reportbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportbtnMouseClicked(evt);
            }
        });

        inventorybtn.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inventorybtn.setForeground(new java.awt.Color(221, 221, 221));
        inventorybtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        inventorybtn.setText("Inventory");
        inventorybtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inventorybtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventorybtnMouseClicked(evt);
            }
        });

        inventorybtn1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inventorybtn1.setForeground(new java.awt.Color(221, 221, 221));
        inventorybtn1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        inventorybtn1.setText("Transactions");
        inventorybtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inventorybtn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventorybtn1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inventorybtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(reportbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(homebtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(employeebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inventorybtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homebtn)
                .addGap(18, 18, 18)
                .addComponent(inventorybtn1)
                .addGap(18, 18, 18)
                .addComponent(inventorybtn)
                .addGap(18, 18, 18)
                .addComponent(employeebtn)
                .addGap(18, 18, 18)
                .addComponent(reportbtn)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addGap(44, 44, 44)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jPanel3.setForeground(new java.awt.Color(204, 204, 204));

        logout.setBackground(new java.awt.Color(255, 51, 51));
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Log Out");
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("DASHBOARD");

        isownerlabel.setText("owner / manager");
        isownerlabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addComponent(isownerlabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(isownerlabel)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(logout)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLayeredPane1.setBackground(new java.awt.Color(0, 204, 204));
        jLayeredPane1.setLayout(new java.awt.CardLayout());

        home.setBackground(new java.awt.Color(255, 255, 255));

        greetinghome.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        greetinghome.setText("Welcome Ankit");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(greetinghome, javax.swing.GroupLayout.DEFAULT_SIZE, 948, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(greetinghome, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel14.setText("Total Item Sold");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setText("Total Sales");

        totalsales.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        totalsales.setText("0");

        totalitemsold.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        totalitemsold.setText("0");

        avgprice.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        avgprice.setText("0");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setText("Avg Price");

        salestable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ItemID", "Item Name", "Selling Price", "Date and Time", "Payment Method"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(salestable);
        if (salestable.getColumnModel().getColumnCount() > 0) {
            salestable.getColumnModel().getColumn(0).setPreferredWidth(20);
            salestable.getColumnModel().getColumn(2).setPreferredWidth(45);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totalsales, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                            .addComponent(totalitemsold, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(avgprice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalitemsold, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(totalsales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(avgprice, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        jPanel32.setLayout(new java.awt.GridLayout(1, 0));

        panelPieChart.setBackground(new java.awt.Color(0, 255, 255));
        panelPieChart.setLayout(new java.awt.BorderLayout());
        jPanel32.add(panelPieChart);

        panelLineChart.setBackground(new java.awt.Color(153, 255, 153));
        panelLineChart.setLayout(new java.awt.BorderLayout());
        jPanel32.add(panelLineChart);

        jPanel33.setLayout(new java.awt.GridLayout(1, 0));

        PanelHistogram.setBackground(new java.awt.Color(255, 153, 102));
        PanelHistogram.setLayout(new java.awt.BorderLayout());
        jPanel33.add(PanelHistogram);

        PanelBarChart.setBackground(new java.awt.Color(255, 51, 255));
        PanelBarChart.setLayout(new java.awt.BorderLayout());
        jPanel33.add(PanelBarChart);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLayeredPane1.add(home, "card3");

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        greetinghome1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        greetinghome1.setText("Transaction History");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(greetinghome1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(greetinghome1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
        );

        transectiontable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaction ID", "No. of Items", "Total", "Tax", "Discount", "Grand Total", "Payment Method", "Date Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(transectiontable);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Total Payments");

        ttotalb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ttotalb.setText("0");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("Number of Items Sold");

        noofitemsold.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        noofitemsold.setText("0");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ttotalb, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noofitemsold, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(323, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(ttotalb, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(noofitemsold, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setText("Total Payments");

        ttotalb2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ttotalb2.setText("0");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setText("Number of Items Sold");

        noofitemsold2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        noofitemsold2.setText("0");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ttotalb2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noofitemsold2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(ttotalb2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(noofitemsold2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setText("From");

        jLabel23.setText("To");

        fromdateq.setText("YYYY-MM-DD");
        fromdateq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fromdateqFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fromdateqFocusLost(evt);
            }
        });

        todateq.setText("YYYY-MM-DD");
        todateq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                todateqFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                todateqFocusLost(evt);
            }
        });

        searchtransquery.setText("Search Record");
        searchtransquery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchtransqueryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromdateq, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todateq, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchtransquery, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchtransquery, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fromdateq, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(todateq, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5)
                    .addComponent(jPanel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel39, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout transectionsLayout = new javax.swing.GroupLayout(transections);
        transections.setLayout(transectionsLayout);
        transectionsLayout.setHorizontalGroup(
            transectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        transectionsLayout.setVerticalGroup(
            transectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLayeredPane1.add(transections, "card6");

        employee.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Employee Details");

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        EmployeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "About", "Password", "IsOwner", "Joining Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EmployeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmployeeTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(EmployeeTable);

        editEmployeePanel.setBackground(new java.awt.Color(255, 255, 255));
        editEmployeePanel.setLayout(new java.awt.GridLayout(1, 0));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("ManagerID");

        employeeid.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        employeeid.setForeground(new java.awt.Color(204, 204, 204));
        employeeid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeeid.setText("#0000");
        employeeid.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(employeeid, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(employeeid, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addContainerGap())
        );

        editEmployeePanel.add(jPanel19);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Name");

        employeename.setFont(new java.awt.Font("Segoe UI Emoji", 0, 14)); // NOI18N
        employeename.setForeground(new java.awt.Color(51, 51, 51));
        employeename.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        employeename.setText("EMPLOYEE NAME");
        employeename.setToolTipText("");
        employeename.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        employeename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeenameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(employeename))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(employeename, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        editEmployeePanel.add(jPanel20);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Password");

        employeepass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        employeepass.setText("********");
        employeepass.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(employeepass))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(employeepass, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        editEmployeePanel.add(jPanel23);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Is Owner");

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        groupownerbtn.add(employeeisowner);
        employeeisowner.setForeground(new java.awt.Color(51, 51, 51));
        employeeisowner.setText("Yes");
        employeeisowner.setActionCommand("true");
        employeeisowner.setAlignmentX(0.5F);
        employeeisowner.setBorder(null);
        employeeisowner.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeeisowner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeeisowner.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        employeeisowner.setMargin(new java.awt.Insets(0, 0, 0, 0));
        employeeisowner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeisownerActionPerformed(evt);
            }
        });

        groupownerbtn.add(employeenoowner);
        employeenoowner.setForeground(new java.awt.Color(51, 51, 51));
        employeenoowner.setText("No");
        employeenoowner.setActionCommand("false");
        employeenoowner.setAlignmentX(0.5F);
        employeenoowner.setBorder(null);
        employeenoowner.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeenoowner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeenoowner.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        employeenoowner.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(employeeisowner, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeenoowner, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeisowner)
                    .addComponent(employeenoowner))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        editEmployeePanel.add(jPanel21);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        employeesave.setBackground(new java.awt.Color(255, 51, 51));
        employeesave.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        employeesave.setForeground(new java.awt.Color(255, 255, 255));
        employeesave.setText("SAVE");
        employeesave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeesave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeesaveActionPerformed(evt);
            }
        });

        employeeaddbtn1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        employeeaddbtn1.setText("+");
        employeeaddbtn1.setActionCommand("add");
        employeeaddbtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeeaddbtn1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        employeeaddbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeaddbtn1ActionPerformed(evt);
            }
        });

        employeedel.setBackground(new java.awt.Color(255, 51, 51));
        employeedel.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        employeedel.setForeground(new java.awt.Color(255, 255, 255));
        employeedel.setText("<html>&#10006;</html>");
        employeedel.setToolTipText("");
        employeedel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employeedel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        employeedel.setIconTextGap(0);
        employeedel.setMargin(new java.awt.Insets(0, 0, 0, 0));
        employeedel.setMinimumSize(new java.awt.Dimension(76, 32));
        employeedel.setPreferredSize(new java.awt.Dimension(76, 32));
        employeedel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeedelActionPerformed(evt);
            }
        });

        employeeclear.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        employeeclear.setText("CLEAR");
        employeeclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeclearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(employeedel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(employeeaddbtn1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeesave, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                    .addComponent(employeeclear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(employeeaddbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(employeesave, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeeclear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(employeedel, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addContainerGap())
        );

        editEmployeePanel.add(jPanel25);

        javax.swing.GroupLayout employeeLayout = new javax.swing.GroupLayout(employee);
        employee.setLayout(employeeLayout);
        employeeLayout.setHorizontalGroup(
            employeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(employeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 960, Short.MAX_VALUE)
                    .addComponent(editEmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        employeeLayout.setVerticalGroup(
            employeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editEmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(employee, "card5");

        myprofile.setBackground(new java.awt.Color(255, 255, 255));

        myprofilehead.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        myprofilehead.setText("My Profile");

        javax.swing.GroupLayout headpanelLayout = new javax.swing.GroupLayout(headpanel);
        headpanel.setLayout(headpanelLayout);
        headpanelLayout.setHorizontalGroup(
            headpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myprofilehead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        headpanelLayout.setVerticalGroup(
            headpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myprofilehead, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("My ID");

        profileid.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        profileid.setForeground(new java.awt.Color(204, 204, 204));
        profileid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileid.setText("#0000");
        profileid.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(profileid, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(profileid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Name");

        profilename.setFont(new java.awt.Font("Segoe UI Emoji", 0, 14)); // NOI18N
        profilename.setForeground(new java.awt.Color(51, 51, 51));
        profilename.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        profilename.setText("OLD NAME");
        profilename.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        profilename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilenameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(profilename, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(profilename, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("About Me");

        profileboutme.setColumns(20);
        profileboutme.setRows(5);
        jScrollPane3.setViewportView(profileboutme);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        SaveEditedprofile.setBackground(new java.awt.Color(255, 51, 51));
        SaveEditedprofile.setForeground(new java.awt.Color(255, 255, 255));
        SaveEditedprofile.setText("SAVE");
        SaveEditedprofile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SaveEditedprofile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveEditedprofileActionPerformed(evt);
            }
        });

        CancelEdit2.setText("CANCEL");
        CancelEdit2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CancelEdit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelEdit2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(404, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CancelEdit2)
                    .addComponent(SaveEditedprofile, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(SaveEditedprofile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CancelEdit2)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout myprofileLayout = new javax.swing.GroupLayout(myprofile);
        myprofile.setLayout(myprofileLayout);
        myprofileLayout.setHorizontalGroup(
            myprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, myprofileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(myprofileLayout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(259, Short.MAX_VALUE))
        );
        myprofileLayout.setVerticalGroup(
            myprofileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myprofileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        jLayeredPane1.add(myprofile, "card4");

        inventory.setBackground(new java.awt.Color(255, 255, 255));

        InventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Item Name", "Vegetarian", "Price", "Avalible"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        InventoryTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        InventoryTable.setRowHeight(30);
        InventoryTable.setRowMargin(10);
        InventoryTable.getTableHeader().setReorderingAllowed(false);
        InventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InventoryTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(InventoryTable);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Inventory");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Item ID");

        inventoryid.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inventoryid.setForeground(new java.awt.Color(204, 204, 204));
        inventoryid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inventoryid.setText("#0000");
        inventoryid.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inventoryid, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(inventoryid, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.add(jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Item Name");

        inventoryname.setFont(new java.awt.Font("Segoe UI Emoji", 0, 14)); // NOI18N
        inventoryname.setForeground(new java.awt.Color(51, 51, 51));
        inventoryname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inventoryname.setText("ITEM NAME");
        inventoryname.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        inventoryname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventorynameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(inventoryname))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inventoryname, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel8);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Vegetarian");

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        groupVegBtns.add(inventoryveg);
        inventoryveg.setForeground(new java.awt.Color(51, 51, 51));
        inventoryveg.setText("Vegetarian");
        inventoryveg.setActionCommand("true");
        inventoryveg.setAlignmentX(0.5F);
        inventoryveg.setBorder(null);
        inventoryveg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inventoryveg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryvegActionPerformed(evt);
            }
        });

        groupVegBtns.add(inventorynonveg);
        inventorynonveg.setForeground(new java.awt.Color(51, 51, 51));
        inventorynonveg.setText("Non-Vegetarian");
        inventorynonveg.setActionCommand("false");
        inventorynonveg.setAlignmentX(0.5F);
        inventorynonveg.setBorder(null);
        inventorynonveg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inventoryveg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inventorynonveg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryveg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inventorynonveg)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Price");

        inventoryprice.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inventoryprice.setForeground(new java.awt.Color(51, 51, 51));
        inventoryprice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inventoryprice.setText("00");
        inventoryprice.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        inventoryprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventorypriceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(inventoryprice))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inventoryprice, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Avalible");

        inventoryavail.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        inventoryavail.setForeground(new java.awt.Color(51, 51, 51));
        inventoryavail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inventoryavail.setText("00");
        inventoryavail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        inventoryavail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryavailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(inventoryavail))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inventoryavail, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        SaveEditedInventory.setBackground(new java.awt.Color(255, 51, 51));
        SaveEditedInventory.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SaveEditedInventory.setForeground(new java.awt.Color(255, 255, 255));
        SaveEditedInventory.setText("SAVE");
        SaveEditedInventory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SaveEditedInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveEditedInventoryActionPerformed(evt);
            }
        });

        inventoryaddbtn.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        inventoryaddbtn.setText("+");
        inventoryaddbtn.setActionCommand("add");
        inventoryaddbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inventoryaddbtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        inventoryaddbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryaddbtnActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("<html>&#10006;</html>");
        jButton1.setToolTipText("");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setIconTextGap(0);
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setMinimumSize(new java.awt.Dimension(76, 32));
        jButton1.setPreferredSize(new java.awt.Dimension(76, 32));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        inventoryclear.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        inventoryclear.setText("CLEAR");
        inventoryclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryclearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(inventoryaddbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SaveEditedInventory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inventoryclear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SaveEditedInventory, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inventoryaddbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inventoryclear, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5.add(jPanel13);

        javax.swing.GroupLayout inventoryLayout = new javax.swing.GroupLayout(inventory);
        inventory.setLayout(inventoryLayout);
        inventoryLayout.setHorizontalGroup(
            inventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inventoryLayout.createSequentialGroup()
                        .addGroup(inventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        inventoryLayout.setVerticalGroup(
            inventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLayeredPane1.add(inventory, "card2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLayeredPane1)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPageMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_logoutActionPerformed

    private void homebtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homebtnMouseClicked
        switchPanel(home);
        getsalesTable();
    }//GEN-LAST:event_homebtnMouseClicked

    private void employeebtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeebtnMouseClicked
        switchPanel(employee);
        getemployeeTable();
    }//GEN-LAST:event_employeebtnMouseClicked

    private void reportbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportbtnMouseClicked
        switchPanel(myprofile);
        showProfile(currentID);
    }//GEN-LAST:event_reportbtnMouseClicked

    private void inventorybtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventorybtnMouseClicked
        switchPanel(inventory);
        getinventoryTable();
    }//GEN-LAST:event_inventorybtnMouseClicked

    private void inventorynameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventorynameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inventorynameActionPerformed

    private void inventoryvegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryvegActionPerformed

    }//GEN-LAST:event_inventoryvegActionPerformed

    private void inventorypriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventorypriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inventorypriceActionPerformed

    private void inventoryavailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryavailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inventoryavailActionPerformed

    private void profilenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilenameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profilenameActionPerformed

    private void SaveEditedprofileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveEditedprofileActionPerformed
        saveProfile(currentID);
    }//GEN-LAST:event_SaveEditedprofileActionPerformed

    private void CancelEdit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelEdit2ActionPerformed
        profilename.setText(currentName);
        profileboutme.setText(currentAbout);
    }//GEN-LAST:event_CancelEdit2ActionPerformed

    private void InventoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventoryTableMouseClicked
        int row = InventoryTable.getSelectedRow();
        TableModel mod = InventoryTable.getModel();
        inventoryid.setText("#" + mod.getValueAt(row, 0).toString());
        inventoryname.setText(mod.getValueAt(row, 1).toString());
        inventoryprice.setText(mod.getValueAt(row, 3).toString());
        inventoryavail.setText(mod.getValueAt(row, 4).toString());
        boolean getveg = Boolean.parseBoolean(mod.getValueAt(row, 2).toString());
        if (getveg) {
            inventoryveg.setSelected(true);
        } else {
            inventorynonveg.setSelected(true);
        }

    }//GEN-LAST:event_InventoryTableMouseClicked

    public void resetEditInventory() {
        inventoryid.setText("#0000");
        inventoryname.setText("OLD NAME");
        inventoryprice.setText("00");
        inventoryavail.setText("00");
        inventoryveg.setSelected(false);
        inventorynonveg.setSelected(false);
    }

    public void resetEditEmployee() {
        employeeid.setText("#0000");
        employeename.setText("EMPLOYEE NAME");
        employeenoowner.setSelected(true);
        employeepass.setText("********");
    }
    private void SaveEditedInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveEditedInventoryActionPerformed
        int idSelected = Integer.parseInt(inventoryid.getText().substring(1));
        System.out.println(idSelected);
        if (idSelected != 0) {
            try {
                String changedname = inventoryname.getText();
                int changedprice = Integer.parseInt(inventoryprice.getText());
                int changedavail = Integer.parseInt(inventoryavail.getText());
                boolean changedtype = inventoryveg.isSelected();
                String query = "UPDATE inventory SET ItemName='" + changedname + "',Price=" + changedprice + ",AvalibleItem=" + changedavail + ",Veg=" + changedtype + " where ItemID=" + idSelected;
                System.out.println();
                Statement stat1 = conn.createStatement();
                stat1.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Item #" + idSelected + " Updated Successfully");
                getinventoryTable();
                resetEditInventory();
            } catch (SQLException ex) {
                Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "First Select One Item from the Table");
        }
    }//GEN-LAST:event_SaveEditedInventoryActionPerformed

    private void inventoryaddbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryaddbtnActionPerformed
        int idSelected = Integer.parseInt(inventoryid.getText().substring(1));
        if (idSelected == 0) {
            String itemname = inventoryname.getText().trim();
            int itemprice = Integer.parseInt(inventoryprice.getText());
            int itemavail = Integer.parseInt(inventoryavail.getText());
            boolean itemtype = inventoryveg.isSelected();
            if ((!"ITEM NAME".equals(itemname) && !"".equals(itemname)) && itemprice != 0 && itemavail != 0 && (inventoryveg.isSelected() || inventorynonveg.isSelected())) {
                try {
                    String query = "INSERT into inventory (ItemName,Price,Veg,AvalibleItem) values('" + itemname + "'," + itemprice + "," + itemtype + "," + itemavail + ")";
                    Statement stat1 = conn.createStatement();
                    stat1.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, itemname + " Added Successfully");
                    getinventoryTable();
                    resetEditInventory();
                } catch (SQLException ex) {
                    Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "All fields Are Mandatory");
            }
        } else {
            JOptionPane.showMessageDialog(null, "#" + idSelected + " Item Already Exist");
            resetEditInventory();
        }
    }//GEN-LAST:event_inventoryaddbtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String itemname = inventoryname.getText().trim();
        int itemid = Integer.parseInt(inventoryid.getText().substring(1));
        int itemavail = Integer.parseInt(inventoryavail.getText());
        System.out.println(itemid);
        if (itemid != 0) {
            try {
                String query = "DELETE FROM inventory WHERE ItemID=" + itemid;
                Statement stat1 = conn.createStatement();
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + itemname + "? " + itemavail + " available.", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    stat1.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, "#" + itemid + " " + itemname + " Deleted Successfully");
                    resetEditInventory();
                    getinventoryTable();
                }

            } catch (SQLException ex) {
                Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "First Select One Item from the Table");

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void employeenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeenameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeenameActionPerformed

    private void employeeisownerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeisownerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeisownerActionPerformed

    private void employeesaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeesaveActionPerformed
        int idSelected = Integer.parseInt(employeeid.getText().substring(1));
        if (idSelected != 0) {
            try {
                String changedname = employeename.getText();
                char[] managerpass = employeepass.getPassword();
                String changedpass = new String(managerpass);
                boolean changedtype = employeeisowner.isSelected();
                String query = "UPDATE employeerecord SET Name='" + changedname + "',Password='" + changedpass + "',Owner=" + changedtype + " where ManagerID=" + idSelected;
                System.out.println();
                Statement stat1 = conn.createStatement();
                stat1.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Employee #" + idSelected + " Updated Successfully");
                getemployeeTable();
                resetEditEmployee();
            } catch (SQLException ex) {
                Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "First Select One Employee from the Table");
        }
    }//GEN-LAST:event_employeesaveActionPerformed

    private void employeeaddbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeaddbtn1ActionPerformed
        int idSelected = Integer.parseInt(employeeid.getText().substring(1));
        if (idSelected == 0) {
            String ename = employeename.getText().trim();
            char[] epass = employeepass.getPassword();
            String epassString = new String(epass).trim();
            boolean etype = employeeisowner.isSelected();
            if ((!"EMPLOYEE NAME".equals(ename) && !"".equals(ename)) && !"********".equals(epassString) && !"".equals(epassString) && (etype || employeenoowner.isSelected())) {
                try {
                    String query = "INSERT into employeerecord (Password,Name,Owner) values('" + epassString + "','" + ename + "'," + etype + ")";
                    Statement stat1 = conn.createStatement();
                    stat1.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, ename + " Added Successfully");
                    getemployeeTable();
                    resetEditEmployee();
                } catch (SQLException ex) {
                    Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "All fields Are Mandatory");
            }
        } else {
            JOptionPane.showMessageDialog(null, "#" + idSelected + " Employee Already Exist");
            resetEditInventory();
        }
    }//GEN-LAST:event_employeeaddbtn1ActionPerformed

    private void employeedelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeedelActionPerformed
        String ename = employeename.getText().trim();
        int eid = Integer.parseInt(employeeid.getText().substring(1));
        String ownerornot = ownerOrManager(employeeisowner.isSelected());
        if (eid != 0) {
            try {
                String query = "DELETE FROM employeerecord WHERE ManagerID=" + eid;
                Statement stat1 = conn.createStatement();
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to Remove " + ename + " (" + ownerornot + ") ?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    stat1.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, "#" + eid + " " + ename + " Deleted Successfully");
                    getemployeeTable();
                    resetEditEmployee();
                }

            } catch (SQLException ex) {
                Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "First Select Employee from the Table");

        }
    }//GEN-LAST:event_employeedelActionPerformed

    private void EmployeeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmployeeTableMouseClicked
        int row = EmployeeTable.getSelectedRow();
        TableModel mod = EmployeeTable.getModel();
        employeeid.setText("#" + mod.getValueAt(row, 0).toString());
        employeename.setText(mod.getValueAt(row, 1).toString());
        employeepass.setText(mod.getValueAt(row, 3).toString());
        boolean getowner = Boolean.parseBoolean(mod.getValueAt(row, 4).toString());
        if (getowner) {
            employeeisowner.setSelected(true);
        } else {
            employeenoowner.setSelected(true);
        }
    }//GEN-LAST:event_EmployeeTableMouseClicked

    private void employeeclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeclearActionPerformed
        resetEditEmployee();
    }//GEN-LAST:event_employeeclearActionPerformed

    private void inventoryclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryclearActionPerformed
        resetEditInventory();
    }//GEN-LAST:event_inventoryclearActionPerformed

    private void inventorybtn1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventorybtn1MouseClicked
        switchPanel(transections);
        ttotalb.setText(String.valueOf(getSumOfColumnSql("billAmount", "transection")));
        noofitemsold.setText(String.valueOf(getSumOfColumnSql("totalItems", "transection")));
        getTransections("");
        transectionTableUpdateValue();
    }//GEN-LAST:event_inventorybtn1MouseClicked

    private void searchtransqueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchtransqueryActionPerformed
        String fromDateQuery = fromdateq.getText();
        String toDateQuery = todateq.getText();

        // Append the necessary time information to the dates
        String fromDateTime = fromDateQuery + " 00:00:00";
        String toDateTime = toDateQuery + " 23:59:59";

        String whereQuery = "";
        if (!fromDateQuery.equals("YYYY-MM-DD") && !toDateQuery.equals("YYYY-MM-DD")) {
            whereQuery = "WHERE time >= '" + fromDateTime + "' AND time <= '" + toDateTime + "'";
        } else if (!fromDateQuery.equals("YYYY-MM-DD")) {
            whereQuery = "WHERE time >= '" + fromDateTime + "'";
        } else if (!toDateQuery.equals("YYYY-MM-DD")) {
            whereQuery = "WHERE time <= '" + toDateTime + "'";
        }

        getTransections(whereQuery);
        transectionTableUpdateValue();
    }//GEN-LAST:event_searchtransqueryActionPerformed

    private void fromdateqFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fromdateqFocusGained
        Placeholder(false, fromdateq, "YYYY-MM-DD");
    }//GEN-LAST:event_fromdateqFocusGained

    private void fromdateqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fromdateqFocusLost
        Placeholder(true, fromdateq, "YYYY-MM-DD");        // TODO add your handling code here:
    }//GEN-LAST:event_fromdateqFocusLost

    private void todateqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_todateqFocusLost
        Placeholder(true, todateq, "YYYY-MM-DD");        // TODO add your handling code here:
    }//GEN-LAST:event_todateqFocusLost

    private void todateqFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_todateqFocusGained
        Placeholder(false, todateq, "YYYY-MM-DD");
    }//GEN-LAST:event_todateqFocusGained

    public void Placeholder(Boolean toadd, JTextField element, String placeholder) {
        String text = element.getText();
        if (toadd && ("".equals(text) || placeholder.contains(text))) {
            element.setText(placeholder);
        } else if (placeholder.equals(text) || placeholder.contains(text)) {
            element.setText("");
        }
    }

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
            java.util.logging.Logger.getLogger(ManagerPageMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerPageMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerPageMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerPageMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerPageMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelEdit2;
    private javax.swing.JTable EmployeeTable;
    private javax.swing.JTable InventoryTable;
    private javax.swing.JPanel PanelBarChart;
    private javax.swing.JPanel PanelHistogram;
    private javax.swing.JButton SaveEditedInventory;
    private javax.swing.JButton SaveEditedprofile;
    private javax.swing.JLabel avgprice;
    private javax.swing.JPanel editEmployeePanel;
    private javax.swing.JPanel employee;
    private javax.swing.JButton employeeaddbtn1;
    private javax.swing.JLabel employeebtn;
    private javax.swing.JButton employeeclear;
    private javax.swing.JButton employeedel;
    private javax.swing.JLabel employeeid;
    private javax.swing.JRadioButton employeeisowner;
    private javax.swing.JTextField employeename;
    private javax.swing.JRadioButton employeenoowner;
    private javax.swing.JPasswordField employeepass;
    private javax.swing.JButton employeesave;
    private javax.swing.JTextField fromdateq;
    private javax.swing.JLabel greetinghome;
    private javax.swing.JLabel greetinghome1;
    private javax.swing.ButtonGroup groupVegBtns;
    private javax.swing.ButtonGroup groupownerbtn;
    private javax.swing.JPanel headpanel;
    private javax.swing.JPanel home;
    private javax.swing.JLabel homebtn;
    private javax.swing.JPanel inventory;
    private javax.swing.JButton inventoryaddbtn;
    private javax.swing.JTextField inventoryavail;
    private javax.swing.JLabel inventorybtn;
    private javax.swing.JLabel inventorybtn1;
    private javax.swing.JButton inventoryclear;
    private javax.swing.JLabel inventoryid;
    private javax.swing.JTextField inventoryname;
    private javax.swing.JRadioButton inventorynonveg;
    private javax.swing.JTextField inventoryprice;
    private javax.swing.JRadioButton inventoryveg;
    private javax.swing.JLabel isownerlabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton logout;
    private javax.swing.JPanel myprofile;
    private javax.swing.JLabel myprofilehead;
    private javax.swing.JLabel noofitemsold;
    private javax.swing.JLabel noofitemsold2;
    private javax.swing.JPanel panelLineChart;
    private javax.swing.JPanel panelPieChart;
    private javax.swing.JTextArea profileboutme;
    private javax.swing.JLabel profileid;
    private javax.swing.JTextField profilename;
    private javax.swing.JLabel reportbtn;
    private javax.swing.JTable salestable;
    private javax.swing.JButton searchtransquery;
    private javax.swing.JTextField todateq;
    private javax.swing.JLabel totalitemsold;
    private javax.swing.JLabel totalsales;
    private javax.swing.JPanel transections;
    private javax.swing.JTable transectiontable;
    private javax.swing.JLabel ttotalb;
    private javax.swing.JLabel ttotalb2;
    // End of variables declaration//GEN-END:variables
}
