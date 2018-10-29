/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinda;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author antunes
 */
public class HierarchicalCluster 
{
 
	private int id;
	private String name;
	private Object color;
    private ArrayList<Snippet> cluster;
    private ArrayList<Object> children;
    
    public HierarchicalCluster()
    {
        cluster = new ArrayList<Snippet>();
        children = new ArrayList<Object>();
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Snippet> getCluster() {
		return cluster;
	}

	public void setCluster(ArrayList<Snippet> cluster) {
		this.cluster = cluster;
	}

	public ArrayList<Object> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Object> children) {
		this.children = children;
	}
	
	public void setColor(Object color) {
		this.color = color;
	}
	
	public Object getColor() {
		return color;
	}
    
}
