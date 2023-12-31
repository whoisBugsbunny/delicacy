/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.teamae.foodkiosk;

import com.teamae.dbfiles.DBConnect;
import java.awt.event.KeyEvent;
import java.sql.*;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Dell India
 */
public class ManagerPage extends javax.swing.JFrame {

    /**
     * Creates new form ManagerPage
     */
    public ManagerPage() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        Mid = new javax.swing.JTextField();
        Mpassword = new javax.swing.JPasswordField();
        Mlogin = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login For Manager");
        setMinimumSize(new java.awt.Dimension(400, 300));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBackground(new java.awt.Color(253, 253, 253));

        jLabel1.setFont(new java.awt.Font("Segoe Script", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(232, 82, 30));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("delicacy");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(263, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);

        jPanel3.setBackground(new java.awt.Color(0, 153, 255));

        Mid.setBackground(new java.awt.Color(253, 253, 253));
        Mid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Mid.setForeground(new java.awt.Color(67, 63, 50));
        Mid.setText("ID");
        Mid.setNextFocusableComponent(Mpassword);
        Mid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                MidFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                MidFocusLost(evt);
            }
        });
        Mid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MidActionPerformed(evt);
            }
        });

        Mpassword.setBackground(new java.awt.Color(253, 253, 253));
        Mpassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Mpassword.setForeground(new java.awt.Color(67, 63, 50));
        Mpassword.setText("Password");
        Mpassword.setName(""); // NOI18N
        Mpassword.setNextFocusableComponent(Mlogin);
        Mpassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                MpasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                MpasswordFocusLost(evt);
            }
        });
        Mpassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MpasswordKeyPressed(evt);
            }
        });

        Mlogin.setBackground(new java.awt.Color(232, 82, 30));
        Mlogin.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        Mlogin.setForeground(new java.awt.Color(255, 255, 255));
        Mlogin.setText("LOG IN");
        Mlogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Mlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MloginActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Manager Login");

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("<html>&#10006;</html>");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                            .addComponent(Mpassword, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Mid, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Mlogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Mid, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Mpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Mlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);

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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public Boolean isSqlInjection(String s){
        if (s == null) {
            return false;
        }
        return (s.contains("\'") || s.contains("\""));
    }
    private void MloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MloginActionPerformed
            Connection conn = DBConnect.getConn();
            int managerid;
            try{
                managerid = Integer.parseInt(Mid.getText());
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "enter valid ID");
                return;
            }
            char[] managerpass = Mpassword.getPassword();
            String managerpassString = new String(managerpass);
        try {
            if(isSqlInjection(String.valueOf(managerid)) || isSqlInjection(managerpassString)){
                JOptionPane.showMessageDialog(null, "Looks Like SQL Injection Attempted!");
                return;
            }
            String query = "SELECT * FROM employeerecord WHERE ManagerID="+managerid+" AND Password='"+managerpassString+"'";
            System.out.println(managerid+" "+managerpassString);
            Statement stat1 = conn.createStatement();
            ResultSet rs = stat1.executeQuery(query);
            System.out.println(managerid+" "+"id");

//            while (rs.next()) {
//                String no = rs.getString("Name");
//                System.out.println(no);
//            }

            if(rs.next()){
                int ID = rs.getInt("ManagerID");
                String Name = rs.getString("Name");
                Boolean Owner = rs.getBoolean("Owner");
                String Pass = rs.getString("Password");
                String About = rs.getString("About");
                
                System.out.println("Logged In");
                ManagerPageMain mpage = new ManagerPageMain(ID,Name,Owner,Pass,About);
                mpage.setVisible(true);
                this.dispose();
            }else{
            String Mquery = "SELECT * FROM employeerecord WHERE ManagerID="+managerid;
            ResultSet Mrs = stat1.executeQuery(Mquery);
                if(!Mrs.next()){
                    JOptionPane.showMessageDialog(null, "User not Exist");
//                    System.out.println("User not Exist");
                }else{
                    ResultSet prs = stat1.executeQuery(query);
                    if(!prs.next()){
                        JOptionPane.showMessageDialog(null, "Wrong Password");
//                        System.out.println("Wrong Password");
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_MloginActionPerformed

    private void MidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MidFocusGained
        Mid.setText("");
    }//GEN-LAST:event_MidFocusGained

    private void MpasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MpasswordFocusGained
        Mpassword.setText("");
    }//GEN-LAST:event_MpasswordFocusGained

    private void MidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MidFocusLost
        String t = Mid.getText();
        if ("".equals(t)) {
            Mid.setText("ID");
        }
    }//GEN-LAST:event_MidFocusLost

    private void MpasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MpasswordFocusLost
        char[] t = Mpassword.getPassword();
        if (t.length == 0) {
            Mpassword.setText("********");
        }
    }//GEN-LAST:event_MpasswordFocusLost

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void MpasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MpasswordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            MloginActionPerformed(null);
        }
    }//GEN-LAST:event_MpasswordKeyPressed

    private void MidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MidActionPerformed

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
            java.util.logging.Logger.getLogger(ManagerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Mid;
    private javax.swing.JButton Mlogin;
    private javax.swing.JPasswordField Mpassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
