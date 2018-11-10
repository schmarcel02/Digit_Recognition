package network;

public class NeuralNetwork {

    public final int[] NETWORK_LAYER_SIZES;
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;
    public final int NETWORK_SIZE;
    
    private double[][] output;
    private double[][][] weights;
    private double[][] bias;

    private double[][] error_signals;
    private double[][] output_derivative;

    public NeuralNetwork(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = this.NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = this.NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = this.NETWORK_LAYER_SIZES[NETWORK_SIZE-1];

        this.output = new double[NETWORK_SIZE][];
        this.error_signals = new double[NETWORK_SIZE][];
        this.output_derivative = new double[NETWORK_SIZE][];
        this.weights = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];

        for (int i = 0; i < NETWORK_SIZE; i++) {
            this.output[i] = new double[this.NETWORK_LAYER_SIZES[i]];
            this.error_signals[i] = new double[this.NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[this.NETWORK_LAYER_SIZES[i]];

            this.bias[i] = NetworkTools.createRandomArray(this.NETWORK_LAYER_SIZES[i], 0.3,0.7);

            if (i > 0)
                weights[i] = NetworkTools.createRandomArray(this.NETWORK_LAYER_SIZES[i], this.NETWORK_LAYER_SIZES[i-1], -0.3,0.5);
        }
    }

    public double[] feedForward(double... input) {
        if (input.length != this.INPUT_SIZE)
            return null;

        this.output[0] = input;
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = bias[layer][neuron];
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++)
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];

                output[layer][neuron] = sigmoid(sum);
                output_derivative[layer][neuron] = (output[layer][neuron] * (1 - output[layer][neuron]));
            }
        }

        return output[NETWORK_SIZE-1];
    }

    public void train(double[] input, double[] target, double eta) {
        if (input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) return;

        feedForward(input);
        backPropagationError(target);
        updateWeights(eta);
    }

    public void train(TrainSet trainSet, int loops, int batch_size){
        if (trainSet.INPUT_SIZE != INPUT_SIZE || trainSet.OUTPUT_SIZE != OUTPUT_SIZE) return;
        for (int i = 0; i < loops; i++) {
            TrainSet batch = trainSet.extractBatch(batch_size);
            for (int j = 0; j < batch_size; j++) {
                this.train(batch.getInput(j), batch.getOutput(j), 0.3);
            }
        }
    }

    public void train(TrainSet trainSet, int loops){
        if (trainSet.INPUT_SIZE != INPUT_SIZE || trainSet.OUTPUT_SIZE != OUTPUT_SIZE) return;
        for (int i = 0; i < loops; i++) {
            for (int j = 0; j < trainSet.size(); j++) {
                this.train(trainSet.getInput(j), trainSet.getOutput(j), 0.3);
            }
        }
    }

    public void train(TrainSet trainSet){
        if (trainSet.INPUT_SIZE != INPUT_SIZE || trainSet.OUTPUT_SIZE != OUTPUT_SIZE) return;
        for (int i = 0; i < trainSet.size(); i++) {
            this.train(trainSet.getInput(i), trainSet.getOutput(i), 0.3);
        }
    }

    public void backPropagationError(double[] target){
        for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE - 1]; neuron++)
            error_signals[NETWORK_SIZE-1][neuron] = (output[NETWORK_SIZE-1][neuron] - target[neuron]) * output_derivative[NETWORK_SIZE-1][neuron];

        for (int layer = NETWORK_SIZE - 2; layer > 0; layer--) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;
                for (int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer+1]; nextNeuron++)
                    sum += weights[layer+1][nextNeuron][neuron] * error_signals[layer+1][nextNeuron];

                this.error_signals[layer][neuron] = sum * output_derivative[layer][neuron];
            }
        }
    }

    public void updateWeights(double eta){
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    weights[layer][neuron][prevNeuron] += -eta * output[layer-1][prevNeuron] * error_signals[layer][neuron];
                }

                bias[layer][neuron] += -eta * error_signals[layer][neuron];
            }
        }
    }

    private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

}
