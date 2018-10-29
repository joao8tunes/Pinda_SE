/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinda;

import java.util.ArrayList;

/**
 *
 * @author antunes
 */
public class HierarchicalCluster2 
{
 
    private Cluster2 cluster;
    private ArrayList<HierarchicalCluster2> childrens;
    
    public HierarchicalCluster2()
    {
        cluster = new Cluster2();
        childrens = new ArrayList<HierarchicalCluster2>();
    }
    
    public Cluster2 getCluster()
    {
        return cluster;
    }
    
    public ArrayList<HierarchicalCluster2> getChildrens()
    {
        return childrens;
    }
    
}
