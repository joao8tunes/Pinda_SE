/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 *  
@inproceedings{paulovich2007pex,
author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
Minghim},
title = {The Projection Explorer: A Flexible Tool for Projection-based 
Multidimensional Visualization},
booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
Computer Graphics and Image Processing (SIBGRAPI 2007)},
year = {2007},
isbn = {0-7695-2996-8},
pages = {27--34},
doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
publisher = {IEEE Computer Society},
address = {Washington, DC, USA},
}
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.projection.distance.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.DissimilarityType;
import visualizer.util.OpenDialog;
import visualizer.util.filefilter.DATAFilter;
import visualizer.util.filefilter.DMATFilter;
import visualizer.view.ProjectionExplorerView;

/**
 *
 * @author  Fernando Vieira Paulovich
 */
public class SimilarityMatrixView extends javax.swing.JDialog {

    /** Creates new form ClustersDistancesView */
    private SimilarityMatrixView(java.awt.Frame parent) {
        super(parent);
        initComponents();

        for (DissimilarityType disstype : DissimilarityType.getTypes()) {
            if (disstype != DissimilarityType.KOLMOGOROV && disstype != DissimilarityType.NONE) {
                this.distanceComboBox.addItem(disstype);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        sourceButtonGroup = new javax.swing.ButtonGroup();
        dataPanel = new javax.swing.JPanel();
        chooseDistanceTypePanel2 = new javax.swing.JPanel();
        distanceComboBox = new javax.swing.JComboBox();
        sourcePanel = new javax.swing.JPanel();
        pointsRadioButton = new javax.swing.JRadioButton();
        distanceMatrixRadioButton = new javax.swing.JRadioButton();
        distanceMatrixTextField = new javax.swing.JTextField();
        pointsTextField = new javax.swing.JTextField();
        pointsButton = new javax.swing.JButton();
        distanceMatrixButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        generateButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Similarity Matrix View");
        setModal(true);
        setResizable(false);

        dataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));
        dataPanel.setLayout(new java.awt.GridBagLayout());

        chooseDistanceTypePanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose the Distance Type"));
        chooseDistanceTypePanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        chooseDistanceTypePanel2.add(distanceComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        dataPanel.add(chooseDistanceTypePanel2, gridBagConstraints);

        sourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Source File"));
        sourcePanel.setLayout(new java.awt.GridBagLayout());

        sourceButtonGroup.add(pointsRadioButton);
        pointsRadioButton.setSelected(true);
        pointsRadioButton.setText("Points File");
        pointsRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pointsRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pointsRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pointsRadioButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        sourcePanel.add(pointsRadioButton, gridBagConstraints);

        sourceButtonGroup.add(distanceMatrixRadioButton);
        distanceMatrixRadioButton.setText("Distance File");
        distanceMatrixRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        distanceMatrixRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        distanceMatrixRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distanceMatrixRadioButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        sourcePanel.add(distanceMatrixRadioButton, gridBagConstraints);

        distanceMatrixTextField.setColumns(35);
        distanceMatrixTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        sourcePanel.add(distanceMatrixTextField, gridBagConstraints);

        pointsTextField.setColumns(35);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        sourcePanel.add(pointsTextField, gridBagConstraints);

        pointsButton.setText("Search...");
        pointsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pointsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        sourcePanel.add(pointsButton, gridBagConstraints);

        distanceMatrixButton.setText("Search...");
        distanceMatrixButton.setEnabled(false);
        distanceMatrixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distanceMatrixButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        sourcePanel.add(distanceMatrixButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        dataPanel.add(sourcePanel, gridBagConstraints);

        getContentPane().add(dataPanel, java.awt.BorderLayout.CENTER);

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(generateButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(closeButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void pointsRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointsRadioButtonActionPerformed
        this.distanceMatrixButton.setEnabled(false);
        this.distanceMatrixTextField.setEnabled(false);

        this.pointsButton.setEnabled(true);
        this.pointsTextField.setEnabled(true);

        this.distanceMatrixTextField.setText("");
        this.distanceComboBox.setEnabled(true);
    }//GEN-LAST:event_pointsRadioButtonActionPerformed

    private void distanceMatrixRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distanceMatrixRadioButtonActionPerformed
        this.distanceMatrixButton.setEnabled(true);
        this.distanceMatrixTextField.setEnabled(true);

        this.pointsButton.setEnabled(false);
        this.pointsTextField.setEnabled(false);

        this.pointsTextField.setText("");
        this.distanceComboBox.setEnabled(false);
    }//GEN-LAST:event_distanceMatrixRadioButtonActionPerformed

    private void pointsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointsButtonActionPerformed
        int result = OpenDialog.showOpenDialog(new DATAFilter(), this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = OpenDialog.getFilename();
            this.pointsTextField.setText(filename);
        }
    }//GEN-LAST:event_pointsButtonActionPerformed

    private void distanceMatrixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distanceMatrixButtonActionPerformed
        int result = OpenDialog.showOpenDialog(new DMATFilter(), this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = OpenDialog.getFilename();
            this.distanceMatrixTextField.setText(filename);
        }
    }//GEN-LAST:event_distanceMatrixButtonActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        if (this.pointsRadioButton.isSelected()) {
            if (this.pointsTextField.getText().trim().length() > 0) {
                try {
                    Matrix matrix = MatrixFactory.getInstance(this.pointsTextField.getText());

                    DissimilarityType mtype = (DissimilarityType) this.distanceComboBox.getSelectedItem();
                    Dissimilarity diss = DissimilarityFactory.getInstance(mtype);

                    DistanceMatrix dmat = new DistanceMatrix(matrix, diss);
                    smv = SimilarityMatrixViewer.getInstance(dmat, pexview);
                    this.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(DistanceHistogramView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "A points file must be selected.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (this.distanceMatrixRadioButton.isSelected()) {
            if (this.distanceMatrixTextField.getText().trim().length() > 0) {
                try {
                    DistanceMatrix dmat = new DistanceMatrix(this.distanceMatrixTextField.getText());
                    smv = SimilarityMatrixViewer.getInstance(dmat, pexview);
                    this.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(DistanceHistogramView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "A distance matrix file must be selected.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_generateButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    public static SimilarityMatrixView getInstance(ProjectionExplorerView pexview) {
        SimilarityMatrixView ins = new SimilarityMatrixView(pexview);
        ins.pexview = pexview;
        return ins;
    }

    public SimilarityMatrixViewer display() {
        this.pack();
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);

        return smv;
    }

    private SimilarityMatrixViewer smv;
    private ProjectionExplorerView pexview;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel chooseDistanceTypePanel2;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JComboBox distanceComboBox;
    private javax.swing.JButton distanceMatrixButton;
    private javax.swing.JRadioButton distanceMatrixRadioButton;
    private javax.swing.JTextField distanceMatrixTextField;
    private javax.swing.JButton generateButton;
    private javax.swing.JButton pointsButton;
    private javax.swing.JRadioButton pointsRadioButton;
    private javax.swing.JTextField pointsTextField;
    private javax.swing.ButtonGroup sourceButtonGroup;
    private javax.swing.JPanel sourcePanel;
    // End of variables declaration//GEN-END:variables
}
