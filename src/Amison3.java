
import java.util.StringTokenizer;

public class Amison3 {

    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LList learnMe = new LList();
    public static double learningRate = 0.01;
    public static double[][] weights;

    public static void main(String[] args) {
//        while (true) {
        TextFileClass textFile = new TextFileClass();
        weights = new double[121][121];
        char X = 'X';
        char space = '\u2588';
        textFile.getFileName("Enter Valid File Name");
        textFile.getFileContents();
        int numberOfRelations = Integer.parseInt(textFile.text[0]);
        for (int i = 1; i <= numberOfRelations * 2; i++) {
            TextFileClass newTextFileIn = new TextFileClass();
            TextFileClass newTextFileOut = new TextFileClass();
            newTextFileIn.getFileNamePassed(textFile.text[i]);
            newTextFileOut.getFileNamePassed(textFile.text[i + 1]);
            newTextFileIn.getFileContents();
            newTextFileOut.getFileContents();
            int[] newIn = parser(newTextFileIn.text[0]);
            int[] newOut = parser(newTextFileOut.text[0]);
            Object newObject = new inOut(newIn, newOut);
            i++;
            learnMe.add(newObject);
        }
        for (int t = 0; t < 121; t++) {
            for (int i = 0; i < 121; i++) {
                double random = Math.random() - .5;
                weights[t][i] = random;
            }
        }
        trainer();
//            char quitMe = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Exit The Program? (Y/N Default=N)");
//            if (quitMe == 'Y') {                                        // exit the program
//                break;
//            } else {
//                learnMe.clear();
//            }
//        }
    }

    public static int[] parser(String passed) {
        int[] returner = new int[121];
        StringTokenizer parser = new StringTokenizer(passed);
        for (int i = 0; i < returner.length; i++) {
            int parsedInt = Integer.parseInt(parser.nextToken(" "));
            returner[i] = parsedInt;
        }
        return returner;
    }

    public static void printer() {

    }

    public static void trainer() {
        for (int i = 1; i <= learnMe.getLength(); i++) {
            inOut x = (inOut) learnMe.getEntry(i);
            int count = 0;
            for (int j = 0; j < 121; j++) {
                double[] weight = weights[j];
                int desired = x.desired[j];
                double activation=activation(x.in, weight);
                double delta = desired-activation;
                for (int k = 0; k < weight.length; k++) {
                    weight[k] = weight[k]+(learningRate*delta*x.in[k]);                   
                }
                weights[j]=weight;
            }
        }
    }

    public static double activation(int[] ins, double[] weight) {
        double activation=0.0;
        for (int i = 0; i < 121; i++) {
            activation += (ins[i] * weight[i]);
        }
        return activation;
    }
}

class inOut {

    public int[] in;
    public int[] desired;

    public inOut(int[] passedIn, int[] passedDesired) {
        in = passedIn;
        desired = passedDesired;
    }
}
