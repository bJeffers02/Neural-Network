public class HiddenLayer extends Layer{

    public HiddenLayer(int numIn, int numOut) {
        super(numIn, numOut);
        initRandBiases();
        initRandWeights();
    }
    
    public void initRandWeights(){
        for(int i = 0; i < numIn; i++){
            for(int j = 0; j < numOut; j++){
                weights[i][j] = (rand.nextDouble() * 2 - 1);
            }
        }
    }

    public void initRandBiases(){
        for(int i = 0; i < numOut; i++){
            biases[i] = (rand.nextDouble() * 2 - 1);
        }
    }
}
