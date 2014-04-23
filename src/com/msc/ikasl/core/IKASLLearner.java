/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msc.ikasl.core;

import com.msc.gsom.GCoordAdjuster;
import com.msc.gsom.GSOMSmoothner;
import com.msc.gsom.GSOMTester;
import com.msc.gsom.GSOMTrainer;
import com.msc.listeners.TaskListener;
import com.msc.objects.GNode;
import com.msc.objects.GenLayer;
import com.msc.objects.LNode;
import com.msc.objects.LearnLayer;
import com.msc.utils.AlgoParameters;
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
public class IKASLLearner {

    private TaskListener tListener;
    private ArrayList<GNode> nonHitNodes;
    
    public IKASLLearner(TaskListener tListener){
        this.tListener = tListener;
    }
    
    public LearnLayer trainAndGetLearnLayer(int currLC, ArrayList<double[]> iWeights, ArrayList<String> iNames, GenLayer prevGLayer) {
        
        //if currLC ==0 there is no prevGLayer. So ignore that parameter
        if (currLC == 0) {
            GSOMTrainer gTrainer = new GSOMTrainer();
            gTrainer.trainNetwork(iNames, iWeights);
            Map<String, LNode> trainedMap = gTrainer.getCopyMap();

            GSOMSmoothner gSmoother = new GSOMSmoothner();
            gSmoother.smoothGSOM(trainedMap, iWeights);
            
            GCoordAdjuster gAdjuster = new GCoordAdjuster();
            Map<String, LNode> adjustedMap = gAdjuster.adjustMapCoords(trainedMap);

            GSOMTester gTester = new GSOMTester();
            gTester.testGSOM(adjustedMap, iWeights, iNames);
            
            LearnLayer lLayer = new LearnLayer();
            lLayer.addMap(Constants.INIT_PARENT, adjustedMap);
            return lLayer;
        } //else, prevGLayer is required
        else {
            
            restoreHitValues(prevGLayer);
            
            if (prevGLayer == null) {
                tListener.logMessage(LogMessages.NO_GLAYER);
            }

            Map<String, ArrayList<double[]>> gNodeIWeights = new HashMap<String, ArrayList<double[]>>();
            Map<String, ArrayList<String>> gNodeINames = new HashMap<String, ArrayList<String>>();
            assignInputsToGNodes(prevGLayer, iWeights, iNames, gNodeIWeights, gNodeINames);
            
            //need to update nodes before Training GSOM 
            //becuase prevGLayer gets modified when GSOMTrain is called
            updateNonHitList(prevGLayer.getMap());
            
            LearnLayer lLayer = new LearnLayer();
            
            for (Map.Entry<String, GNode> n : prevGLayer.getMap().entrySet()) {
                GSOMTrainer gTrainer = new GSOMTrainer();
                gTrainer.trainNetwork(gNodeINames.get(n.getKey()), gNodeIWeights.get(n.getKey()));
                Map<String, LNode> trainedMap = gTrainer.getCopyMap();

                GSOMSmoothner gSmoother = new GSOMSmoothner();
                gSmoother.smoothGSOM(trainedMap, gNodeIWeights.get(n.getKey()));

                GCoordAdjuster gAdjuster = new GCoordAdjuster();
                Map<String, LNode> adjustedMap = gAdjuster.adjustMapCoords(trainedMap);
            
                GSOMTester gTester = new GSOMTester();
                gTester.testGSOM(adjustedMap, gNodeIWeights.get(n.getKey()), gNodeINames.get(n.getKey()));
                
                lLayer.addMap(n.getKey(), adjustedMap);
            }
            
            
            
            return lLayer;


        }
    }

    private void restoreHitValues(GenLayer gLayer){
        for(Map.Entry<String,GNode> e : gLayer.getMap().entrySet()){
            e.getValue().setHitValue(0);
        }
    }
    
    private void assignInputsToGNodes(GenLayer prevGLayer, ArrayList<double[]> iWeights, ArrayList<String> iNames,
        Map<String, ArrayList<double[]>> gNodeIWeights, Map<String, ArrayList<String>> gNodeINames) {

        Map<String, GNode> prevGNodes = prevGLayer.getMap();

        for (String s : prevGNodes.keySet()) {
            gNodeIWeights.put(s, new ArrayList<double[]>());
            gNodeINames.put(s, new ArrayList<String>());
        }

        for (int i = 0; i < iWeights.size(); i++) {
            double minDist = Double.MAX_VALUE;
            String gID = "";
            for (Map.Entry<String, GNode> n : prevGNodes.entrySet()) {
                double distance = Utils.calcEucDist(iWeights.get(i), n.getValue().getWeights(), AlgoParameters.DIMENSIONS);
                if (distance < minDist) {
                    minDist = distance;
                    gID = n.getKey();
                }
            }

            //get relevant entry from gNodeIweights and gNodeInames, update the entry and put it back to map
            ArrayList<double[]> weightArrForInput = gNodeIWeights.get(gID);
            weightArrForInput.add(iWeights.get(i));
            gNodeIWeights.put(gID, weightArrForInput);

            ArrayList<String> nameArrForInput = gNodeINames.get(gID);
            nameArrForInput.add(iNames.get(i));
            gNodeINames.put(gID, nameArrForInput);
            
            //update hit value
            GNode node = prevGNodes.get(gID);
            node.increaseHitVal();
            prevGNodes.put(gID,node);

        }
    }

    public void updateNonHitList(Map<String,GNode> map){
        if(nonHitNodes==null){
            nonHitNodes = new ArrayList<GNode>();
        }
        for(Map.Entry<String,GNode> entry : map.entrySet()){
            if(entry.getValue().getHitValue()==0){
                nonHitNodes.add(entry.getValue());
            }
        }
    }
    
    public ArrayList<GNode> getNonHitNodes(int currLC) {
        return nonHitNodes;
    }
}
