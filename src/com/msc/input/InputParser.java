package com.msc.input;


import com.msc.utils.AlgoParameters;
import com.msc.utils.ArrayHelper;
import java.awt.Color;
import java.util.ArrayList;

public abstract class InputParser {

    ArrayList<String> strForWeights;
    ArrayList<double[]> weights;

    public InputParser() {

        strForWeights = new ArrayList<String>();
        weights = new ArrayList<double[]>();
    }

    abstract public void parseInput(String fileName);

    protected void normalizeData(ArrayList<double[]> inputs) {     
            normalizeVertical(inputs);
       
    }

    private void normalizeVertical(ArrayList<double[]> inputs) {
        
        int dimensions = AlgoParameters.DIMENSIONS;
        
        ArrayList<Double> maxDimArr = new ArrayList<Double>();
        ArrayList<Double> minDimArr = new ArrayList<Double>();

        for (int i = 0; i < dimensions; i++) {
            double[] dimArr = new double[inputs.size()];
            for (int j = 0; j < inputs.size(); j++) {
                dimArr[j] = inputs.get(j)[i];
            }
            maxDimArr.add(ArrayHelper.getMax(dimArr));
            minDimArr.add(ArrayHelper.getMin(dimArr));
        }

        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < inputs.size(); j++) {
                double[] inArr = inputs.get(j);
                
                //do this if there's some value other than 0 is in column
                if(maxDimArr.get(i) - minDimArr.get(i) > 0){
                inArr[i] = (inArr[i] - minDimArr.get(i)) / (maxDimArr.get(i) - minDimArr.get(i));
                inputs.set(j, inArr);
                }
            }
        }
    }

    protected boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    protected Color getColor(String color) {
        Color clr = null;

        if (color.equalsIgnoreCase("red")) {
            clr = Color.red;
        } else if (color.equalsIgnoreCase("green")) {
            clr = Color.green;
        } else if (color.equalsIgnoreCase("blue")) {
            clr = Color.blue;
        } else if (color.equalsIgnoreCase("black")) {
            clr = Color.black;
        } else if (color.equalsIgnoreCase("white")) {
            clr = Color.white;
        } else if (color.equalsIgnoreCase("orange")) {
            clr = Color.orange;
        } else if (color.equalsIgnoreCase("gold")) {
            clr = new Color(255, 215, 0);
        } else if (color.equalsIgnoreCase("brown")) {
            clr = new Color(165, 42, 42);
        }

        return clr;
    }

    public ArrayList<String> getStrForWeights() {
        return strForWeights;
    }

    public ArrayList<double[]> getWeights() {
        return weights;
    }

}
