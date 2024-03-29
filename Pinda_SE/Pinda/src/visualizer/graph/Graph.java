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
 *                 Roberto Pinho <robertopinho@yahoo.com.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package visualizer.graph;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.graph.scalar.QuerySolver;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import visualizer.corpus.Corpus;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.projection.ProjectionData;
import visualizer.projection.distance.Euclidean;
import visualizer.util.PExConstants;
import visualizer.datamining.clustering.HierarchicalClustering;
import visualizer.datamining.clustering.HierarchicalClusteringType;
import visualizer.topic.TopicData;

/**
 * @author Fernando Vieira Paulovich
 *
 * This class represents a graph, with vertices and edges
 */
public class Graph implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Return the size of the graph, i.e., the maximum height and width.
     * @return The size of the graph
     */
    public java.awt.Dimension getSize() {
        if (this.vertex.size() > 0) {
            float maxX = vertex.get(0).getX();
            float minX = vertex.get(0).getX();
            float maxY = vertex.get(0).getY();
            float minY = vertex.get(0).getY();

            //Encontra o maior e menor valores para X e Y
            for (Vertex v : this.vertex) {
                if (maxX < v.getX()) {
                    maxX = v.getX();
                } else {
                    if (minX > v.getX()) {
                        minX = v.getX();
                    }
                }

                if (maxY < v.getY()) {
                    maxY = v.getY();
                } else {
                    if (minY > v.getY()) {
                        minY = v.getY();
                    }
                }
            }

            int w = (int) (maxX + (Vertex.getRayBase() * 5)) + 350;
            int h = (int) (maxY + (Vertex.getRayBase() * 5));

            return new java.awt.Dimension(w, h);
        } else {
            return new java.awt.Dimension(0, 0);
        }
    }

    /**
     * Returns the vertex which occupies the (x,y) position. If this position
     * is not ocuppied by any vertex, returns null.
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The v which occupy the (x,y) position
     */
    public Vertex getVertexByPosition(int x, int y) {
        for (Vertex v : this.vertex) {
            if (v.isInside(x, y)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Draw the graph on a graphical device. 
     * @param connectivity The connectivity to be drawn
     * @param g2 The graphical device
     */
    public void draw(Connectivity connectivity, BufferedImage image, boolean highquality) {
        if (connectivity != null) {
            if (connectivity != null) {
                ArrayList<Edge> edges = connectivity.getEdges();

                //Draw each edges of the graph
                if (this.isEdgeVisible) {
                    for (Edge edge : edges) {
                        edge.draw(image, null, highquality);
                    }
                }
            }
        }

        //Draw each vertice of the graph
        for (Vertex v : this.vertex) {
            if (!v.isValid()) {
                v.draw(image, null, highquality);
            }
        }

        for (Vertex v : this.vertex) {
            if (v.isValid() && !v.isSelected()) {
                v.draw(image, null, highquality);
            }
        }

        for (Vertex v : this.vertex) {
            if (v.isValid() && v.isSelected()) {
                v.draw(image, null, highquality);
            }
        }
    }

    public void draw(Connectivity connectivity, Graphics2D g2, boolean highquality) {
        if (connectivity != null) {
            if (connectivity != null) {
                ArrayList<Edge> edges = connectivity.getEdges();

                //Draw each edges of the graph
                if (this.isEdgeVisible) {
                    for (Edge edge : edges) {
                        edge.draw(null, g2, highquality);
                    }
                }
            }
        }

        //Draw each vertice of the graph
        for (Vertex v : this.vertex) {
            if (!v.isValid()) {
                v.draw(null, g2, highquality);
            }
        }

        for (Vertex v : this.vertex) {
            if (v.isValid() && !v.isSelected()) {
                v.draw(null, g2, highquality);
            }
        }

        for (Vertex v : this.vertex) {
            if (v.isValid() && v.isSelected()) {
                v.draw(null, g2, highquality);
            }
        }
    }

    /**
     * Returns the v which composes the graph. 
     * @return The v of the graph
     */
    public ArrayList<Vertex> getVertex() {
        return vertex;
    }

    /**
     * Changes the vertex which composes the graph (normalize to fit on 
     * the panel) 
     * @param vertex The new v for the graph
     */
    public void setVertex(ArrayList<Vertex> vertex) {
        this.vertex = vertex;
        java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension d = tk.getScreenSize();
        this.normalizeVertex(Vertex.getRayBase() * 5 + 10,
                ((float) (d.getHeight())) / 1.65f);
    }

    public void getNeighbors(ArrayList<Vertex> neighborsVertex,
            ArrayList<Edge> neighborsEdges, Connectivity connectivity,
            Vertex vertex, int depth) {

        if (connectivity != null) {
            ArrayList<Edge> edges = connectivity.getEdges();

            ArrayList<Vertex> visitedVertex = new ArrayList<Vertex>();
            ArrayList<Vertex> vertexToVisit = new ArrayList<Vertex>();
            vertexToVisit.add(vertex);

            for (int i = 0; i < depth; i++) {
                for (int j = 0; j < vertexToVisit.size(); j++) {
                    Vertex v = vertexToVisit.get(j);

                    for (Edge e : edges) {
                        if (e.getSource().getId() == v.getId() &&
                                !neighborsVertex.contains(e.getTarget())) {
                            neighborsVertex.add(e.getTarget());
                            visitedVertex.add(e.getTarget());

                            if (neighborsEdges != null && !neighborsEdges.contains(e)) {
                                neighborsEdges.add(e);
                            }
                        }

                        if (e.getTarget().getId() == v.getId() &&
                                !neighborsVertex.contains(e.getSource())) {
                            neighborsVertex.add(e.getSource());
                            visitedVertex.add(e.getSource());

                            if (neighborsEdges != null && !neighborsEdges.contains(e)) {
                                neighborsEdges.add(e);
                            }
                        }
                    }
                }

                vertexToVisit = visitedVertex;
                visitedVertex = new ArrayList<Vertex>();
            }

            neighborsVertex.remove(vertex);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Scalar createQueryScalar(String word) throws IOException {
        if (this.corpus == null) {
            throw new IOException("The corpus must be loaded!");
        }

        //Adding a new scalar
        String scalarName = "'" + word + "'";
        Scalar scalar = this.addScalar(scalarName);

        QuerySolver qS = new QuerySolver(this.corpus, this.vertex);
        qS.createCdata(word, scalar);

        return scalar;
    }

    public ArrayList<Connectivity> getConnectivities() {
        return this.connectivities;
    }

    public Connectivity getConnectivityByName(String name) {
        for (Connectivity c : this.connectivities) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public void addConnectivity(Connectivity connectivity) {
        this.removeConnectivity(connectivity);
        this.connectivities.add(connectivity);
    }

    public void removeConnectivity(Connectivity connectivity) {
        this.connectivities.remove(connectivity);
    }

    public void normalizeVertex(float begin, float end) {
        float maxX = vertex.get(0).getX();
        float minX = vertex.get(0).getX();
        float maxY = vertex.get(0).getY();
        float minY = vertex.get(0).getY();

        //Encontra o maior e menor valores para X e Y
        for (Vertex v : this.vertex) {
            if (maxX < v.getX()) {
                maxX = v.getX();
            } else {
                if (minX > v.getX()) {
                    minX = v.getX();
                }
            }

            if (maxY < v.getY()) {
                maxY = v.getY();
            } else {
                if (minY > v.getY()) {
                    minY = v.getY();
                }
            }
        }

        ///////Fazer a largura ficar proporcional a altura
        float endX = ((maxX - minX) * end);
        if (maxY != minY) {
            endX = ((maxX - minX) * end) / (maxY - minY);
        }
        //////////////////////////////////////////////////

        //Normalizo
        for (Vertex v : this.vertex) {
            if (maxX != minX) {
                v.setX((((v.getX() - minX) / (maxX - minX)) *
                        (endX - begin)) + begin);
            } else {
                v.setX(begin);
            }

            if (maxY != minY) {
                v.setY(((((v.getY() - minY) / (maxY - minY)) *
                        (end - begin)) + begin));
            } else {
                v.setY(begin);
            }

        }
    }

    public Scalar addScalar(String name) {
        Scalar scalar = new Scalar(name);

        if (!this.scalars.contains(scalar)) {
            this.scalars.add(scalar);
        }

        scalar.setIndex(this.scalars.indexOf(scalar));

        return this.scalars.get(scalar.getIndex());
    }

    public ArrayList<Scalar> getScalars() {
        return this.scalars;
    }

    public Scalar getScalarByName(String name) {
        for (Scalar s : this.scalars) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public void removeScalar(Scalar scalar) {
        //removing the scalar from the vertex
        for (Vertex v : this.vertex) {
            v.removeScalar(scalar);
        }

        //removing the scalar from the graph and update the indexes
        this.scalars.remove(scalar);
        for (int i = 0; i < this.scalars.size(); i++) {
            this.scalars.get(i).setIndex(i);
        }
    }

    public int getTitleIndex(String name) {
        return this.titles.indexOf(name);
    }

    public int addTitle(String name) {
        if (!this.titles.contains(name)) {
            this.titles.add(name);
        }
        return this.titles.indexOf(name);
    }

    public ArrayList<String> getTitles() {
        return this.titles;
    }

    public void removeTitle(String title) {
        int index = this.titles.indexOf(title);

        //removing the scalar from the graph and update the indexes
        this.titles.remove(title);

        //removing the scalar from the vertex
        for (Vertex v : this.vertex) {
            v.removeTile(index);
        }
    }

    public void changeTitle(String title) {
        int index = this.getTitleIndex(title);

        for (Vertex v : this.getVertex()) {
            v.changeTitle(index);
        }
    }

    public ProjectionData getProjectionData() {
        return pData;
    }

    public Scalar createHC(HierarchicalClusteringType type) throws IOException {
        float[][] projection = new float[this.getVertex().size()][];

        for (int i = 0; i < this.getVertex().size(); i++) {
            projection[i] = new float[2];
            projection[i][0] = this.getVertex().get(i).getX();
            projection[i][1] = this.getVertex().get(i).getY();
        }

        DenseMatrix dproj = new DenseMatrix();
        for (int i = 0; i < projection.length; i++) {
            dproj.addRow(new DenseVector(projection[i]));
        }

        HierarchicalClustering hc = new HierarchicalClustering(type);
        float[] hcScalars = hc.getPointsHeight(dproj, new Euclidean());

        String scalarname = PExConstants.SLINK;
        if (type == HierarchicalClusteringType.ALINK) {
            scalarname = PExConstants.ALINK;
        } else if (type == HierarchicalClusteringType.CLINK) {
            scalarname = PExConstants.CLINK;
        }

        Scalar scalar = this.addScalar(scalarname);

        for (int i = 0; i < this.getVertex().size(); i++) {
            this.getVertex().get(i).setScalar(scalar, hcScalars[i]);
        }

        return scalar;
    }

    public void exportCorpus(String newCorpusName, Scalar scalar, boolean reverse) {
        if (this.corpus != null) {
            ArrayList<Vertex> newvertex = new ArrayList<Vertex>();

            for (Vertex v : this.vertex) {
                if (scalar == null || scalar.getIndex() == -1 ||
                        ((reverse && v.getNormalizedScalar(scalar) <= 0.1f) ||
                        (!reverse && v.getNormalizedScalar(scalar) >= 0.1f))) {
                    newvertex.add(v);
                }
            }

            this.exportCorpus(newCorpusName, newvertex);
        }
    }

    public void exportCorpus(String newCorpusName, ArrayList<Vertex> vertex) {
        if (this.corpus != null) {
            ZipOutputStream zout = null;

            try {
                FileOutputStream dest = new FileOutputStream(newCorpusName);
                zout = new ZipOutputStream(new BufferedOutputStream(dest));
                zout.setMethod(ZipOutputStream.DEFLATED);

                for (Vertex v : vertex) {
                    if (v.getUrl() != null) {
                        ZipEntry entry = new ZipEntry(v.getUrl());
                        zout.putNextEntry(entry);

                        String filecontent = this.corpus.getFullContent(v.getUrl());
                        zout.write(filecontent.getBytes(), 0, filecontent.length());
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {

                if (zout != null) {
                    try {
                        zout.flush();
                        zout.finish();
                        zout.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void removeVertex(ArrayList<Vertex> vertex) {
        for (int i = 0; i < vertex.size(); i++) {
            Vertex v = vertex.get(i);

            //remove the v
            this.vertex.remove(v);

            for (Connectivity con : this.connectivities) {
                if (con != null) {
                    ArrayList<Edge> edges = con.getEdges();

                    if (edges != null) {
                        for (int j = 0; j < edges.size(); j++) {
                            if (edges.get(j).getSource() == v ||
                                    edges.get(j).getTarget() == v) {
                                con.getEdges().remove(j);
                                j--;
                            }
                        }
                    }
                }
            }
        }

        //adjust the v ids
        for (int i = 0; i < this.vertex.size(); i++) {
            this.vertex.get(i).setId(i);
        }
    }

    public Graph cutGraph(ArrayList<Vertex> vertex) {
        try {
            //map old v into new v
            HashMap<Vertex, Vertex> vertexMap = new HashMap<Vertex, Vertex>();
            ArrayList<Vertex> newVertex = new ArrayList<Vertex>();

            Graph graph = new Graph();
            graph.corpus = this.corpus;
            graph.pData = (ProjectionData) this.pData.clone();

            //creating all scalars
            for (Scalar s : this.getScalars()) {
                graph.addScalar(s.getName());
            }

            //creating all titles
            for (String t : this.getTitles()) {
                graph.addTitle(t);
            }

            int nrValidVertex = 0;
            for (int i = 0; i < vertex.size(); i++) {
                Vertex v = vertex.get(i);
                Vertex newV = new Vertex(i);
                newV.setColor(new java.awt.Color(v.getColor().getRGB()));
                newV.setUrl(v.getUrl());
                newV.setX(v.getX());
                newV.setY(v.getY());
                newV.setRayFactor(v.getRayFactor());
                newV.setValid(v.isValid());

                for (String t : this.titles) {
                    int index = graph.getTitleIndex(t);
                    v.changeTitle(index);
                    newV.setTitle(index, v.toString());
                }

                for (int j = 0; j < this.scalars.size(); j++) {
                    newV.setScalar(graph.scalars.get(j), v.getScalar(graph.scalars.get(j)));
                }

                newVertex.add(newV);
                vertexMap.put(v, newV);

                if (newV.isValid()) {
                    nrValidVertex++;
                }
            }

            graph.setVertex(newVertex);
            graph.pData.setNumberObjects(nrValidVertex);

            //creating all connectivities
            for (Connectivity oldCon : this.connectivities) {
                ArrayList<Edge> oldEdges = oldCon.getEdges();

                Connectivity newCon = new Connectivity(oldCon.getName());
                ArrayList<Edge> newEdges = new ArrayList<Edge>();

                for (int i = 0; i < vertex.size(); i++) {
                    Vertex v = vertex.get(i);

                    if (oldEdges != null) {
                        for (Edge e : oldEdges) {
                            if (e.getSource() == v && vertex.contains(e.getTarget())) {
                                newEdges.add(new Edge(e.getLength(),
                                        vertexMap.get(e.getSource()), vertexMap.get(e.getTarget())));
                            }

                            if (e.getTarget() == v && vertex.contains(e.getSource())) {
                                newEdges.add(new Edge(e.getLength(),
                                        vertexMap.get(e.getSource()), vertexMap.get(e.getTarget())));
                            }
                        }
                    }
                }

                newCon.setEdges(newEdges);
                graph.addConnectivity(newCon);
            }

            return graph;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public boolean isEdgeVisible() {
        return this.isEdgeVisible;
    }

    public void setEdgeVisible(boolean isEdgeVisible) {
        this.isEdgeVisible = isEdgeVisible;
    }

    public TopicData getTopicData() {
        return this.tdata;
    }

    public Scalar createScalarByConnection(Connectivity connectivity) {
        HashMap<Vertex, Float> vDegree = new HashMap<Vertex, Float>();

        if (connectivity != null) {
            for (Edge e : connectivity.getEdges()) {
                Vertex source = e.getSource();
                if (!vDegree.containsKey(source)) {
                    vDegree.put(source, 1.0f);
                } else {
                    vDegree.put(source, vDegree.get(source) + 1);
                }

                Vertex target = e.getTarget();
                if (!vDegree.containsKey(target)) {
                    vDegree.put(target, 1.0f);
                } else {
                    vDegree.put(target, vDegree.get(target) + 1);
                }
            }
        }

        Scalar scalar = this.addScalar(connectivity.getName());
        for (Vertex v : vDegree.keySet()) {
            v.setScalar(scalar, vDegree.get(v));
        }

        return scalar;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    public void perturb() {
        Random rand = new Random(7);

        float maxx = Float.NEGATIVE_INFINITY;
        float minx = Float.POSITIVE_INFINITY;
        float maxy = Float.NEGATIVE_INFINITY;
        float miny = Float.POSITIVE_INFINITY;

        for (Vertex v : this.vertex) {
            if (maxx < v.getX()) {
                maxx = v.getX();
            }

            if (minx > v.getX()) {
                minx = v.getX();
            }

            if (maxy < v.getY()) {
                maxy = v.getY();
            }

            if (miny > v.getY()) {
                miny = v.getY();
            }
        }

        float diffx = (maxx - minx) / 1000;
        float diffy = (maxy - miny) / 1000;

        for (int i = 0; i < vertex.size(); i++) {
            vertex.get(i).setX(vertex.get(i).getX() + diffx * rand.nextFloat());
            vertex.get(i).setY(vertex.get(i).getY() + diffy * rand.nextFloat());
        }
    }
    protected Corpus corpus;
    protected TopicData tdata = new TopicData(this);
    protected boolean isEdgeVisible = true;
    protected ProjectionData pData = new ProjectionData();
    protected ArrayList<Scalar> scalars = new ArrayList<Scalar>();
    protected ArrayList<String> titles = new ArrayList<String>();
    protected String description = ""; //A description of this graph
    protected ArrayList<Vertex> vertex = new ArrayList<Vertex>();
    protected ArrayList<Connectivity> connectivities = new ArrayList<Connectivity>();
}
