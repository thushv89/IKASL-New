/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msc.ikasl.core;

import com.msc.enums.GenType;
import com.msc.input.NumericalDataParser;
import com.msc.listeners.TaskListener;
import com.msc.objects.GNode;
import com.msc.objects.GenLayer;
import com.msc.objects.LNode;
import com.msc.objects.LearnLayer;
import com.msc.utils.AlgoParameters;
import com.msc.utils.Constants;
import com.msc.utils.LogMessages;
import com.msc.utils.Utils;
import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLRun {

    private int currLC = 0;
    private ArrayList<GenLayer> allGLayers;
    private ArrayList<Map<String,String>> allGNodeInputs;
    private ArrayList<ArrayList<double[]>> allIWeights;
    private ArrayList<ArrayList<String>> allINames;
    
    private NumericalDataParser parser;
    private IKASLLearner learner;
    private IKASLGeneralizer generalizer;
    private TaskListener tListener;
    
    private String dir;
    
    public IKASLRun(TaskListener tListener){
        this.tListener = tListener;
        
        allGLayers = new ArrayList<GenLayer>();
        allGNodeInputs = new ArrayList<Map<String, String>>();
        
        allINames = new ArrayList<ArrayList<String>>();
        allIWeights = new ArrayList<ArrayList<double[]>>();
        
        parser = new NumericalDataParser(tListener);
        learner = new IKASLLearner(tListener);
        generalizer = new IKASLGeneralizer(tListener);
    
    }
    
    public IKASLRun(TaskListener tListener, String dir) {
        allGLayers = new ArrayList<GenLayer>();
        allGNodeInputs = new ArrayList<Map<String, String>>();
        
        allINames = new ArrayList<ArrayList<String>>();
        allIWeights = new ArrayList<ArrayList<double[]>>();
        
        parser = new NumericalDataParser(tListener);
        learner = new IKASLLearner(tListener);
        generalizer = new IKASLGeneralizer(tListener);
        
        this.dir = dir;
    }

    public void setDir(String dir){
        this.dir = dir;
    }
    
    public void runAllSteps() {
        for (int i = 0; i < AlgoParameters.LEARN_CYCLES; i++) {
        }
    }

    public void runSingleStep() {

        parser.parseInput(dir + File.separator + "input" + (currLC+1) + ".txt");
        ArrayList<double[]> iWeights = parser.getWeights();
        iWeights = parser.normalizeWithBounds(iWeights,AlgoParameters.MIN_BOUNDS,AlgoParameters.MAX_BOUNDS);
        
        ArrayList<String> iNames = parser.getStrForWeights();
        allIWeights.add(iWeights);
        allINames.add(iNames);

        tListener.logMessage(LogMessages.SEPARATOR);
        tListener.logMessage(LogMessages.LEARN_CYCLE_PREFIX+" "+currLC);
        
        if (currLC == 0) {
            //run the GSOM algorithm and output LearnLayer
            LearnLayer initLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, null);

            //run IKASL aggregation and output GenLayer
            ArrayList<String> bestHits = getHitNodeIDs(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer initGLayer = generalizer.generalize(currLC, initLLayer, bestHits, AlgoParameters.gType);
            
            //add it to allGLayers
            allGLayers.add(initGLayer);
            
            mapInputsToGNodes(initGLayer, allIWeights.get(currLC), allINames.get(currLC));
        } else {
            //get currLC-1 genLayer
            GenLayer prevGLayer = allGLayers.get(currLC-1);
            
            //create a copy of prevGLayer to avoid modificatiosn to existing layer
            //call IKASLLearner.learn(genLayer(currLC-1)) and output LearnLayer
            GenLayer copyOfPrevGLayer = new GenLayer(prevGLayer.getCopyMap());
            LearnLayer currLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, copyOfPrevGLayer);
            
            //call IKASLAggregator.aggregate(learnLayer) and output Genlayer(currLC)
            ArrayList<String> bestHits = getHitNodeIDs(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, AlgoParameters.gType);
            currGLayer.addNodes(learner.getNonHitNodes(currLC));
            
            String msg = "Non-Hit Nodes (LC="+(currLC-1)+"): ";
            for(GNode n : learner.getNonHitNodes(currLC)){
                msg += n.getParentID()+Constants.NODE_TOKENIZER+Utils.generateIndexString(n.getLc(), n.getId())+" ";
            }
            tListener.logMessage(msg);
            
            //add Genlayer(currLC) to allGLayers
            allGLayers.add(currGLayer);
            
            mapInputsToGNodes(currGLayer, allIWeights.get(currLC), allINames.get(currLC));
        }
        currLC++;
    }

    public void clearAll(){
        currLC = 0;
        allGLayers.clear();
        allGNodeInputs.clear();
        allINames.clear();
        allIWeights.clear();
        
    }
    
    private void mapInputsToGNodes(GenLayer gLayer, ArrayList<double[]> prevIWeights, ArrayList<String> prevINames){
        
        Map<String, String> testResultMap = new HashMap<String, String>();
        Map<String,GNode> nodeMap = gLayer.getMap();
        
        for(int i = 0; i<prevIWeights.size();i++){
            
            GNode winner = Utils.selectGWinner(nodeMap, prevIWeights.get(i));
            //System.out.println("Winner for "+iStrings.get(i)+" is "+winner.getX()+","+winner.getY());
            
            String winnerStr = Utils.generateIndexString(winner.getLc(), winner.getId());
            String testResultKey = winner.getParentID() + Constants.NODE_TOKENIZER + winnerStr;
            GNode winnerNode = nodeMap.get(winnerStr);
            winnerNode.increasePrevHitVal();
            
            if(!testResultMap.containsKey(testResultKey)){
                testResultMap.put(testResultKey, prevINames.get(i));
            }else{
                String currStr = testResultMap.get(testResultKey);
                String newStr = currStr +","+ prevINames.get(i);
                testResultMap.remove(winnerStr);
                testResultMap.put(testResultKey,newStr);
            }
        }
        
        allGNodeInputs.add(testResultMap);
        
        String results="";
        for(Map.Entry<String,String> entry : testResultMap.entrySet()){
            results += entry.getKey()+": "+entry.getValue()+"\n";
        }
        tListener.logMessage(results);
    }
    
    private ArrayList<String> getHitNodeIDs(LearnLayer lLayer, int hitThresh, double neighRad) {
        Map<String, Map<String, LNode>> lMap = lLayer.getCopyMap();
        
        ArrayList<String> selectedNodeIDs = new ArrayList<String>();

        double threshold = hitThresh * neighRad;

        for (Entry<String, Map<String, LNode>> eMap : lMap.entrySet()) {
            Map<String, Double> mapHitVals = new HashMap<String, Double>();
            //Check whether atleast one node exceed hit Threshold 
            //if not just ignore that map
            boolean noNodeExceedHT = true;
            for(LNode n : eMap.getValue().values()){
                if(n.getHitValue()>=hitThresh){
                    noNodeExceedHT = false;
                    break;
                }
            }
            
            if(noNodeExceedHT){
                continue;
            }
            
            ArrayList<LNode> nodes = new ArrayList<LNode>(eMap.getValue().values());
            Collections.sort(nodes, new Comparator<LNode>() {

                @Override
                public int compare(LNode o1, LNode o2) {
                    if (o1.getHitValue() > o2.getHitValue()) {
                        return -1;
                    } else if(o1.getHitValue() == o2.getHitValue()){
                        return 0;
                    }else {
                        return 1;
                    }
                }
            });

            for (int i = 0; i < nodes.size(); i++) {
                LNode n = nodes.get(i);
                String fullNodeID = eMap.getKey() + Constants.NODE_TOKENIZER + Utils.generateIndexString(n.getX(), n.getY());

                //if the current node hit value is not atleaset half of hit threshold -> ignore the node
                if(n.getHitValue()<hitThresh/2){
                    continue;
                }
                
                if (i == 0) {
                    mapHitVals.put(fullNodeID, Double.MAX_VALUE);
                } else {
                    double minDist = Double.MAX_VALUE;
                    for (int j = 0; j < i; j++) {
                        double distance = (nodes.get(i).getX() - nodes.get(j).getX()) * (nodes.get(i).getX() - nodes.get(j).getX())
                                + (nodes.get(i).getY() - nodes.get(j).getY()) * (nodes.get(i).getY() - nodes.get(j).getY());
                        distance = Math.sqrt(distance);
                        if (distance < minDist) {
                            minDist = distance;
                        }
                    }

                    if(minDist < neighRad/2 ){
                        continue;
                    }
                    
                    double hitValue = nodes.get(i).getHitValue() * minDist;
                    mapHitVals.put(fullNodeID, hitValue);
                }

                
            }

            ValueComparator bvc = new ValueComparator(mapHitVals);
            TreeMap<String, Double> sortedHitVals = new TreeMap<String, Double>(bvc);
            sortedHitVals.clear();
            sortedHitVals.putAll(mapHitVals);

            for (Entry<String, Double> e : sortedHitVals.entrySet()) {
                if (e.getValue() >= threshold) {
                    selectedNodeIDs.add(e.getKey());
                } else {
                    break;
                }
            }
        }

        return selectedNodeIDs;
    }

    public int getCurrLC(){
        return currLC;
    }
    
    public ArrayList<GenLayer> getAllGenLayers(){
        return allGLayers;
    }
    
    class ValueComparator implements Comparator<String> {

        Map<String, Double> base;

        public ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.    
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
