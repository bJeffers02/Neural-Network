import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;

public class Test {

    public static double[] getImageGrayscale() throws IOException{
        BufferedImage image = ImageIO.read(new File("resources/5.1.png"));
        int width = image.getWidth();
        int height = image.getHeight();
        double[] grayscaleValues = new double[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (int) ((r + g + b) / 3.0);
                grayscaleValues[y * width + x] = avg / 255.f;
            }
        }
        return grayscaleValues;
    }

    public static Object ReadObjectFromFile(String filepath) throws ClassNotFoundException, IOException {
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Object obj = objectIn.readObject();
        objectIn.close();
        return obj;
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Network network = (Network) ReadObjectFromFile("resources/network");

        double[] values = getImageGrayscale();

        double[] output = network.calculateOutputs(values);
        
        int indexOfGreatest = 0;
        double greatest = 0;

        int c = 0;
        for(double op : output){
            System.out.println(c + ": " + Math.round(op*100) + "%");
            c++;
        }

        for(int i = 0; i < 10; i++){
            if(output[i] > greatest){
                indexOfGreatest = i;
                greatest = output[i];
            }
        }

        // String black = "\u001B[40m";
        // String white = "\u001B[47m";
        // String reset = "\u001B[0m";

        // for(int j = 0; j < 28; j++){
        //     for(int k = 0; k < 28; k++){
        //         if(values[k + j*28] == 0){
        //             System.out.print(black + "  ");
        //         }
        //         else{
        //             System.out.print(white + "  ");
        //         }
                
        //     }
        //     System.out.println(reset);
        // }

        System.out.println("\nClassify: " + indexOfGreatest);
    }
}