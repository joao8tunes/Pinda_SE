/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinda;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Vector;
import visualizer.projection.ProjectionData;
import visualizer.projection.ProjectionFactory;
import visualizer.projection.ProjectionType;
import visualizer.projection.ProjectorType;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.DissimilarityType;
//import org.apache.tomcat.jni.File;
import visualizer.datamining.clustering.BKmeans;

/**
 *
 * @author antunes
 */
public class Pinda 
{
    
	private static int clusterId = 0;
	private static HierarchicalCluster root;
	private static ArrayList<String> clustersNames;
	protected static int k = 5;
	
    public static void getHierarchicalCluster(HierarchicalCluster node, DenseMatrix dproj, ArrayList<ArrayList<Integer>> frequencies, ArrayList<String> attributes, boolean firstLevel) throws IOException
    {
        ArrayList<Integer> discartedIds;
        Color[] firstLevelColors = new Color[]{new Color(214,39,40), new Color(31,119,180), new Color(44,160,44), new Color(148,103,189), new Color(255,127,14)};
        
        if (dproj.getRowCount() > k) {
//            System.out.println("ELEMENTOS: " + node.getCluster().getElements());
            
            BKmeans hC = new BKmeans(k);
            ArrayList<ArrayList<Integer>> clusters = hC.execute(DissimilarityFactory.getInstance(DissimilarityType.COSINE_BASED), dproj);
            float decreseSaturationColor = 15f, currentSaturationColor = 0f;
            
            for (int i = 0; i < clusters.size(); i++) {    //Percorrendo os clusters.
                DenseMatrix children = new DenseMatrix();
                HierarchicalCluster newNode = new HierarchicalCluster();
                ArrayList<Integer> totalWordFrequencies = new ArrayList<Integer>();    //Frequências totais dos termos para cada cluster.
                
                discartedIds = new ArrayList<Integer>();
                newNode.setId(clusterId++);
                
                if (firstLevel) newNode.setColor(firstLevelColors[i]);
                else {
                	currentSaturationColor += decreseSaturationColor;
                	newNode.setColor(getDecreaseColor((Color) node.getColor(), currentSaturationColor));
                }
                
                children.setAttributes(dproj.getAttributes());
                node.getChildren().add(newNode);    //Adicionando novo cluster no n� atual.
                
//                System.out.println("Cluster " + i + ": " + clusters.get(i).size() + " elementos");
                
                //Inicializando contagem total das frequências dos termos dos elementos do cluster pra saber o termo q melhor representa esse cluster:
                for (int j = 0; j < attributes.size(); j++) {    
                	totalWordFrequencies.add(0);
                }

                for (int j = 0; j < clusters.get(i).size(); j++) {    //Percorrendo os elementos do cluster 'i'.
                    Integer index = clusters.get(i).get(j);

                    String arquivo = dproj.getRow(index).getId();
//                    System.out.println(arquivo);
                    newNode.getCluster().add(new Snippet(dproj.getRow(index).getId(), dproj.getRow(index).getValues()[0], dproj.getRow(index).getValues()[1], newNode.getColor()));    //Populando o �ltimo cluster com seus elementos.
                    children.addRow(dproj.getRow(index));    //Criando matriz baseada no novo cluster.
                    int indexSnippet = Integer.valueOf(arquivo.replaceFirst("snippet_", ""))-1;
                    
                    for (int l = 0; l < attributes.size(); l++) {
                    	totalWordFrequencies.set(l, totalWordFrequencies.get(l)+frequencies.get(indexSnippet).get(l));
                    }
                }
                
                int maxFreq = 0, indexMaxFreq = 0;
                //PROCURANDO TERMO COM MAIOR FREQUENCIA ENTRE OS SNIPPETS DESSE CLUSTER:
                for (int j = 0; j < attributes.size(); j++) {
                	if (totalWordFrequencies.get(j) > maxFreq) {
                		maxFreq = totalWordFrequencies.get(j);
                		indexMaxFreq = j;
                	}
                }
                
                int firstIndexMaxFreq = indexMaxFreq;    //Caso nenhum outro 
                
                while (checkClusterNameUsage(attributes.get(indexMaxFreq))) {
//                	System.out.println(">>>>>>Repetido: " + attributes.get(indexMaxFreq));
                	discartedIds.add(indexMaxFreq);
                	if (discartedIds.size() == attributes.size()) {    //Uma vez que todos os ids foram descartados, significa que não tem jeito, terá q usar um nome já existente. O melhor é usar o de 'firstIndexMaxFreq'.
                		indexMaxFreq = firstIndexMaxFreq;
                		break;
                	}
                		
                	maxFreq = 0;
                	indexMaxFreq = 0;
                    //PROCURANDO TERMO COM MAIOR FREQUENCIA ENTRE OS SNIPPETS DESSE CLUSTER:
                	//Obs.: Tenta não repetir os nomes de clusteres, 'descartando' os ids relativos aos nomes de clusters já existentes.
                    for (int j = 0; j < attributes.size(); j++) {
                    	if (totalWordFrequencies.get(j) > maxFreq  && !checkDiscartedIds(discartedIds, j)) {
                    		maxFreq = totalWordFrequencies.get(j);
                    		indexMaxFreq = j;
                    	}
                    }	
                }
                
//                System.out.println("Novo: " + attributes.get(indexMaxFreq));
                clustersNames.add(attributes.get(indexMaxFreq));    //Nome do novo cluster.
                newNode.setName(attributes.get(indexMaxFreq));
                
                getHierarchicalCluster(newNode, children, frequencies, attributes, false);
                
                if (newNode.getChildren().size() == 0) {    //Folha.
                	ArrayList<Object> leafs = new ArrayList<Object>();
                	float currentLeafSaturationColor = 0f;
                	
                	for (int j = 0; j < clusters.get(i).size(); j++) {    //Percorrendo os elementos do cluster 'i'.
                        Integer index = clusters.get(i).get(j);
                        String arquivo = dproj.getRow(index).getId();
                        
//                        System.out.println(arquivo);
                        currentLeafSaturationColor += decreseSaturationColor;
                        leafs.add(new Leaf(arquivo, 1, getDecreaseColor((Color) newNode.getColor(), currentLeafSaturationColor)));
                    }
                	
                	newNode.setChildren(leafs);
                }
//                cluster1.save("/home/antunes/cluster" + i + ".data");
//                System.out.println(i);

    //            BKmeans hCC = new BKmeans(5);HierarchicalCluster aux;
            	
    //            ArrayList<ArrayList<Integer>> clustersC = hC.execute(DissimilarityFactory.getInstance(DissimilarityType.COSINE_BASED), cluster1);
            }
        }
    }
    
