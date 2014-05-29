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
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLRun {

    private int currLC = 0;
    private ArrayList<GenLayer> allGLayers;
    private ArrayList<Map<String, String>> allGNodeInputs;
    private ArrayList<ArrayList<double[]>> allIWeights;
    private ArrayList<ArrayList<String>> allINames;
    private NumericalDataParser parser;
    private IKASLLearner learner;
    private IKASLGeneralizer generalizer;
    private TaskListener tListener;
    private String dir;

    public IKASLRun(TaskListener tListener) {
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

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void runAllSteps() {
        for (int i = 0; i < AlgoParameters.LEARN_CYCLES; i++) {
        }
    }

    public void runSingleStep() {

        parser.parseInput(dir + File.separator + "input" + (currLC + 1) + ".txt");
        ArrayList<double[]> iWeights = parser.getWeights();

        iWeights = parser.normalizeWithBounds(iWeights, AlgoParameters.MIN_BOUNDS, AlgoParameters.MAX_BOUNDS);
        tListener.logMessage(LogMessages.INPUTS_NORMALIZED);

        ArrayList<String> iNames = parser.getStrForWeights();
        allIWeights.add(iWeights);
        allINames.add(iNames);

        tListener.logMessage(LogMessages.SEPARATOR);
        tListener.logMessage(LogMessages.LEARN_CYCLE_PREFIX + " " + currLC);

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
            GenLayer prevGLayer = allGLayers.get(currLC - 1);

            //create a copy of prevGLayer to avoid modificatiosn to existing layer
            //call IKASLLearner.learn(genLayer(currLC-1)) and output LearnLayer
            GenLayer copyOfPrevGLayer = new GenLayer(prevGLayer.getCopyMap());
            LearnLayer currLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, copyOfPrevGLayer);

            //call IKASLAggregator.aggregate(learnLayer) and output Genlayer(currLC)
            ArrayList<String> bestHits = getHitNodeIDs(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, AlgoParameters.gType);

            //We call mapping inputs to GNodes before adding non-hit nodes from prev GLayer to new layer
            //because otherwise it is possible to non-hit node to have inputs assigned
            //Intuitively it should not happen, because it appeared as a non-hit node at the first place, 
            //because there were no inputs similar to that.
            mapInputsToGNodes(currGLayer, allIWeights.get(currLC), allINames.get(currLC));

            currGLayer.addNodes(learner.getNonHitNodes(currLC));

            //add Genlayer(currLC) to allGLayers
            allGLayers.add(currGLayer);

            getClusterPurityVector(currGLayer, prevGLayer, currLC);

        }
        currLC++;
    }

    public void clearAll() {
        currLC = 0;
        allGLayers.clear();
        allGNodeInputs.clear();
        allINames.clear();
        allIWeights.clear();

    }

    //WE haven't considered what happens when the parent node is not from the immediate previous layer, but from a layer below that
    //SOLVED Above
    private HashMap<String, Double> getClusterPurityVector(GenLayer currLayer, GenLayer prevLayer, int currLC) {
        HashMap<String, Double> purityMap = new HashMap<String, Double>();
        tListener.logMessage(LogMessages.SEPARATOR);
        tListener.logMessage("Cluster Purity Information");

        //find total number of inputs
        int totalInputs = 0;
        for (String s : allGNodeInputs.get(currLC).values()) {
            totalInputs += s.split(Constants.INPUT_TOKENIZER).length;
        }

        for (GNode n : currLayer.getMap().values()) {
            String currNodeInputs = allGNodeInputs.get(currLC).get(Utils.generateIndexString(n.getLc(), n.getId()));
            String pNodeInputs = null;

            if (!n.getParentID().contains(Constants.PARENT_TOKENIZER)) {
                for (int j = currLC - 1; j >= 0; j--) {
                    pNodeInputs = allGNodeInputs.get(j).get(n.getParentID());
                    if (pNodeInputs != null && !pNodeInputs.isEmpty()) {
                        break;
                    }
                }

            } else {
                //logic when the node has two parents
                String[] pIDs = n.getParentID().split(Constants.PARENT_TOKENIZER);
                for (int j = currLC - 1; j >= 0; j--) {
                    pNodeInputs = allGNodeInputs.get(j).get(pIDs[0]);
                    if (pNodeInputs != null && !pNodeInputs.isEmpty()) {
                        break;
                    }
                }

                for (int i = 1; i < pIDs.length; i++) {
                    for (int j = currLC - 1; j >= 0; j--) {

                        if (allGNodeInputs.get(j).get(pIDs[i]) != null && !allGNodeInputs.get(j).get(pIDs[i]).isEmpty()) {
                            pNodeInputs += "," + allGNodeInputs.get(j).get(pIDs[i]);
                            break;
                        }
                    }
                }
            }



            if (currNodeInputs != null && !currNodeInputs.isEmpty()
                    && pNodeInputs != null && !pNodeInputs.isEmpty()) {
                String key = n.getParentID() + Constants.NODE_TOKENIZER + Utils.generateIndexString(n.getLc(), n.getId());
                double value = getStringArrIntersectionPercent(currNodeInputs.split(Constants.INPUT_TOKENIZER), pNodeInputs.split(Constants.INPUT_TOKENIZER));
                purityMap.put(key, value);
                
                double weight = getIntersectionWeight(currNodeInputs.split(Constants.INPUT_TOKENIZER).length, totalInputs);
                
                DecimalFormat df = new DecimalFormat("#.###");
                tListener.logMessage(key + ": " + df.format(value) + "%" + " ("+df.format(weight)+")");
            }
        }
        tListener.logMessage(LogMessages.SEPARATOR);
        return purityMap;
    }

    //When finding intersection of current and previous, remember to consider situations like
    //if curr node has 10 units and prev node has 5 units, but curr node has all 5 of the prev node
    //introduce a penalty for additional units the curr node having
    //We do not have to worry about that according to current implementation. Because if the above scenario occur,
    //percentage will be (5/10)*100 = 50% so it's automaticall taken care of.
    
    //Taking the max as denominator can result in a low percentage if there's a branching from parent node to 2 current nodes
    //because the denominator would be the parent nodes value
    /*private double getStringArrIntersectionPercent(String[] curr, String[] prev) {
        int downcount = 0;
        for (String s1 : curr) {
            for (String s2 : prev) {
                if (s1.equals(s2)) {
                    downcount++;
                    break;
                }
            }
        }

        double downPercent = downcount * 100.0 / Math.max(curr.length,prev.length);
        return downPercent;
    }*/

    private double getStringArrIntersectionPercent(String[] curr, String[] prev) {
        int downcount = 0;
        int extraInCurr = 0;
        for (String s1 : curr) {
            for (String s2 : prev) {
                if (s1.equals(s2)) {
                    downcount++;
                    break;
                }
            }
        }

        extraInCurr = curr.length - downcount;
        
        //downPercent can go negative if the curr node has no intersection with it's parent
        double downPercent = ((downcount*1.0/prev.length)-(extraInCurr*1.0/Math.max(curr.length,prev.length))) * 100.0;
        
        return downPercent;
    }
    
    private double getIntersectionWeight(int curr, int total) {
        return curr*1.0 / total;
    }

    private void mapInputsToGNodes(GenLayer gLayer, ArrayList<double[]> prevIWeights, ArrayList<String> prevINames) {

        Map<String, String> testResultMap = new HashMap<String, String>();
        Map<String, GNode> nodeMap = gLayer.getMap();

        for (int i = 0; i < prevIWeights.size(); i++) {

            GNode winner = Utils.selectGWinner(nodeMap, prevIWeights.get(i));

            String winnerStr = Utils.generateIndexString(winner.getLc(), winner.getId());
            String testResultKey = winnerStr;
            GNode winnerNode = nodeMap.get(winnerStr);
            winnerNode.increasePrevHitVal();

            if (!testResultMap.containsKey(testResultKey)) {
                testResultMap.put(testResultKey, prevINames.get(i));
            } else {
                String currStr = testResultMap.get(testResultKey);
                String newStr = currStr + "," + prevINames.get(i);
                testResultMap.remove(winnerStr);
                testResultMap.put(testResultKey, newStr);
            }
        }

        allGNodeInputs.add(testResultMap);

        String results = "";
        for (Map.Entry<String, String> entry : testResultMap.entrySet()) {
            String parentID = gLayer.getMap().get(entry.getKey()).getParentID();
            results += parentID + Constants.NODE_TOKENIZER + entry.getKey() + ": " + entry.getValue() + "\n";
        }
        tListener.logMessage(results);
    }

    private ArrayList<String> getHitNodeIDs(LearnLayer lLayer, int hitThresh, double neighRad) {
        Map<String, Map<String, LNode>> lMap = lLayer.getCopyMap();

        ArrayList<String> selectedNodeIDs = new ArrayList<String>();

        //double threshold = hitThresh * Math.sqrt(neighRad) * Math.sqrt(AlgoParameters.MERGE_ONE_DIM_THRESHOLD*AlgoParameters.DIMENSIONS);
        double distThreshold = Utils.calcEucDist(Utils.getUniformVector(AlgoParameters.MERGE_ONE_DIM_THRESHOLD, AlgoParameters.DIMENSIONS),
                Utils.getZeroVector(AlgoParameters.DIMENSIONS), AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS);
        double threshold = hitThresh * Math.sqrt(neighRad) * Math.pow(distThreshold, 2);

        for (Entry<String, Map<String, LNode>> eMap : lMap.entrySet()) {
            Map<String, Double> mapHitVals = new HashMap<String, Double>();
            //Check whether atleast one node exceed hit Threshold 
            //if not just ignore that map
            boolean noNodeExceedHT = true;
            for (LNode n : eMap.getValue().values()) {
                if (n.getHitValue() >= hitThresh) {
                    noNodeExceedHT = false;
                    break;
                }
            }

            if (noNodeExceedHT) {
                continue;
            }

            ArrayList<LNode> nodes = new ArrayList<LNode>(eMap.getValue().values());
            Collections.sort(nodes, new Comparator<LNode>() {

                @Override
                public int compare(LNode o1, LNode o2) {
                    if (o1.getHitValue() > o2.getHitValue()) {
                        return -1;
                    } else if (o1.getHitValue() == o2.getHitValue()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

            for (int i = 0; i < nodes.size(); i++) {
                LNode n = nodes.get(i);
                String fullNodeID = eMap.getKey() + Constants.NODE_TOKENIZER + Utils.generateIndexString(n.getX(), n.getY());

                //if the current node hit value is not atleaset half of hit threshold -> ignore the node
                if (n.getHitValue() < hitThresh / 2) {
                    continue;
                }

                if (i == 0) {
                    mapHitVals.put(fullNodeID, Double.MAX_VALUE);
                } else {
                    double minPDist = Double.MAX_VALUE;
                    double minEDist = Double.MAX_VALUE;

                    for (int j = 0; j < i; j++) {
                        double pDistance = (nodes.get(i).getX() - nodes.get(j).getX()) * (nodes.get(i).getX() - nodes.get(j).getX())
                                + (nodes.get(i).getY() - nodes.get(j).getY()) * (nodes.get(i).getY() - nodes.get(j).getY());
                        pDistance = Math.sqrt(pDistance);

                        double eDistance = Utils.calcEucDist(nodes.get(i).getWeights(), nodes.get(j).getWeights(), AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS);

                        if (pDistance < minPDist) {
                            minPDist = pDistance;
                        }
                        if (pDistance < minEDist) {
                            minEDist = eDistance;
                        }
                    }

                    if (minPDist < neighRad / 2) {
                        continue;
                    }

                    double hitValue = nodes.get(i).getHitValue() * Math.sqrt(minPDist) * Math.pow(minEDist, 2);
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

    public int getCurrLC() {
        return currLC;
    }

    public ArrayList<GenLayer> getAllGenLayers() {
        return allGLayers;
    }

    public ArrayList<Map<String, String>> getAllGenLayerInputs() {
        return allGNodeInputs;
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
