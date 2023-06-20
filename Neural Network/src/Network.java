import java.io.Serializable;
import java.util.Random;

public class Network implements Serializable{
    
    Random rand = new Random();

    Layer[] layers;

    public Network(Network other){
        this.layers = new Layer[other.layers.length];
        for(int i = 0; i < layers.length; i++){
            this.layers[i] = new Layer(other.layers[i]);
        }
    }

    public Network(int[] layerSizes){
        layers = new Layer[layerSizes.length-1];
        for(int i = 0; i < layers.length; i++){
            layers[i] = new HiddenLayer(layerSizes[i], layerSizes[i+1]);
        }
    }

    public double[] calculateOutputs(double[] inputs){
        for(Layer lr : layers){
            inputs = lr.calculateOutputs(inputs);
        }
        return inputs;
    }

    public int classify(double[] inputs){
        double[] outputs = calculateOutputs(inputs);
        int maxAt = 0;

        for (int i = 0; i < outputs.length; i++) {
            maxAt = outputs[i] > outputs[maxAt] ? i : maxAt;
        }
        
        return maxAt;
    }

    public double getCost(double[] inputs, int expectedOutput){
        double[] outputs = calculateOutputs(inputs);
        double[] expectedOutputs = new double[outputs.length];
        double cost = 0;

        for(int i = 0; i < expectedOutputs.length; i++){
            expectedOutputs[i] = 0.0;
        }
        expectedOutputs[expectedOutput] = 1.0;

        for(int i = 0 ; i < outputs.length; i++){
            cost += layers[layers.length-1].nodeCost(outputs[i], expectedOutputs[i]);
        }

        return cost;
    }

    public void mutate(){
        if(rand.nextInt(10) == 0){
            for(Layer lay : layers){
                lay.mutate();
            }
        }
    }

    public Network crossover(Network other){
        Network child = new Network(this);
        for(int i = 0; i < layers.length; i++){
            child.layers[i] = layers[i].crossover(other.layers[i]);
        }
        return child;
    }
}