    private static void depthFirstSearch(ArrayList<HierarchicalCluster2> nodes, int level, ArrayList<String> result)
    {
        ArrayList<HierarchicalCluster2> clusters = new ArrayList<HierarchicalCluster2>();
        int cluster = 0;

        //System.out.println("Level " + level);
        result.add("Level " + level);
        
        for (HierarchicalCluster2 n : nodes) {
            for (HierarchicalCluster2 h : n.getChildrens()) {
                //System.out.println("Cluster " + cluster + ": " + h.getCluster().getElements().size() + " elementos");
            	result.add("Cluster " + cluster + ": " + h.getCluster().getElements().size() + " elementos");
                ++cluster;
                
                for (Vector i : h.getCluster().getElements()) {
                    //System.out.println(i);
                	String newLine = i.getId();
                	
                	for (float j : i.getValues()) {
                		newLine += ";" + j;
                	}
                	
                	result.add(newLine);
                }
                
                //System.out.println("______________________");
                result.add("______________________");
                clusters.add(h);
            }
        }
        
        //System.out.println("###########################");
        result.add("###########################");
        //System.out.println("        " + clusters.size() + " clusters");
        result.add("        " + clusters.size() + " clusters");
        //System.out.println("###########################");
        result.add("###########################");
        
        if (clusters.size() != 0) depthFirstSearch(clusters, level+1, result);
    }
    
    protected static boolean checkClusterNameUsage(String name) 
    {
    	for (String s : clustersNames) {
    		if (s.equals(name)) return true;
    	}
	    	
    	return false;
    }
    
    protected static boolean checkDiscartedIds(ArrayList<Integer> discartedIds, int id) 
    {
    	for (Integer i : discartedIds) {
    		if (id == i) return true;
    	}
	    
    	return false;
    }    
    
