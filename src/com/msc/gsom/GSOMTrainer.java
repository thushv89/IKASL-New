package com.msc.gsom;

import com.msc.objects.GNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.msc.objects.LNode;
import com.msc.utils.AlgoParameters;
import com.msc.utils.Utils;

public class GSOMTrainer {

    private Map<String, LNode> nodeMap;
    private NodeGrowthHandler growthHandler;

    public GSOMTrainer() {
        nodeMap = new HashMap<String, LNode>();
        growthHandler = new NodeGrowthHandler();
    }

    public Map<String, LNode> trainNetwork(ArrayList<String> iStrings, ArrayList<double[]> iWeights) {
        initFourNodes();	//init the map with four nodes
        for (int i = 0; i < AlgoParameters.MAX_ITERATIONS; i++) {
            int k = 0;
            double learningRate = Utils.getLearningRate(i, nodeMap.size());
            double radius = Utils.getRadius(i, Utils.getTimeConst());
            for (double[] input : iWeights) {
                trainForSingleIterAndSingleInput(i, input, iStrings.get(k), learningRate, radius);
                k++;
            }
        }
        return nodeMap;
    }

    public Map<String, LNode> trainNetwork(Map<String, GNode> map, ArrayList<String> iStrings, ArrayList<double[]> iWeights) {
        
        nodeMap = new HashMap<String, LNode>();
        
        if(map == null || map.size()<=0){
            return null;
        }

        //TODO : Convert GNOde to LNode
        for(GNode n : map.values()){
            LNode initNode = new LNode(0,0, n.getWeights());
            this.nodeMap.put(Utils.generateIndexString(initNode.getX(), initNode.getY()), initNode);
        }   
        
        for (int i = 0; i < AlgoParameters.MAX_ITERATIONS; i++) {
            int k = 0;
            double learningRate = Utils.getLearningRate(i, this.nodeMap.size());
            double radius = Utils.getRadius(i, Utils.getTimeConst());
            
            for (double[] input : iWeights) {
                trainForSingleIterAndSingleInput(i, input, iStrings.get(k), learningRate, radius);
                k++;
            }
        }
        
        return this.nodeMap;
    }
    
    private void trainForSingleIterAndSingleInput(int iter, double[] input, String str, double learningRate, double radius) {

        LNode winner = Utils.selectLWinner(nodeMap, input);

        for (Map.Entry<String, LNode> entry : nodeMap.entrySet()) {
            entry.setValue(Utils.adjustNeighbourWeight(entry.getValue(), winner, input, radius, learningRate));
        }

        winner.calcAndUpdateErr(input);

        if (winner.getErrorValue() > AlgoParameters.getGT()) {
            //System.out.println("Winner "+winner.getX()+","+winner.getY()+" GT exceeded");
            adjustWinnerError(winner);
        }
    }

    //Initialization of the map. 
    //this will create 4 nodes with random weights
    private void initFourNodes() {

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                LNode initNode = new LNode(i, j, Utils.generateRandomArray(AlgoParameters.DIMENSIONS));
                nodeMap.put(Utils.generateIndexString(i, j), initNode);
            }
        }

    }

    //when a neuron wins its error value needs to be adjusted
    private void adjustWinnerError(LNode winner) {

        //on x-axis
        String nodeLeftStr = Utils.generateIndexString(winner.getX() - 1, winner.getY());
        String nodeRightStr = Utils.generateIndexString(winner.getX() + 1, winner.getY());

        //on y-axis
        String nodeTopStr = Utils.generateIndexString(winner.getX(), winner.getY() + 1);
        String nodeBottomStr = Utils.generateIndexString(winner.getX(), winner.getY());

        if (nodeMap.containsKey(nodeLeftStr)
                && nodeMap.containsKey(nodeRightStr)
                && nodeMap.containsKey(nodeTopStr)
                && nodeMap.containsKey(nodeBottomStr)) {
            distrErrToNeighbors(winner, nodeLeftStr, nodeRightStr, nodeTopStr, nodeBottomStr);
        } else {
            growthHandler.growNodes(nodeMap, winner); //NodeGrowthHandler takes over
        }
    }

    //distributing error to the neighbors of thw winning node
    private void distrErrToNeighbors(LNode winner, String leftK, String rightK, String topK, String bottomK) {
        winner.setErrorValue(AlgoParameters.getGT() / 2);
        nodeMap.get(leftK).setErrorValue(calcErrForNeighbour(nodeMap.get(leftK)));
        nodeMap.get(rightK).setErrorValue(calcErrForNeighbour(nodeMap.get(rightK)));
        nodeMap.get(topK).setErrorValue(calcErrForNeighbour(nodeMap.get(topK)));
        nodeMap.get(bottomK).setErrorValue(calcErrForNeighbour(nodeMap.get(bottomK)));
    }

    //error calculating equation for neighbours of a winner
    private double calcErrForNeighbour(LNode node) {
        return node.getErrorValue() + (AlgoParameters.getFD() * node.getErrorValue());
    }
    
    public Map<String,LNode> getMap(){
        return nodeMap;
    }
    public Map<String,LNode> getCopyMap(){
        return new HashMap<String,LNode>(nodeMap);
    }
}
