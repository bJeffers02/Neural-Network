import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgo extends Thread{
    
    Random rand = new Random();
    int randomIndex;
    
    int populationSize;
    int generations;
    int testSize;

    double[][] inputs;
    int[] labels;
    double[][] testInputs;
    int[] testLabels;

    ArrayList<Network> currentGen;
    ArrayList<Network> nextGen;

    public GeneticAlgo(double[][] inputs, int[] labels, int populationSize, int generations, int testSize){
        this.inputs = inputs;
        this.labels = labels;
        this.populationSize = populationSize;
        this.generations = generations;
        this.testSize = testSize;
        testInputs = new double[testSize][10];
        testLabels = new int[testSize];
    }

    @Override
    public void run() {
        currentGen = initializePopulation(populationSize);
        nextGen = new ArrayList<>();

        for(int i = 0; i < testSize; i++){
            testInputs[i] = inputs[i];
            testLabels[i] = labels[i];
        }
        
        currentGen = sort(currentGen, testInputs, testLabels);
        
        for(int i = 0; i < generations; ++i){ 
            
            for(int j = 0; j < currentGen.size(); ++j){ 
                nextGen.add(new Network(currentGen.get(j)));
            }
            
            for(int j = 0; j < (populationSize*2); ++j){
                nextGen.add((nextGen.get(rand.nextInt(nextGen.size()))).crossover(nextGen.get(rand.nextInt(nextGen.size()))));
            }

            for(int j = 1; j < nextGen.size(); ++j){ 
                nextGen.get(j).mutate();
            }

            for(int j = 0; j < testSize; j++){
                randomIndex = rand.nextInt(inputs.length);
                testInputs[j] = inputs[randomIndex];
                testLabels[j] = labels[randomIndex]; 
            }
            
            
            nextGen = sort(nextGen, testInputs, testLabels);

            currentGen.clear(); 

            for(int j = 0; j < populationSize; ++j){ 
                currentGen.add(new Network(nextGen.get(j)));
            }

            nextGen.clear();

            //System.out.println((i+1) + " out of " + generations + ": " + currentGen.get(0).getCost(inputs[59999], labels[59999]) );
        }

        printResults(currentGen.get(0));

        try {
            writeObjectToFile(currentGen.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Network> initializePopulation(int populationSize){
        ArrayList<Network> population = new ArrayList<>();
        for(int i = 0; i < populationSize; ++i){
            population.add(new Network(new int[]{784, 16, 16, 10}));
        }
        return population;
    }

    public ArrayList<Network> sort(ArrayList<Network> sortable, double[][] testInputs, int[] testLabels){
        int n = sortable.size();
        Network temp;
        double dTemp;

        double[] costs = new double[n];
        for(int i = 0; i < n; i++){
            costs[i] = getCost(sortable.get(i), testInputs, testLabels);
        }
        
        for(int i = 0; i < n; i++){
            for(int j = 1; j < (n-i); j++){
                if(costs[j-1] > costs[j]){
                    temp = sortable.get(j-1);
                    sortable.set(j-1, sortable.get(j));
                    sortable.set(j, temp);

                    dTemp = costs[j-1];
                    costs[j-1] = costs[j];
                    costs[j] = dTemp;
                }
            }
        }

        // for (double d : costs) {
        //     System.out.println(d);
        // }
        return sortable;
    }

    public double getCost(Network nwrk, double[][] testInputs, int[] testLabels){
        double cost = 0;
        for(int i = 0; i < testInputs.length; i++){
            cost += nwrk.getCost(testInputs[i], testLabels[i]);
        }
        return cost / testInputs.length;
    }

    public void printResults(Network best){
        int indexOfGreatest = 0;
        double greatest = 0;

        randomIndex = rand.nextInt(inputs.length);
        System.out.println("Cost: " + best.getCost(inputs[randomIndex], labels[randomIndex]));
        double[] output = best.calculateOutputs(inputs[randomIndex]);
        
        for(int i = 0; i < 10; i++){
            if(output[i] > greatest){
                indexOfGreatest = i;
                greatest = output[i];
            }
        }
        System.out.println("\nPredictions: ");
        int i = 0;
        for(double otp : output){
            System.out.print(i + ": " + Math.round(otp*100) + "% -> ");
            if(i==indexOfGreatest){
                System.out.println("True");
            }
            else{
                System.out.println("False");
            }
            i++;
        }
        System.out.println("\nActual: " + labels[randomIndex]);
    }

    public static void writeObjectToFile(Object serObj) throws IOException {
        final String filepath = "resources/network";
        
        FileOutputStream fileOut = new FileOutputStream(filepath, false);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(serObj);
        objectOut.close();
        System.out.println("The Object  was succesfully written to a file");
    }
}
