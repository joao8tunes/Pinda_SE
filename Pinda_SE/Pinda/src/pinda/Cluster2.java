/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinda;

import java.util.ArrayList;

import visualizer.matrix.Vector;

/**
 *
 * @author antunes
 */
public class Cluster2 
{
    
    private ArrayList<Vector> elements;    //Id dos snippets desse cluster;
    
    public Cluster2()
    {
        elements = new ArrayList<Vector>();
    }
    
    public ArrayList<Vector> getElements()
    {
        return elements;
    }
    
    public void addElement(Vector element)
    {
        elements.add(element);
    }
    
}
