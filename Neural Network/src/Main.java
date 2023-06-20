import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Main {

    public final static int numberOfProb = 60000;

    public static void writeObjectToFile(Object serObj) throws IOException {
        final String filepath = "resources/network.txt";
        
        FileOutputStream fileOut = new FileOutputStream(filepath, false);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(serObj);
        objectOut.close();
        System.out.println("The Object  was succesfully written to a file");
    }
    
    public static int[] getLabels(){
        final String trainLabelsURL = "http://yann.lecun.com/exdb/mnist/train-labels-idx1-ubyte.gz";
        int[] labels = new int[numberOfProb];
        byte[] dataBuffer = new byte[1];

        try {
            URL url = new URL(trainLabelsURL);
            InputStream stream = url.openConnection().getInputStream();
            InputStream lblIn = new GZIPInputStream(stream);

            byte[] tempBuffer = new byte[8];
            lblIn.read(tempBuffer, 0, 8);

            for (int i = 0; i < numberOfProb; i++){
                lblIn.read(dataBuffer, 0, 1);
                labels[i] = dataBuffer[0] & 0xFF;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return labels;
    }

    public static double[][] getInputs(){
        final String trainImagesURL = "http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz";
        byte[] dataBuffer = new byte[1];
        double[][] images = new double[numberOfProb][784];
        
        try {
            URL url = new URL(trainImagesURL);
            InputStream stream = url.openConnection().getInputStream();
            InputStream imgIn = new GZIPInputStream(stream);

            byte[] tempBuffer = new byte[16];
            imgIn.read(tempBuffer, 0, 16);

            for (int i = 0; i < numberOfProb; i++){
                for (int j = 0; j < 784; j++){
                imgIn.read(dataBuffer, 0, 1);
                float pixelVal = (dataBuffer[0] & 0xFF) / 255.f;
                images[i][j] = pixelVal;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return images;
    }

    public static void main(String[] args) throws IOException {
        double[][] inputs = getInputs();
        int[] labels = getLabels();

        // String black = "\u001B[40m";
        // String white = "\u001B[47m";
        // String reset = "\u001B[0m";
        // for(int i = 0; i < 100; i++){
        //     for(int j = 0; j < 28; j++){
        //         for(int k = 0; k < 28; k++){
        //             if(inputs[i][k + j*28] == 0){
        //                 System.out.print(white + "  ");
        //             }
        //             else{
        //                 System.out.print(black + "  ");
        //             }
                    
        //         }
        //         System.out.println(reset);
        //     }
        //     System.out.println(labels[i]);
        // }
        
        GeneticAlgo algo1 = new GeneticAlgo(inputs, labels, 300, 3500, 10);
        algo1.run();
        
        // Network network = new Network(new int[]{10,10,10});
        // Network clone = new Network(network);

        // network.mutate();
        

        // System.out.println(clone.getCost(inputs[1], labels[1]));
        // System.out.println(network.getCost(inputs[1], labels[1]));
    }
    
}
