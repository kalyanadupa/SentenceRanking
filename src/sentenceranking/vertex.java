/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenceranking;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Abhishek
 */
public class vertex {
    int no;
    Double auth;
    Double hub;
    List<vertex> neighbours = new ArrayList<vertex>();
    List<Double> cosSim = new ArrayList<Double>();
    
}
