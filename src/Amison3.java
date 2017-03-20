
import java.util.StringTokenizer;
//test
public class Amison3 {

    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LList learnMe = new LList();
    public static double[] weights = new double[121];
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
                Object newObject = new inOut(newTextFileIn.text[0], newTextFileOut.text[0]);
                i++;
                learnMe.add(newObject);
            }
            for (int i = 0; i < 121; i++) {
                double random = Math.random()-.5;
                System.out.println(random);
                weights[i] = random;
            }
            char quitMe = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Exit The Program? (Y/N Default=N)");
            if (quitMe == 'Y') {                                        // exit the program
                break;
            }
            else{
                weights = new double[121];
                learnMe.clear();
            }
        }
    }

    public static void printer() {

    }

    public static void trainer() {

    }
}

class inOut {

    public String in;
    public String desired;

    public inOut(String passedIn, String passedDesired) {
        in = passedIn;
        desired = passedDesired;
    }
}
