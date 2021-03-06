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
 * Contributor(s): Roberto Pinho <robertopinho@yahoo.com.br>, Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author  Fernando Vieira Paulovich
 */
public class FileViewer extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    /** Creates new form TextViewer */
    private FileViewer(javax.swing.JFrame parent) {
        super(parent);
        initComponents();
        this.setModal(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        textAreaScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        filelabelPanel = new javax.swing.JPanel();
        filelabelField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("File Viewer");
        closeButton.setText("Close");
        closeButton.setAutoscrolls(true);
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(closeButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        textAreaScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("File Content"));
        textAreaScrollPane.setAutoscrolls(true);
        textArea.setColumns(70);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setRows(20);
        textArea.setWrapStyleWord(true);
        textArea.setAutoscrolls(false);
        textAreaScrollPane.setViewportView(textArea);

        getContentPane().add(textAreaScrollPane, java.awt.BorderLayout.CENTER);

        filelabelPanel.setLayout(new java.awt.BorderLayout());

        filelabelPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("File Label"));
        filelabelField.setEditable(false);
        filelabelPanel.add(filelabelField, java.awt.BorderLayout.CENTER);

        getContentPane().add(filelabelPanel, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    public static FileViewer getInstance(javax.swing.JFrame parent) {
        if (instance == null) {
            instance = new FileViewer(parent);
        }
        return instance;
    }

    public void display(String label, String corporaFilename, String filename) {
        textArea.setText("");
        filelabelField.setText(label);
        filelabelField.setCaretPosition(0);

        ZipFile zip = null;
        try {
            zip = new ZipFile(corporaFilename);
            ZipEntry entry = zip.getEntry(filename);
            if (entry != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));

                String line;
                while ((line = in.readLine()) != null) {
                    textArea.append(line + "\n");
                }

                //Posiciona no in�cio da �rea de texto
                textArea.setCaretPosition(0);
                this.pack();
                this.setLocationRelativeTo(this.getParent());
                this.setVisible(true);
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new FileViewer(null).setVisible(true);
            }

        });
    }

    private static FileViewer instance;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JTextField filelabelField;
    private javax.swing.JPanel filelabelPanel;
    private javax.swing.JTextArea textArea;
    private javax.swing.JScrollPane textAreaScrollPane;
    // End of variables declaration//GEN-END:variables
}
