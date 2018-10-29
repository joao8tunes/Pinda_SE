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

package visualizer.wizard;

import javax.swing.JFileChooser;
import visualizer.dimensionreduction.DimensionalityReductionType;
import visualizer.matrix.normalization.NormalizationType;
import visualizer.projection.ProjectionData;
import visualizer.util.SaveDialog;
import visualizer.util.filefilter.DATAFilter;

/**
 *
 * @author  Fernando Vieira Paulovich
 */
public class DimensionReduction extends WizardPanel {

    /** Creates new form DimensionReduction
     * @param projectionData 
     */
    public DimensionReduction(ProjectionData projectionData) {
        this.pdata = projectionData;
        initComponents();

        for (NormalizationType nt : NormalizationType.getTypes()) {
            this.normalizationComboBox.addItem(nt);
        }

        for (DimensionalityReductionType drt : DimensionalityReductionType.getTypes()) {
            this.dimensionalityReductionComboBox.addItem(drt);
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

        parametersPanel = new javax.swing.JPanel();
        dimensionsTextField = new javax.swing.JTextField();
        dimensionsLabel = new javax.swing.JLabel();
        dimensionalityReductionComboBox = new javax.swing.JComboBox();
        normalPanel = new javax.swing.JPanel();
        normalizationComboBox = new javax.swing.JComboBox();
        saveDocumetsTermsMatrixCheckBox = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Dimensionality Reduction"));
        setLayout(new java.awt.GridBagLayout());

        parametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Dimensionality reduction technique"));
        parametersPanel.setLayout(new java.awt.GridBagLayout());

        dimensionsTextField.setColumns(5);
        dimensionsTextField.setText("15");
        dimensionsTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        parametersPanel.add(dimensionsTextField, gridBagConstraints);

        dimensionsLabel.setText("dimensions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        parametersPanel.add(dimensionsLabel, gridBagConstraints);

        dimensionalityReductionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dimensionalityReductionComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        parametersPanel.add(dimensionalityReductionComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(parametersPanel, gridBagConstraints);

        normalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Normalization"));
        normalPanel.add(normalizationComboBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(normalPanel, gridBagConstraints);

        saveDocumetsTermsMatrixCheckBox.setText("Save documents x terms matrix");
        saveDocumetsTermsMatrixCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        saveDocumetsTermsMatrixCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        saveDocumetsTermsMatrixCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDocumetsTermsMatrixCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(saveDocumetsTermsMatrixCheckBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

private void dimensionalityReductionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dimensionalityReductionComboBoxActionPerformed
    DimensionalityReductionType drt = (DimensionalityReductionType) this.dimensionalityReductionComboBox.getSelectedItem();

    if (drt != DimensionalityReductionType.NONE) {
        this.dimensionsTextField.setEnabled(true);
    } else {
        this.dimensionsTextField.setEnabled(false);
    }
}//GEN-LAST:event_dimensionalityReductionComboBoxActionPerformed

private void saveDocumetsTermsMatrixCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDocumetsTermsMatrixCheckBoxActionPerformed
    if (this.saveDocumetsTermsMatrixCheckBox.isSelected()) {
        String filename = this.pdata.getSourceFile();

        int result = SaveDialog.showSaveDialog(new DATAFilter(), this, filename);

        if (result == JFileChooser.APPROVE_OPTION) {
            filename = SaveDialog.getFilename();
            this.pdata.setDocsTermsFilename(filename);
        } else {
            this.saveDocumetsTermsMatrixCheckBox.setSelected(false);
        }
    } else {
        this.pdata.setDocsTermsFilename("");
    }
}//GEN-LAST:event_saveDocumetsTermsMatrixCheckBoxActionPerformed

    public DimensionReduction reset() {
        this.dimensionalityReductionComboBox.setSelectedItem(DimensionalityReductionType.NONE);
        this.dimensionsTextField.setEnabled(false);
        this.normalizationComboBox.setSelectedItem(NormalizationType.NONE);

        return this;
    }

    @Override
    public void refreshData() {
        DimensionalityReductionType drt = (DimensionalityReductionType) this.dimensionalityReductionComboBox.getSelectedItem();
        this.pdata.setDimensionReductionType(drt);

        if (drt != DimensionalityReductionType.NONE) {
            this.pdata.setTargetDimension(Integer.parseInt(this.dimensionsTextField.getText()));
        } else {
            this.pdata.setTargetDimension(0);
        }

        this.pdata.setNormalization((NormalizationType) this.normalizationComboBox.getSelectedItem());
    }

    private ProjectionData pdata;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox dimensionalityReductionComboBox;
    private javax.swing.JLabel dimensionsLabel;
    private javax.swing.JTextField dimensionsTextField;
    private javax.swing.JPanel normalPanel;
    private javax.swing.JComboBox normalizationComboBox;
    private javax.swing.JPanel parametersPanel;
    private javax.swing.JCheckBox saveDocumetsTermsMatrixCheckBox;
    // End of variables declaration//GEN-END:variables
}