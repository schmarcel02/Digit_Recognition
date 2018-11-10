package common;

import ch.seifert.nn.NeuralNetwork;

import java.util.Arrays;

public class NN {
    public static double eta = 0.1;
    private static NeuralNetwork neuralNetwork;

    static {
        neuralNetwork = new NeuralNetwork(784, 100, 10);
    }

    public static int feedForward(byte[] imageData) {
        double[] inputs = imageDataToDoubleArray(imageData);
        double[] output = neuralNetwork.feedForward(inputs);
        //System.out.println(Arrays.toString(output));
        return getIndexOfLargest(output);
    }

    public static void train(byte[] imageData, int expected) {
        double[] e = new double[10];
        e[expected] = 1;
        //System.out.println(Arrays.toString(e));
        double[] inputs = imageDataToDoubleArray(imageData);
        neuralNetwork.train(inputs, e, eta);
    }

    private static int getIndexOfLargest( double[] array )
    {
        if ( array == null || array.length == 0 ) return -1; // null or empty

        int largest = 0;
        for ( int i = 1; i < array.length; i++ )
        {
            if ( array[i] > array[largest] ) largest = i;
        }
        return largest; // position of the first largest found
    }

    private static double[] imageDataToDoubleArray(byte[] imageData) {
        double[] inputs = new double[784];
        for (int i = 0; i < 784; i++) {
            inputs[i] = (imageData[i] & 0xFF)/255f;
        }
        return inputs;
    }
}
