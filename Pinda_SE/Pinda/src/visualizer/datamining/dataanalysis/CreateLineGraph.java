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

package visualizer.datamining.dataanalysis;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import visualizer.util.SaveDialog;
import visualizer.util.filefilter.PNGFilter;

/**
 *
 * @author  Fernando Vieira Paulovich
 */
public class CreateLineGraph extends javax.swing.JDialog {

    /** Creates new form CreateLineGraph
     * @param parent 
     */
    public CreateLineGraph(javax.swing.JDialog parent) {
        super(parent);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonPanel = new javax.swing.JPanel();
        saveImageButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        saveImageButton.setText("Save Image");
        saveImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImageButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(saveImageButton);

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

private void saveImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveImageButtonActionPerformed
    int result = SaveDialog.showSaveDialog(new PNGFilter(), this, "image.png");

    if (result == JFileChooser.APPROVE_OPTION) {
        String filename = SaveDialog.getFilename();

        try {
            BufferedImage image = new BufferedImage(panel.getWidth(),
                    panel.getHeight(), BufferedImage.TYPE_INT_RGB);
            panel.paint(image.getGraphics());
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_saveImageButtonActionPerformed

private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
    this.setVisible(false);
}//GEN-LAST:event_closeButtonActionPerformed

    public static CreateLineGraph getInstance(javax.swing.JDialog parent) {
        return new CreateLineGraph(parent);
    }

    public void display(String filename) throws IOException {
        //title
        //xtitle
        //ytitle
        //serie_name;serie_name;...
        //value;value;...
        //value;value;...
        //...

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(new File(filename)));
            String title = br.readLine();
            String xtitle = br.readLine();
            String ytitle = br.readLine();

            //filling the names
            ArrayList<String> serienames = new ArrayList<String>();
            String names = br.readLine();
            StringTokenizer st = new StringTokenizer(names, ";");
            while (st.hasMoreTokens()) {
                serienames.add(st.nextToken());
            }

            //filling the values
            ArrayList<ArrayList<Double>> values = new ArrayList<ArrayList<Double>>();
            for (int i = 0; i < serienames.size(); i++) {
                values.add(new ArrayList<Double>());
            }

            String line = null;
            while ((line = br.readLine()) != null) {
                StringTokenizer stv = new StringTokenizer(line, ";");

                for (int i = 0; stv.hasMoreTokens(); i++) {
                    values.get(i).add(Double.parseDouble(stv.nextToken()));
                }
            }

            ArrayList<Serie> series = new ArrayList<Serie>();
            for (int i = 0; i < serienames.size(); i++) {
                CreateLineGraph.Serie serie = new Serie(serienames.get(i), values.get(i));
                series.add(serie);
            }

            display(series, title, xtitle, ytitle);
        } catch (IOException ex) {
            throw new IOException(ex);
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public void display(ArrayList<Serie> series, String title, String xtitle, String ytitle) throws IOException {
        this.freechart = this.createChart(this.createAllSeries(series), title, xtitle, ytitle);
        this.panel = new ChartPanel(freechart);
        this.getContentPane().add(panel, BorderLayout.CENTER);

        this.setPreferredSize(new Dimension(650, 400));
        this.setSize(new Dimension(650, 400));

        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }

    private JFreeChart createChart(XYDataset xydataset, String title,
            String xtitle, String ytitle) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, xtitle, ytitle,
                xydataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot xyplot = (XYPlot) chart.getPlot();
        NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);

        xyplot.setDomainGridlinePaint(Color.BLACK);
        xyplot.setRangeGridlinePaint(Color.BLACK);

        xyplot.setOutlinePaint(Color.BLACK);
        xyplot.setOutlineStroke(new BasicStroke(1.0f));
        xyplot.setBackgroundPaint(Color.white);
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);

        xyplot.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA,
                    Color.CYAN, Color.ORANGE, Color.BLACK, Color.DARK_GRAY, Color.GRAY,
                    Color.LIGHT_GRAY, Color.YELLOW
                }, DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));

        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        xylineandshaperenderer.setBaseShapesFilled(true);
        xylineandshaperenderer.setDrawOutlines(true);

        return chart;
    }

    private XYDataset createAllSeries(ArrayList<Serie> series) throws IOException {
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();

        for (int i = 0; i < series.size(); i++) {
            XYSeries xyseries = this.createSerie(series.get(i).name, series.get(i).values);
            xyseriescollection.addSeries(xyseries);
        }

        return xyseriescollection;
    }

    private XYSeries createSerie(String name, ArrayList<Double> values) {
        XYSeries xyseries = new XYSeries(name);

        for (int i = 0; i < values.size(); i++) {
            xyseries.add(i + 1, values.get(i));
        }

        return xyseries;
    }

    public static class Serie {

        public Serie(String name, ArrayList<Double> values) {
            this.name = name;
            this.values = values;
        }

        public String name;
        public ArrayList<Double> values;
    }

    public static void main(String[] args) {
        try {
            CreateLineGraph instance = CreateLineGraph.getInstance(null);
            instance.display("precision.txt");
            instance.dispose();
        } catch (IOException ex) {
            Logger.getLogger(CreateLineGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JFreeChart freechart;
    private JPanel panel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton saveImageButton;
    // End of variables declaration//GEN-END:variables
}