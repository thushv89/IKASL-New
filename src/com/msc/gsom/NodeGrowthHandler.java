package com.msc.gsom;

import java.util.Map;
import com.msc.objects.LNode;
import com.msc.utils.AlgoParameters;
import com.msc.utils.ArrayHelper;
import com.msc.utils.Utils;

public class NodeGrowthHandler {
    //This class is responsible of growing nodes in the GSOM map.
    //It'll check the free spaces around the winning neuron and grow new nodes accordingly
    //Also, it'll assign optimal weights for the new neurons
	Map<String,LNode> map;
	
	public void growNodes(Map<String,LNode> map, LNode winner){
		this.map = map;
		
		int X = winner.getX();
		int Y = winner.getY();
		
		for(int i=X-1;i<=X+1;i=i+2){
			String nodeStr = Utils.generateIndexString(i, Y);
			if(!map.containsKey(nodeStr)){
				//grow new node
				LNode newNode = new LNode(i, Y, getNewNodeWeights(winner, i, Y));
				//System.out.println("Node "+X+","+Y+" grown from Node "+i+","+Y);
				map.put(Utils.generateIndexString(i, Y), newNode);
			}
		}
		for(int i=Y-1;i<=Y+1;i=i+2){
			String nodeStr = Utils.generateIndexString(X, i);
			if(!map.containsKey(nodeStr)){
				//grow new node
				LNode newNode = new LNode(X, i, getNewNodeWeights(winner, X, i));
				//System.out.println("Node "+X+","+Y+" grown from Node "+X+","+i);
				map.put(Utils.generateIndexString(X, i), newNode);
			}
		}
		return;
	}
	
