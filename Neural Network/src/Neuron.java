// import java.io.Serializable;
// import java.util.Random;

// public class Neuron implements Serializable{
//     Random rand = new Random();
//     double bias;
//     double[] weights;

//     public Neuron(int numWeights){
//         weights = new double[numWeights];
//         for(int i = 0; i < numWeights; i++){
//             weights[i] = (rand.nextDouble() * 50) - 25;
//         }
//         bias = (rand.nextDouble() * 50) - 25;
//     }

//     public Neuron(Neuron other) {
//         this.bias = other.bias;
//         this.weights = other.weights;
//     }

//     public double sigmoid(double in){
//         return 1 / (1 + Math.exp(-in));
//     }

//     public double compute(double[] lastValues){
//         double preSig = 0;
//         int i = 0;

//         for(double lv : lastValues){
//             preSig += (lv * weights[i]);
//             i++;
//         }

//         preSig += bias;

//         return sigmoid(preSig);
//     }

//     public void mutate(){
//         if(rand.nextInt(5)==1){
//             bias = (rand.nextDouble() * 2) - 1;
//         }

//         for(int i = 0; i < weights.length; i++){
//             if(rand.nextInt(10)==1){
//                 weights[i] = (rand.nextDouble() * 50) - 25;
//             }
//         }
//     }
// }
