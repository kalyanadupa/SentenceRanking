/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenceranking;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Abhishek
 */
public class hits {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String input = "India, officially the Republic of India (Bhārat Gaṇarājya),[13][14][c] is a country in South Asia. It is the seventh-largest country by area, the second-most populous country with over 1.2 billion people, and the most populous democracy in the world. Bounded by the Indian Ocean on the south, the Arabian Sea on the south-west, and the Bay of Bengal on the south-east, it shares land borders with Pakistan to the west;[d] China, Nepal, and Bhutan to the north-east; and Myanmar (Burma) and Bangladesh to the east. In the Indian Ocean, India is in the vicinity of Sri Lanka and the Maldives; in addition, India's Andaman and Nicobar Islands share a maritime border with Thailand and Indonesia.\n" +
"\n" +
"Home to the ancient Indus Valley Civilisation and a region of historic trade routes and vast empires, the Indian subcontinent was identified with its commercial and cultural wealth for much of its long history.[15] Four religions—Hinduism, Buddhism, Jainism, and Sikhism—originated here, whereas Zoroastrianism and the Abrahamic religions of Judaism, Christianity, and Islam arrived in the 1st millennium CE and also shaped the region's diverse culture. Gradually annexed by and brought under the administration of the British East India Company from the early 18th century and administered directly by the United Kingdom after the Indian Rebellion of 1857, India became an independent nation in 1947 after a struggle for independence that was marked by non-violent resistance led by Mahatma Gandhi.\n" +
"\n" +
"The Indian economy is the world's seventh-largest by nominal GDP and third-largest by purchasing power parity (PPP).[10] Following market-based economic reforms in 1991, India became one of the fastest-growing major economies; it is considered a newly industrialised country. However, it continues to face the challenges of poverty, corruption, malnutrition, inadequate public healthcare, and terrorism. A nuclear weapons state and a regional power, it has the third-largest standing army in the world and ranks ninth in military expenditure among nations. India is a federal constitutional republic governed under a parliamentary system consisting of 29 states and 7 union territories. India is a pluralistic, multilingual, and a multi-ethnic society. It is also home to a diversity of wildlife in a variety of protected habitats.";
        int threshold = 15;
        int steps = 3;
        String[] token = input.split("(?<=[.!?])\\s* ");
        List<String> allStrings = new ArrayList<String>();
        for(String k : token){
            allStrings.add(k);
        }
        
        List<vertex> graph = new ArrayList<vertex>();

        
        
        //*************************Finding Cosine Similary Graph**********************//
        CosineSimilarity cs = new CosineSimilarity();

        
        List<String> tempStrings = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        for (String str : allStrings) {
            if (str.length() > 5) {
                str = str.replaceAll("[\\p{Punct}&&[^'-.]]+", " ");
                tempStrings.add(str);
            }
        }

        allStrings = tempStrings;
        double[][] similarity = new double[allStrings.size()][allStrings.size()];
        vertex x;
        for (int i = 0; i < allStrings.size(); i++) {
            x = new vertex();
            graph.add(x);
        }
        for (int i = 0; i < allStrings.size(); i++) {
            graph.get(i).auth = 1.0;
            graph.get(i).hub = 1.0;
            graph.get(i).no = i;
        }
        
        for (int i = 0; i < (allStrings.size()); i++) {
            for (int j = i; j < (allStrings.size()); j++) {
                double sim = cs.CosineSimilarity_Score(allStrings.get(i), allStrings.get(j));
                double si = sim * 100;
                int num = (int) si;
//                System.out.println("num = "+ num+ "  sim "+sim);
                if ((i != j) && (sim != 0) && (!Double.isNaN(sim)) && (sim >= (threshold / 100)) && (num > 0)) {
                    similarity[i][j] = sim;
                    graph.get(i).neighbours.add(graph.get(j));
                    graph.get(i).cosSim.add(sim);
                    
                } 
            }
        }
        
        //*************************Finding Hub score**********************//
        Double norm;
        
        
        for(int i = 1 ; i < steps ; i++){
            norm = 0.0;
            for(vertex p : graph){
                p.auth = 0.0;
                for(vertex q: p.neighbours){
                    p.auth = p.auth + p.cosSim.get(p.neighbours.indexOf(q))*q.hub;
                }
                norm = norm + (p.auth*p.auth);
            }
            norm = Math.sqrt(norm);
            for(vertex p: graph){
                p.auth = p.auth/norm;
            }
            norm  = 0.0;
            for(vertex p : graph){
                p.hub = 0.0;
                for(vertex q: p.neighbours){
                    p.hub = p.hub + p.cosSim.get(p.neighbours.indexOf(q))*q.auth;
                }
                norm = norm + (p.hub*p.hub);
            }
            norm = Math.sqrt(norm);
            for(vertex p: graph){
                p.hub = p.hub/norm;
            }
            
        }
        for(String po : allStrings)
            System.out.println(allStrings.indexOf(po) + " : " +po);
        for(vertex p : graph)
            System.out.println(graph.indexOf(p)+" : "+p.hub);
        
            
        
        
        
    }
    
}