	//calc and get weights for the new node
	private double[] getNewNodeWeights(LNode winner, int X, int Y){
		
		double[] newWeights= new double[AlgoParameters.DIMENSIONS];
		
		if(winner.getY()==Y){
			//two consecutive nodes 
			
			//winnerX,otherX
			if(X == winner.getX()+1){
				String nextNodeStr = Utils.generateIndexString(X+1, Y); //nX
				String othrSideNodeStr = Utils.generateIndexString(X-2, Y); //oX
				String topNodeStr = Utils.generateIndexString(winner.getX(), Y+1);  //tX
				String botNodeStr = Utils.generateIndexString(winner.getX(), Y-1);  //bX
				
				//new node has one direct neighbor, 
				//but neighbor has a neighbor in the opposing directly
                                //wX,X,nX
				if(map.containsKey(nextNodeStr)){
					newWeights = newWeightsForNewNodeInMiddle(winner, nextNodeStr);
				}
                                //oX,wX,X
				else if(map.containsKey(othrSideNodeStr)){
					//2 consecutive nodes on right
					newWeights = newWeightsForNewNodeOnOneSide(winner, othrSideNodeStr);
				}
                                //   tX
                                //wX, X
				else if(map.containsKey(topNodeStr)){
					//right and top nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, topNodeStr);
				}
                                //wX, X
                                //   bX
				else if(map.containsKey(botNodeStr)){
					//right and bottom nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, botNodeStr);
				}
				else {
					newWeights = newWeightsForNewNodeOneOlderNeighbor(winner);
				}
			}
			//otherX,winnerX
			else if(X == winner.getX()-1){
				String nextNodeStr = Utils.generateIndexString(X-1, Y);
				String othrSideNodeStr = Utils.generateIndexString(X+2, Y);
				String topNodeStr = Utils.generateIndexString(winner.getX(), Y+1);
				String botNodeStr = Utils.generateIndexString(winner.getX(), Y-1);
				
				//new node has one direct neighbor, 
				//but neighbor has a neighbor in the opposing directly
                                //nX,X,wX
				if(map.containsKey(nextNodeStr)){
					newWeights = newWeightsForNewNodeInMiddle(winner, nextNodeStr);
				}
                                //X,wX,oX
				else if(map.containsKey(othrSideNodeStr)){
					//2 consecutive nodes on left
					newWeights = newWeightsForNewNodeOnOneSide(winner, othrSideNodeStr);
				}
				else if(map.containsKey(topNodeStr)){
					//left and top nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, topNodeStr);
				}
				else if(map.containsKey(botNodeStr)){
					//left and bottom nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, botNodeStr);
				}
				else {
					newWeights = newWeightsForNewNodeOneOlderNeighbor(winner);
				}
			}
			
			
			//new node is in the middle of two older nodes
		}else if(winner.getX()==X){
			
			//otherY
			//winnerY
			if(Y == winner.getY()+1){
				String nextNodeStr = Utils.generateIndexString(X, Y+1);
				String othrSideNodeStr = Utils.generateIndexString(X, Y-2);
				String leftNodeStr = Utils.generateIndexString(X-1, winner.getY());
				String rightNodeStr = Utils.generateIndexString(X+1, winner.getY());
				
				//new node has one direct neighbor, 
				//but neighbor has a neighbor in the opposing directly
                                //nY
                                // Y
                                //wY
				if(map.containsKey(nextNodeStr)){
					newWeights = newWeightsForNewNodeInMiddle(winner, nextNodeStr);
				}
				else if(map.containsKey(othrSideNodeStr)){
					//2 consecutive nodes upwards
					newWeights = newWeightsForNewNodeOnOneSide(winner, othrSideNodeStr);
				}
				else if(map.containsKey(leftNodeStr)){
					//left and top nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, leftNodeStr);
				}
				else if(map.containsKey(rightNodeStr)){
					//right and top nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, rightNodeStr);
				}
				else {
					newWeights = newWeightsForNewNodeOneOlderNeighbor(winner);
				}
			}
			//winnerY
			//otherY
			else if(Y == winner.getY()-1){
				String nextNodeStr = Utils.generateIndexString(X, Y-1);
				String othrSideNodeStr = Utils.generateIndexString(X, Y+2);
				String leftNodeStr = Utils.generateIndexString(X-1, winner.getY());
				String rightNodeStr = Utils.generateIndexString(X+1, winner.getY());
				
				//new node has one direct neighbor, 
				//but neighbor has a neighbor in the opposing directly
                                
                                //wY
                                // Y
                                //nY
				if(map.containsKey(nextNodeStr)){
					newWeights = newWeightsForNewNodeInMiddle(winner, nextNodeStr);
				}
				else if(map.containsKey(othrSideNodeStr)){
					//2 consecutive nodes on left
					newWeights = newWeightsForNewNodeOnOneSide(winner, othrSideNodeStr);
				}
				else if(map.containsKey(leftNodeStr)){
					//left and top nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, leftNodeStr);
				}
				else if(map.containsKey(rightNodeStr)){
					//left and bottom nodes
					newWeights = newWeightsForNewNodeOnOneSide(winner, rightNodeStr);
				}
				else {
					newWeights = newWeightsForNewNodeOneOlderNeighbor(winner);
				}
			}
		}
		
		for(int i=0;i<AlgoParameters.DIMENSIONS;i++){
			if(newWeights[i]<0){
				newWeights[i]=0;
			}
                        if(newWeights[i]>1){
                            newWeights[i]=1;
                        }
		}
		return newWeights;
	}
	
	//node1,new_node,node2
	private double[] newWeightsForNewNodeInMiddle(LNode winner, String otherNodeIdx){
		double[] newWeights;
		LNode otherNode = map.get(otherNodeIdx);
		newWeights = ArrayHelper.add(winner.getWeights(), otherNode.getWeights(),AlgoParameters.DIMENSIONS);
		newWeights = ArrayHelper.multiplyArrayByConst(newWeights,0.5);
		return newWeights;
	}
	
	//node1,node2,new_node or new_node,node1,node2
	private double[] newWeightsForNewNodeOnOneSide(LNode winner, String otherNodeIdx){
		double[] newWeights;
		LNode otherNode = map.get(otherNodeIdx);
		newWeights = ArrayHelper.multiplyArrayByConst(winner.getWeights(), 2);
                //System.out.println(newWeights.length+" "+winner.getWeights().length+" "+otherNode.getWeights().length);
		newWeights = ArrayHelper.substract(newWeights, otherNode.getWeights(), AlgoParameters.DIMENSIONS);
		return newWeights;
	}
	
        //winner,new node
	private double[] newWeightsForNewNodeOneOlderNeighbor(LNode winner){
		double[] newWeights = new double[AlgoParameters.DIMENSIONS];
		double min = ArrayHelper.getMin(winner.getWeights());
		double max = ArrayHelper.getMax(winner.getWeights());
		for(int i=0;i<AlgoParameters.DIMENSIONS;i++){
			newWeights[i]=(min+max)/2;
		}
		return newWeights;
	}
}