    public static Color getDecreaseColor(Color color, float decreaseSaturation)
    {
    	Color newColor = color;
    	float[] hsb = new float[3];
    	
    	Color.RGBtoHSB(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), hsb);
    	newColor = new Color(hsbToRGB(hsb[0], Math.abs(hsb[1] * ((100.0f - decreaseSaturation)/100.0f)), hsb[2]));
    			
    	return newColor;
    }
    
	public static int hsbToRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
            case 0:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 1:
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 2:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
                break;
            case 3:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 4:
                r = (int) (t * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 5:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (q * 255.0f + 0.5f);
                break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }
	
	private void setRGBColorsToHex(HierarchicalCluster node)
	{
		node.setColor("#" + Integer.toHexString(((Color) node.getColor()).getRGB()).substring(2));
		
		for (Snippet s : node.getCluster()) {
			s.setColor("#" + Integer.toHexString(((Color) s.getColor()).getRGB()).substring(2));
		}
		
		if (node.getChildren().size() > 0) {
			for (Object c : node.getChildren()) {
				if (c.getClass().equals(Leaf.class)) ((Leaf) c).setColor("#" + Integer.toHexString(((Color) ((Leaf) c).getColor()).getRGB()).substring(2));
				else setRGBColorsToHex(((HierarchicalCluster) c));
			}
		}
	}
    
    public HierarchicalCluster group(String data) throws IOException
    {
    	ArrayList<String> result = new ArrayList<String>();
        HierarchicalCluster root = new HierarchicalCluster();    //Raiz da �rvore de clusters.
        DenseMatrix matrix = new DenseMatrix();
        ProjectionData pData = new ProjectionData();    //configurara a ProjectionData
        float projection[][];
        DenseMatrix dproj = new DenseMatrix();
        ArrayList<String> att = new ArrayList<String>();
        ArrayList<HierarchicalCluster2> c = new ArrayList<HierarchicalCluster2>();
        String[] vectorData = data.split("\n");
        int nrdims = Integer.valueOf(vectorData[2].trim());
        ArrayList<ArrayList<Integer>> frequencies = new ArrayList<ArrayList<Integer>>();
        
        clusterId = 0;
        clustersNames = new ArrayList<String>();
        //matrix.load("/home/antunes/matrix.data");
        
        /******************************************************************************/
        /* CRIANDO MATRIX DIRETO DA STRING, SIMULANDO O MÉTODO 'load' DE DenseMatrix: */
        /******************************************************************************/
        //vectorData[0] - DN
        //vectorData[1] - número de snippets (objetos)
        //vectorData[2] - número de termos (dimensões)
        //vectorData[3] - termos (dimensões)
        StringTokenizer t1 = new StringTokenizer(vectorData[3].trim(), ";");
        
        root.setId(clusterId++);
        root.setColor(new Color(187,187,187));

        while (t1.hasMoreTokens()) {
            String token = t1.nextToken();
            matrix.getAttributes().add(token.trim());
        }        
        
        //vectorData[4-n] - matrix de frequência

        //read the vectors
        for (int i = 4; i < vectorData.length && vectorData[i].trim().length() > 0; i++) {
        	frequencies.add(new ArrayList<Integer>());
            StringTokenizer t2 = new StringTokenizer(vectorData[i].trim(), ";");

            //read the id
            String id = t2.nextToken().trim();

            //class data
            float klass = 0.0f;

            //the vector
            float[] vector = new float[nrdims];

            int index = 0;
            
            while (t2.hasMoreTokens()) {
                String token = t2.nextToken();
                float value = Float.parseFloat(token.trim());

                
                if (index < nrdims) {
                    vector[index] = value;
                    index++;
                } 
                else throw new IOException("Vector with wrong number of dimensions!");
                
                //GUARDAR O VALOR DE CADA PALAVRA Q REPRESENTA CADA CLUSTER PRA DEPOIS COMPARAR PRA SABER QUAL DAS PALAVRAS REPRESENTA AQUELE CLUSTER:
                //CADA SNIPPET DEVE POSSUIR A FREQUENCIA DE TODAS AS PALAVRAS QUE REPRESENTAM TODOS OS SNIPPETS, PRA SABER PELO SOMATORIO QUAL PALAVRA REPRESENTA UM CLUSTER (CONJUNTO DE SNIPPETS)
                frequencies.get(frequencies.size()-1).add((int) value);
            }

            matrix.addRow(new DenseVector(vector, id, klass));
        }        
        
        pData.setDissimilarityType(DissimilarityType.EUCLIDEAN);
        pData.setProjectionType(ProjectionType.IDMAP);
        pData.setProjectorType(ProjectorType.FASTMAP);
        projection = ProjectionFactory.getInstance(ProjectionType.IDMAP).project(matrix, pData, null);
            
        att.add("X");
        att.add("Y");
        dproj.setAttributes(att);
        
        for (int i = 0; i < projection.length; i++) {
            dproj.addRow(new DenseVector(projection[i], matrix.getRow(i).getId(), matrix.getRow(i).getKlass()));
        }

        //Atribuição de elementos do cluster inicial (todos os elementos):        
        for (int i = 0; i < dproj.getRowCount(); i++) {    //Percorrendo os elementos do cluster 'i'.
            //root.getCluster().addElement(dproj.getRow(i));    //Preenchendo com todos os elementos do cluster atual (original).
            root.getCluster().add(new Snippet(dproj.getRow(i).getId(), dproj.getRow(i).getValues()[0], dproj.getRow(i).getValues()[1], root.getColor()));
        }
        
        ArrayList<Integer> totalWordFrequencies = new ArrayList<Integer>();
        
        //Inicializando contagem total das frequências dos termos dos elementos do cluster pra saber o termo q melhor representa esse cluster:
        for (int j = 0; j < matrix.getAttributes().size(); j++) {    
        	totalWordFrequencies.add(0);
        }
        
        for (int j = 0; j < dproj.getRowCount(); j++) {    //Encontrando termo mais representativo de todos os snippets.
	        for (int l = 0; l < matrix.getAttributes().size(); l++) {
	        	totalWordFrequencies.set(l, totalWordFrequencies.get(l)+frequencies.get(j).get(l));
	        }
        }
        
        int maxFreq = 0, indexMaxFreq = 0;
        //PROCURANDO TERMO MAIS FREQUENCIA ENTRE OS SNIPPETS DESSE CLUSTER:
        for (int j = 0; j < matrix.getAttributes().size(); j++) {
        	if (totalWordFrequencies.get(j) > maxFreq) {
        		maxFreq = totalWordFrequencies.get(j);
        		indexMaxFreq = j;
        	}
        }
        
        root.setName(matrix.getAttributes().get(indexMaxFreq));
//        System.out.println("Atributos:\n"+matrix.getAttributes());
        
        getHierarchicalCluster(root, dproj, frequencies, matrix.getAttributes(), true);
//        c.add(root);
//        depthFirstSearch(c, 0, result);
        setRGBColorsToHex(root);
        
        return root;
    }    
    
