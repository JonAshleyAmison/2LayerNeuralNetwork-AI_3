
import java.util.StringTokenizer;

public class Amison3 {

    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LList learnMe = new LList();
    public static double learningRate = 0.008;
    public static double[][] weights;

    public static void main(String[] args) {
        while (true) {
            int options = input.getInteger(true, 1, 1, 3, "1=Train\n2=Recall\n3=Exit");
            if (options == 1) {
                learnMe.clear();
                weights = new double[121][121];
                TextFileClass textFile = new TextFileClass();
                textFile.getFileName("Specify the text file containing the list of I/O file names:");
                textFile.getFileContents();
                int numberOfRelations = Integer.parseInt(textFile.text[0]);
                for (int i = 1; i <= numberOfRelations * 2; i++) {
                    TextFileClass newTextFileIn = new TextFileClass();
                    TextFileClass newTextFileOut = new TextFileClass();
                    newTextFileIn.getFileNamePassed(textFile.text[i]);
                    newTextFileOut.getFileNamePassed(textFile.text[i + 1]);
                    newTextFileIn.getFileContents();
                    newTextFileOut.getFileContents();
                    double[] newIn = parser(newTextFileIn.text[0]);
                    double[] newOut = parser(newTextFileOut.text[0]);
                    Object newObject = new inOut(textFile.text[i],newIn, newOut, "");
                    i++;
                    learnMe.add(newObject);
                }

                for (int t = 0; t < 121; t++) {
                    for (int i = 0; i < 121; i++) {
                        double random = Math.random() - .5;
                        weights[t][i] = random;
                    }
                }
                outUpdate();
                int trainingIters = input.getInteger(true, 10, 1, 999999, "How many training iterations?");
                learningRate = input.getDouble(true, learningRate, 0.000000000000000000001, Double.MAX_VALUE, "Learning rate?");
                for (int i = 0; i < trainingIters; i++) {
                    trainer();
                    System.out.println("---------------------------------------");
                    System.out.println("");
                }
            }
            if (options == 2) {
                String recallMe = input.getString("", "Specify the text file containing the input pattern data:");
                while(recallMe.equals("")){
                    System.out.println("Enter a File Name");
                    recallMe = input.getString("", "Specify the text file containing the input pattern data:");
                }
                recall(recallMe);
            }
            if (options == 3) {
                break;
            }
        }

    }

    public static double[] parser(String passed) {
        double[] returner = new double[121];
        StringTokenizer parser = new StringTokenizer(passed);
        for (int i = 0; i < returner.length; i++) {
            double parsedInt = Double.parseDouble(parser.nextToken(" "));
            returner[i] = parsedInt;
        }
        return returner;
    }

    public static String convert(double[] passed) {
        String returner = "";
        char X = 'X';
        char space = '\u2588';
        for (int i = 0; i < passed.length; i++) {
            double here = passed[i];
            if (here < 0) {
                returner += 'X';
            } else {
                returner += '\u2588';
            }
        }
        return returner;
    }

    public static void print(double[] inPassed, double[] desiredPassed, String out) {
        String in = convert(inPassed);
        String desired = convert(desiredPassed);
        System.out.println(in.substring(0, 11) + "   " + desired.substring(0, 11) + "   " + out.substring(0, 11));
        System.out.println(in.substring(11, 22) + "   " + desired.substring(11, 22) + "   " + out.substring(11, 22));
        System.out.println(in.substring(22, 33) + "   " + desired.substring(22, 33) + "   " + out.substring(22, 33));
        System.out.println(in.substring(33, 44) + "   " + desired.substring(33, 44) + "   " + out.substring(33, 44));
        System.out.println(in.substring(44, 55) + "   " + desired.substring(44, 55) + "   " + out.substring(44, 55));
        System.out.println(in.substring(55, 66) + "   " + desired.substring(55, 66) + "   " + out.substring(55, 66));
        System.out.println(in.substring(66, 77) + "   " + desired.substring(66, 77) + "   " + out.substring(66, 77));
        System.out.println(in.substring(77, 88) + "   " + desired.substring(77, 88) + "   " + out.substring(77, 88));
        System.out.println(in.substring(88, 99) + "   " + desired.substring(88, 99) + "   " + out.substring(88, 99));
        System.out.println(in.substring(99, 110) + "   " + desired.substring(99, 110) + "   " + out.substring(99, 110));
        System.out.println(in.substring(110) + "   " + desired.substring(110) + "   " + out.substring(110));
    }

    public static void trainer() {
        for (int i = 1; i <= learnMe.getLength(); i++) {
            inOut current = (inOut) learnMe.getEntry(i);
            for (int j = 0; j < 121; j++) {
                double[] weight = weights[j];
                double desired = current.desired[j];
                double activation = activation(current.in, weight);
                double delta = desired - activation;
                for (int k = 0; k < weight.length; k++) {
                    weight[k] = tiny(weight[k] + (learningRate * delta * current.in[k]));
                }
                weights[j] = weight;
            }
        }
        outUpdate();
        for (int i = 1; i <= learnMe.getLength(); i++) {
            inOut current = (inOut) learnMe.getEntry(i);
            print(current.in,current.desired,current.out);
            System.out.println("");
        }        
    }

    public static double tiny(double passed) {
        if (passed < 0.000000000001 && passed > -0.000000000001) {
            return 0.0;
        }
        return passed;
    }

    public static void outUpdate() {
        for (int i = 1; i <= learnMe.getLength(); i++) {
            inOut current = (inOut) learnMe.getEntry(i);
            current.out = "";
            for (int j = 0; j < 121; j++) {
                double activate = activation(current.in, weights[j]);
                if (activate > 0) {
                    current.out += '\u2588';
                } else {
                    current.out += 'X';
                }
            }            
        }
    }

    public static double activation(double[] ins, double[] weight) {
        double activation = 0.0;
        for (int i = 0; i < 121; i++) {
            activation += (ins[i] * weight[i]);
        }
        return activation;
    }

    public static void recall(String name) {
        while(true){
            String list = "";       
        for (int i = 1; i <= learnMe.getLength(); i++) {
           inOut findMe = (inOut) learnMe.getEntry(i);
           if(name.equals(findMe.name)){
               print(findMe.in, findMe.desired, findMe.out);
               return;
           }
           list+= findMe.name+" ";
        }
            System.out.println("Could Not Find File, Re-Enter The Name of The File To Recall (Case Sensative)");
            name = input.getString(name, "Files Currently in Memory: ("+list+")");
        } 
    }
}

class inOut {

    public String name;
    public double[] in;
    public double[] desired;
    public String out;

    public inOut(String passedName,double[] passedIn, double[] passedDesired, String outPass) {
        name=passedName;
        in = passedIn;
        desired = passedDesired;
        out = outPass;
    }
}
