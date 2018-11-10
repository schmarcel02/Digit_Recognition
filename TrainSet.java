package network;

import java.util.ArrayList;
import java.util.Arrays;

public class TrainSet {
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;

    //double[][] <- index1: 0 = input, 1 = output || index2: index of element
    private ArrayList<double[][]> data = new ArrayList<>();

    public TrainSet(int INPUT_SIZE, int OUTPUT_SIZE) {
        this.INPUT_SIZE = INPUT_SIZE;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    public void addData(double[] input, double[] target) {
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) return;
        data.add(new double[][]{input, target});
    }

    public TrainSet extractBatch(int size) {
        if(size > 0 && size <= this.size()) {
            TrainSet set = new TrainSet(INPUT_SIZE, OUTPUT_SIZE);
            Integer[] ids = NetworkTools.randomValues(0,this.size() - 1, size);
            assert ids != null;
            for(Integer i:ids) {
                set.addData(this.getInput(i),this.getOutput(i));
            }
            return set;
        }else return this;
    }

    public void createSets(int loops){
        for(int i = 0; i < loops; i++) {
            double[] input = new double[INPUT_SIZE];
            double[] output = new double[OUTPUT_SIZE];
            for(int k = 0; k < INPUT_SIZE; k++) {
                input[k] = (double)((int)(Math.random() * 10)) / (double)10;
                if(k < OUTPUT_SIZE) {
                    output[k] = (double)((int)(Math.random() * 10)) / (double)10;
                }
            }
            this.addData(input,output);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder("TrainSet [" + INPUT_SIZE + " ; " + OUTPUT_SIZE + "]\n");
        int index = 0;
        for(double[][] r:data) {
            s.append(index).append(":   ").append(Arrays.toString(r[0])).append("  >-||-<  ").append(Arrays.toString(r[1])).append("\n");
            index++;
        }
        return s.toString();
    }

    public int size() {
        return data.size();
    }

    public double[] getInput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    public double[] getOutput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }

    public int getINPUT_SIZE() {
        return INPUT_SIZE;
    }
}