//    public static void main(String args[]) throws IOException
//    {
//        HierarchicalCluster root = new HierarchicalCluster();    //Raiz da �rvore de clusters.
//        DenseMatrix matrix = new DenseMatrix();
//        matrix.load("/home/antunes/matrix.data");
//            
//        ProjectionData pData = new ProjectionData();    //configurara a ProjectionData        
//        pData.setDissimilarityType(DissimilarityType.EUCLIDEAN);
//        pData.setProjectionType(ProjectionType.IDMAP);
//        pData.setProjectorType(ProjectorType.FASTMAP);
//        float projection[][] = ProjectionFactory.getInstance(ProjectionType.IDMAP).project(matrix, pData, null);
//            
//        DenseMatrix dproj = new DenseMatrix();
//        ArrayList<String> att = new ArrayList<String>();
//        att.add("X");
//        att.add("Y");
//        dproj.setAttributes(att);
//        
//        for (int i = 0; i < projection.length; i++) {
//            //dproj.addRow(new DenseVector(projection[i], matrix.getRow(i).getId()));
//            dproj.addRow(new DenseVector(projection[i], matrix.getRow(i).getId(), matrix.getRow(i).getKlass()));
//        }
//        
////        String filename = "/home/antunes/arquivoDeSaida.data";
////        dproj.save(filename);
//        
//        for (int i = 0; i < dproj.getRowCount(); i++) {    //Percorrendo os elementos do cluster 'i'.
//            root.getCluster().addElement(dproj.getRow(i).getId());    //Preenchendo com todos os elementos do cluster atual (original).
//        }
//        
//        getHierarchicalCluster(root, dproj);
//        ArrayList<HierarchicalCluster> c = new ArrayList<HierarchicalCluster>();
//        c.add(root);
//        depthFirstSearch(c, 0);
//    }
}