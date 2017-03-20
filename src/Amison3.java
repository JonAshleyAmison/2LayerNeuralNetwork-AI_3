
import java.util.StringTokenizer;
//test

public class Amison3 {

    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LList learnMe = new LList();
    public double learningRate = 0.01;

    public static void main(String[] args) {
        while (true) {
            TextFileClass textFile = new TextFileClass();
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
                double[] weights = new double[121];
                for (int t = 0; t < 121; t++) {
                    double random = Math.random() - .5;
                    System.out.println(random);
                    weights[t] = random;
                }
                Object newObject = new inOut(newTextFileIn.text[0], newTextFileOut.text[0], weights);
                i++;
                learnMe.add(newObject);
            }
            char quitMe = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Exit The Program? (Y/N Default=N)");
            if (quitMe == 'Y') {                                        // exit the program
                break;
            } else {
                learnMe.clear();
            }
        }
    }

    public static void printer() {

    }

    public static void trainer() {

    }

    public static double delta(int target, int input, double weight) {
        double delta = target - (/*Ei??**/input * weight);

        return delta;
    }
}

class inOut {

    public String in;
    public String desired;
    public double[] weights;

    public inOut(String passedIn, String passedDesired, double[] passedWeights) {
        in = passedIn;
        desired = passedDesired;
        weights = passedWeights;
    }
}
