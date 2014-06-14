/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msc.ikasl.auxi;

import com.msc.objects.GNode;
import com.msc.objects.GenLayer;
import com.msc.utils.Constants;
import com.msc.utils.Utils;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class InterLinkGenerator {

    private ArrayList<String> getIntersectionLinks1(GenLayer currLayer, Map<String, String> currInputs,
            GenLayer prevLayer, Map<String, String> prevInputs, double thresh) {

        ArrayList<String> links = new ArrayList<String>();

        for (GNode gn1 : currLayer.getMap().values()) {
            String gn1ID = Utils.generateIndexString(gn1.getLc(), gn1.getId());
            if (currInputs.get(gn1ID) != null && !currInputs.get(gn1ID).isEmpty()) {
                String[] in1 = currInputs.get(gn1ID).split(Constants.INPUT_TOKENIZER);
                double intersection;
                String bestMatchID = "";
                for (GNode gn2 : prevLayer.getMap().values()) {
                    String gn2ID = Utils.generateIndexString(gn2.getLc(), gn2.getId());
                    if (prevInputs.get(gn2ID) != null && !prevInputs.get(gn2ID).isEmpty()) {
                        String[] in2 = prevInputs.get(gn2ID).split(Constants.INPUT_TOKENIZER);
                        intersection = getIntersection(in1, in2);
                        if (intersection > thresh) {
                            //Make sure you get the bestMatchID exactly same as in the getIntersectionLinks1 method
                            //because we need to ignore duplicates
                            //SOLUTION: make sure the node ID with lower LC is in front
                            bestMatchID = gn2ID + Constants.NODE_TOKENIZER + gn1ID;
                            links.add(bestMatchID);
                        }
                    }
                }
                
            }
        }
        return links;
    }

    private ArrayList<String> getIntersectionLinks2(GenLayer currLayer, Map<String, String> currInputs,
            GenLayer prevLayer, Map<String, String> prevInputs, double thresh) {

        ArrayList<String> links = new ArrayList<String>();

        for (GNode gn1 : prevLayer.getMap().values()) {
            String gn1ID = Utils.generateIndexString(gn1.getLc(), gn1.getId());
            if (prevInputs.get(gn1ID) != null && !prevInputs.get(gn1ID).isEmpty()) {
                String[] in1 = prevInputs.get(gn1ID).split(Constants.INPUT_TOKENIZER);
                double intersection;
                String bestMatchID = "";
                for (GNode gn2 : currLayer.getMap().values()) {
                    String gn2ID = Utils.generateIndexString(gn2.getLc(), gn2.getId());
                    if (currInputs.get(gn2ID) != null && !currInputs.get(gn2ID).isEmpty()) {
                        String[] in2 = currInputs.get(gn2ID).split(Constants.INPUT_TOKENIZER);
                        intersection = getIntersection(in1, in2);
                        if (intersection > thresh) {
                            //Make sure you get the bestMatchID exactly same as in the getIntersectionLinks1 method
                            //because we need to ignore duplicates
                            //SOLUTION: make sure the node ID with lower LC is in front
                            bestMatchID = gn1ID + Constants.NODE_TOKENIZER + gn2ID;
                            links.add(bestMatchID);
                        }
                    }
                }
                
            }
        }
        return links;
    }

    public ArrayList<String> getAllIntsectLinks(GenLayer currLayer, Map<String, String> currInputs,
            GenLayer prevLayer, Map<String, String> prevInputs, double thresh) {
        ArrayList<String> intsect1 = getIntersectionLinks1(currLayer, currInputs, prevLayer, prevInputs, thresh);
        ArrayList<String> intsect2 = getIntersectionLinks2(currLayer, currInputs, prevLayer, prevInputs, thresh);

        HashSet<String> linkSet = new HashSet<String>();
        linkSet.addAll(intsect1);
        linkSet.addAll(intsect2);
        
        ArrayList<String> allLinks = new ArrayList<String>(linkSet);
        
        return allLinks;
    }

    private double getIntersection(String[] in1, String[] in2) {
        int downcount = 0;
        for (String s1 : in1) {
            for (String s2 : in2) {
                if (s1.equals(s2)) {
                    downcount++;
                    break;
                }
            }
        }

        double downPercent = downcount * 100.0 / in1.length;
        return downPercent;
    }
    
    private double getEffectiveThreshold(double thresh){
        double minThresh = 30;
        double maxThresh = thresh;
        
        return 0;
    }
    
    public ArrayList<String> getFullLinks(ArrayList<String> links, int minLength, int minCount){
        ArrayList<String> linksCopy = new ArrayList<String>(links);
        Map<String,ArrayList<Integer>> paChiCount = new HashMap<String, ArrayList<Integer>>();
        HashSet<String> allNodes = new HashSet<String>();
        for(int i=0;i<linksCopy.size();i++){
            String[] tokens = linksCopy.get(i).split(Constants.NODE_TOKENIZER);
            for(String s : tokens){
                if(s.contains(Constants.PARENT_TOKENIZER)){
                    String[] pTokens = s.split(Constants.PARENT_TOKENIZER);
                    for(String t : pTokens){
                        allNodes.add(t);
                    }
                }else{
                    allNodes.add(s);
                }
            }
        }
        
        //get parent child count for each node
        for(String s : allNodes){
            ArrayList<Integer> paChiVals = new ArrayList<Integer>();
            int paVal = 0;
            int chiVal = 0;
            for(int i=0;i<linksCopy.size();i++){
                String[] tokens = linksCopy.get(i).split(Constants.NODE_TOKENIZER);
                if(tokens[0].contains(s)){
                    chiVal++;
                }else if(tokens[1].contains(s)){
                    paVal++;
                }
            }
            paChiVals.add(paVal);
            paChiVals.add(chiVal);
            
            paChiCount.put(s, paChiVals);
        }
        
        for(int i=0;i<linksCopy.size();i++){
            
        }
        return null;
    }
    
}
