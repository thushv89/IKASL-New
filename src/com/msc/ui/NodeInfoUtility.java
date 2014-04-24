/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msc.ui;

import com.msc.ikasl.core.IKASLRun;
import com.msc.objects.GNode;
import com.msc.objects.GenLayer;
import com.msc.utils.Constants;
import com.msc.utils.Utils;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

/**
 *
 * @author thushang
 */
public class NodeInfoUtility extends javax.swing.JFrame {

    private IKASLRun iRun;

    /**
     * Creates new form NodeInfoUtility
     */
    public NodeInfoUtility() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public void setData(IKASLRun iRun) {
        this.iRun = iRun;
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
        nodeWeightInTxt = new javax.swing.JTextField();
        weightsBtn = new javax.swing.JButton();
        nodeWeightResultTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        ancestorInTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        ancestorResultLbl = new javax.swing.JLabel();
        ancestorsBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        commonResultTxt = new javax.swing.JTextArea();
        commonIn1Txt = new javax.swing.JTextField();
        commonIn2Txt = new javax.swing.JTextField();
        commonBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Node Weight Display"));

        weightsBtn.setText("Display Weights");
        weightsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightsBtnActionPerformed(evt);
            }
        });

        nodeWeightResultTxt.setText("Display Node Weights");

        jLabel1.setText("->");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(weightsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nodeWeightInTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nodeWeightResultTxt)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nodeWeightInTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nodeWeightResultTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(weightsBtn)
                .addGap(34, 34, 34))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ancestors"));

        jLabel2.setText("->");

        ancestorResultLbl.setText("Display Ancestors");

        ancestorsBtn.setText("Display Ancestors");
        ancestorsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ancestorsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ancestorsBtn))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ancestorInTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ancestorResultLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ancestorInTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(ancestorResultLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ancestorsBtn))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Common Inputs"));

        commonResultTxt.setColumns(20);
        commonResultTxt.setRows(5);
        jScrollPane1.setViewportView(commonResultTxt);

        commonBtn.setText("Display Common");
        commonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(commonIn1Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(commonIn2Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addComponent(commonBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(commonIn1Txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commonIn2Txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commonBtn))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(441, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void weightsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightsBtnActionPerformed
        String result = "";
        String[] nodeStr = nodeWeightInTxt.getText().split(Constants.I_J_TOKENIZER);
        int lc = Integer.parseInt(nodeStr[0]);
        int id = Integer.parseInt(nodeStr[1]);

        GenLayer reqLayer = null;
        //The node essentially does not need to be in the GLayer with index lc,
        //It can be in any layer above lc as it can propagate to other layers as a non-hit node
        for(int i=iRun.getCurrLC()-1;i>=lc;i--){
            if(iRun.getAllGenLayers().get(i).getMap().containsKey(nodeWeightInTxt.getText())){
                reqLayer = iRun.getAllGenLayers().get(i);
                break;
            }
        }

        double[] reqWeight = reqLayer.getMap().get(Utils.generateIndexString(lc, id)).getWeights();

        for (int i = 0; i < reqWeight.length; i++) {
            DecimalFormat df = new DecimalFormat("#.000");
            result += df.format(reqWeight[i]) + ", ";
        }

        nodeWeightResultTxt.setText(result);
    }//GEN-LAST:event_weightsBtnActionPerformed

    private void ancestorsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ancestorsBtnActionPerformed
        String result = "";
        String[] nodeStr = ancestorInTxt.getText().split(Constants.I_J_TOKENIZER);
        int lc = Integer.parseInt(nodeStr[0]);
        int id = Integer.parseInt(nodeStr[1]);

        ArrayList<GenLayer> allGLayers = iRun.getAllGenLayers();

        GNode node = allGLayers.get(lc).getMap().get(ancestorInTxt.getText());
        String parentStr = node.getParentID();
        result += parentStr + " -> ";
        for (int i = allGLayers.size() - 2; i >= 0; i--) {
            if (parentStr.contains(Constants.PARENT_TOKENIZER)) {
                result += parentStr;
                break;
            } else {
                parentStr = allGLayers.get(i).getMap().get(parentStr).getParentID();
                result += parentStr + " -> ";
            }
        }

        ancestorResultLbl.setText(result);
    }//GEN-LAST:event_ancestorsBtnActionPerformed

    private void commonBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonBtnActionPerformed
        String result = "";
        String[] in1Str = commonIn1Txt.getText().split(Constants.I_J_TOKENIZER);
        int lc1 = Integer.parseInt(in1Str[0]);
        int id1 = Integer.parseInt(in1Str[1]);
        String[] in2Str = commonIn2Txt.getText().split(Constants.I_J_TOKENIZER);
        int lc2 = Integer.parseInt(in2Str[0]);
        int id2 = Integer.parseInt(in2Str[1]);
        
        String inputs1 = "";
        String inputs2 = "";
        
        for(int i=iRun.getCurrLC()-1;i>=Math.min(lc1, lc2);i--){
            if(iRun.getAllGenLayerInputs().get(i).containsKey(commonIn1Txt.getText())){
                inputs1 = iRun.getAllGenLayerInputs().get(i).get(commonIn1Txt.getText());
            }
            if(iRun.getAllGenLayerInputs().get(i).containsKey(commonIn2Txt.getText())){
                inputs2 = iRun.getAllGenLayerInputs().get(i).get(commonIn2Txt.getText());
            }
        }
        
        String[] input1Tokens = inputs1.split(Constants.I_J_TOKENIZER);
        String[] input2Tokens = inputs2.split(Constants.I_J_TOKENIZER);
        
        List<String> in1List = Arrays.asList(input1Tokens);
        List<String> in2List = Arrays.asList(input2Tokens);
        
        in1List.retainAll(in2List);
        
        
        for(String s : in1List){
            result += s+", ";
        }
        
        commonResultTxt.setText(result);
    }//GEN-LAST:event_commonBtnActionPerformed

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
            java.util.logging.Logger.getLogger(NodeInfoUtility.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NodeInfoUtility.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NodeInfoUtility.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NodeInfoUtility.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NodeInfoUtility().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ancestorInTxt;
    private javax.swing.JLabel ancestorResultLbl;
    private javax.swing.JButton ancestorsBtn;
    private javax.swing.JButton commonBtn;
    private javax.swing.JTextField commonIn1Txt;
    private javax.swing.JTextField commonIn2Txt;
    private javax.swing.JTextArea commonResultTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nodeWeightInTxt;
    private javax.swing.JTextField nodeWeightResultTxt;
    private javax.swing.JButton weightsBtn;
    // End of variables declaration//GEN-END:variables
}
