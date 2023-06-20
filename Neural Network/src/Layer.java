import java.io.Serializable;
import java.util.Random;

public class Layer implements Serializable{
    Random rand = new Random();
    
    double[][] weights;
    double[] biases;
    int numIn, numOut;

    public Layer(){
    }
    

    public Layer(Layer other){
        this.numIn = other.numIn;
        this.numOut = other.numOut;

        weights = other.weights.clone();
        biases = other.biases.clone();
    }

    public Layer(int numIn, int numOut){
        this.numIn = numIn;
        this.numOut = numOut;

        weights = new double[numIn][numOut];
        biases = new double[numOut];
    }

    public double[] calculateOutputs(double[] inputs){
        double[] outputs = new double[numOut];

        for(int i = 0; i < numOut; i++){
            double output = biases[i];
            for(int j = 0; j < numIn; j++){
                output += inputs[j] * weights[j][i];
            }
            outputs[i] = activationFunction(output);
        }

        return outputs;
    }

    public double activationFunction(double in){
        return 1/(1 + Math.exp(-in));
        //return (in > 0) ? 1 : 0;
    }

    double nodeCost(double actualOutput, double expectedOutput){
        return Math.pow(actualOutput - expectedOutput, 2);
    }

    public void setBiases(double[] biases) {
        this.biases = biases;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }

    public void mutate(){
        for(int i = 0; i < weights[0].length; i++){
            for(int j = 0; j < weights.length; j++){
                if(rand.nextInt(10) == 0){
                    weights[j][i] += rand.nextDouble() * 0.2 - 0.1;
                }
                
            }

            if(rand.nextInt(10) == 0){
                biases[i] += rand.nextDouble() * 0.2 - 0.1;
            }
        }
    }

    public Layer crossover(Layer other){
        double[][] childWeights = new double[numIn][numOut];
        double[] childBiases = new double[numOut];
        Layer child = new Layer();
        child.numIn = numIn;
        child.numOut = numOut;

        for(int i = 0; i < numIn; i++){
            for(int j = 0; j < numOut; j++){
                if(rand.nextInt(10)>4){
                    childWeights[i][j] = weights[i][j];
                }
                else{
                    
                    childWeights[i][j] = other.weights[i][j];
                }
            }
        }
        
        for (int i = 0; i < numOut; i++) {
            if(rand.nextInt(10)>4){
                childBiases[i] = biases[i];
            }
            else{
                childBiases[i] = other.biases[i];
            }
        }

        child.setBiases(childBiases);
        child.setWeights(childWeights);

        return child;
    }
}
