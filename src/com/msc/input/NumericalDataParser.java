/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msc.input;


import com.msc.listeners.TaskListener;
import com.msc.utils.AlgoParameters;
import com.msc.utils.Constants;
import com.msc.utils.LogMessages;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Thush
 */
public class NumericalDataParser extends InputParser{
    
    private TaskListener tListener;
    
    public NumericalDataParser(TaskListener tListener){
        this.tListener = tListener;
    }
    
    public void parseInput(String fileName){

		String tokenizer= Constants.INPUT_TOKENIZER;
                int tempDim = 0;
                boolean dimensionMismatch = false;
                boolean notNormalized = false;
                
		try {
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			File iFile = new File(fileName);
			BufferedReader input =  new BufferedReader(new FileReader(iFile));
			try {
				String line = null; //not declared within while loop

				while (( line = input.readLine()) != null){
					String text = line;
					if(text!=null && text.length()>0){
						String[] tokens = text.split(tokenizer);
                                                
                                                
						strForWeights.add(tokens[0]);
						double[] weightArr = new double[tokens.length-1];
						for(int j=1;j<tokens.length;j++){
							weightArr[j-1]=Double.parseDouble(tokens[j]);
                                                        if(weightArr[j-1]>1 || weightArr[j-1]<0){
                                                            notNormalized = true;
                                                            break;
                                                        }
						}
						weights.add(weightArr);
                                                
                                                if(tempDim == 0)
                                                    tempDim = weightArr.length;
                                                
                                                if(weightArr.length != tempDim)
                                                    dimensionMismatch = true;
					}					
				}
			}catch(IOException ex){
                            tListener.logMessage(LogMessages.IO_ERROR);
                        }

                        input.close();
			
                        if(dimensionMismatch)
                            tListener.updateStatus(LogMessages.DIM_MISMATCH);
                        
                        if(notNormalized)
                            tListener.updateStatus(LogMessages.NOT_NORMALIZED);
                        
                        AlgoParameters.DIMENSIONS = weights.get(0).length;
                        
		}
		catch (IOException ex){
			tListener.logMessage(LogMessages.IO_ERROR);
		}
	}
}
