/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msc.objects;

import com.msc.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class GenLayer extends Layer{
    
    private Map<String, GNode> map;
    
    public GenLayer(Map<String,GNode> map){
        this.map = map;
    }
    
    public GenLayer(){
        map = new HashMap<String, GNode>();
    }
    
    public void addNode(GNode node){
        getMap().put(Utils.generateIndexString(node.getLc(), node.getId()), node);
    }
    
    public void addNodes(ArrayList<GNode> nodes){
        for(GNode n : nodes){
            addNode(n);
        }
    }

    /**
     * @return the map
     */
    public Map<String, GNode> getMap() {
        return map;
    }
    
    public Map<String, GNode> getCopyMap() {
        return new HashMap<String, GNode>(map);
    }
}
