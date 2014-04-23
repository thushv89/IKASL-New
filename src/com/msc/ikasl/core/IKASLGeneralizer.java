/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msc.ikasl.core;

import com.msc.enums.GenType;
import com.msc.listeners.TaskListener;
import com.msc.objects.*;
import com.msc.utils.Constants;
import com.msc.utils.LogMessages;
import com.msc.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLGeneralizer {
    
    private TaskListener tListener;
    
    public IKASLGeneralizer(TaskListener tListener){
        this.tListener = tListener;
    }
    
    public GenLayer generalize(int currLC, LearnLayer lLayer, ArrayList<String> bestHits,GenType gType){
        
        GenLayer gLayer = new GenLayer();
        
        int gID = 0;
        IKASLGenType genType = null;
        if(gType == GenType.AVG)
            genType = new AvgGenType();
        else if(gType == GenType.FUZZY)
            genType = new FuzzyGenType();
        
        for(String hit : bestHits){
            String parentID;
            String loc;
            
            //tokenize string 'hit' and get parent ID and Map x,y of hit
            String[] hTokens = hit.split(Constants.NODE_TOKENIZER);
            parentID = hTokens[0];
            loc = hTokens[1];
            
            Map<String,LNode> mapWithHit = lLayer.getSingleMapCopyWithParent(parentID);
            LNode hitnode = mapWithHit.get(loc);
            
            ArrayList<String> neigh1 = get1stLvlNeighbors(mapWithHit, loc);
            ArrayList<String> neigh2 = get2ndLvlNeighbors(mapWithHit, loc);
            
            ArrayList<Node> neigh1Nodes = new ArrayList<Node>();
            ArrayList<Node> neigh2Nodes = new ArrayList<Node>();
            
            for(String s1 : neigh1){
                neigh1Nodes.add(mapWithHit.get(s1));
            }
            for(String s2 : neigh2){
                neigh2Nodes.add(mapWithHit.get(s2));
            }
            
            if(genType != null){
                double[] gWeight = genType.generalize(hitnode,neigh1Nodes,neigh2Nodes);
                if(currLC == 0){
                    GNode node = new GNode(currLC, gID, gWeight, Constants.INIT_PARENT);
                    gLayer.addNode(node);
                }else{
                    GNode node = new GNode(currLC, gID, gWeight, parentID);
                    gLayer.addNode(node);
                }
                gID++;
            }else{
                tListener.logMessage(LogMessages.NO_GEN_TYPE);
            }
        }
        
        return gLayer;
    }
    
    //return all the neighbors 1step away from the primnode
    private ArrayList<String> get1stLvlNeighbors(Map<String, LNode> map, String loc) {
        
        ArrayList<String> neigh1 = new ArrayList<String>();
        int X = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[0]);
        int Y = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[1]);
        
        String lStr = Utils.generateIndexString(X-1, Y);
        String rStr = Utils.generateIndexString(X+1, Y);
        String bStr = Utils.generateIndexString(X, Y-1);
        String tStr = Utils.generateIndexString(X, Y+1);
        String lbStr = Utils.generateIndexString(X-1, Y-1);
        String rbStr = Utils.generateIndexString(X+1, Y-1);
        String rtStr = Utils.generateIndexString(X+1, Y+1);
        String ltStr = Utils.generateIndexString(X-1, Y+1);
        String[] neighbors = {lStr,rStr,bStr,tStr,lbStr,rbStr,rtStr,ltStr};
        
        
        for(String neighbor : neighbors){
            if(map.containsKey(neighbor)){
                neigh1.add(neighbor);
            }
        }
        return neigh1;
    }
    
    //return all the neighbors 2 steps away from the primNode
    private ArrayList<String> get2ndLvlNeighbors(Map<String, LNode> map, String loc) {
        
        int X = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[0]);
        int Y = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[1]);
        
        String llbbStr = X-2 +","+ (Y-2);
        String lbbStr = X-1 + "," + (Y-2);
        String bbStr = X + "," + (Y-2);
        String rbbStr = X+1 + "," + (Y-2);
        String rrbbStr = X+2 + "," + (Y-2);
        String llbStr = X-2 + "," + (Y-1);
        String rrbStr = X+2 + "," + (Y-1);
        String rrStr = X+2 +","+Y;
        String llStr = X-2 + "," + Y;
        String lltStr = X-2 + "," + (Y+1);
        String rrtStr = X+2 + "," + (Y+1);
        String llttStr = X-2 +","+ (Y+2);
        String lttStr = X-1 + "," + (Y+2);
        String ttStr = X + "," + (Y+2);
        String rttStr = X+1 + "," + (Y+2);
        String rrttStr = X+2 + "," + (Y+2);
        
        String[] neighbors2Str = {llbbStr,lbbStr,bbStr,rbbStr,rrbbStr,llbStr,rrbStr,rrStr,llStr,lltStr,rrtStr,llttStr,lttStr,ttStr,rttStr,rrttStr};
        
        ArrayList<String> neigh2 = new ArrayList<String>();
        for(String neigh2Str : neighbors2Str){
            if(map.containsKey(neigh2Str)){
                neigh2.add(neigh2Str);
            }
        }
        
        return neigh2;
    }
}
