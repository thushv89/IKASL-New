package com.msc.gsom;

import com.msc.objects.LNode;
import com.msc.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GSOMTester {

    private Map<String,String> testResultMap;

    public GSOMTester() {
        testResultMap = new HashMap<String, String>();
    }
    
    public void testGSOM(Map<String,LNode> nodeMap,ArrayList<double[]> iWeights,ArrayList<String> iStrings){
        for(int i = 0; i<iWeights.size();i++){
            
            LNode winner = Utils.selectLWinner(nodeMap, iWeights.get(i));
            //System.out.println("Winner for "+iStrings.get(i)+" is "+winner.getX()+","+winner.getY());
            
            String winnerStr = Utils.generateIndexString(winner.getX(), winner.getY());
            LNode winnerNode = nodeMap.get(winnerStr);
            winnerNode.setHitValue(winner.getHitValue()+1);
            
            if(!testResultMap.containsKey(winnerStr)){
                testResultMap.put(winnerStr, iStrings.get(i));
            }else{
                String currStr = getTestResultMap().get(winnerStr);
                String newStr = currStr +","+ iStrings.get(i);
                testResultMap.remove(winnerStr);
                testResultMap.put(winnerStr,newStr);
            }
        }
        
    }

    /**
     * @return the testResultMap
     */
    public Map<String,String> getTestResultMap() {
        return testResultMap;
    }
}
