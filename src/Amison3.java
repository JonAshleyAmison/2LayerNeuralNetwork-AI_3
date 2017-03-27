
import java.util.StringTokenizer;
//Program:	Amison3.java
//Course:	COSC470
//Description:	2-Layer Neural Network used for binary pattern recognition
//Author:	Reagan JonAshley Amison 
//Revised:	3/27/2017
//Language:	Java
//IDE:		NetBeans 8.1
//Notes:	added a method to TextFileClass called getFileNamePassed(see TextFileClass.java)
//*************************************************************************************************************************
//*************************************************************************************************************************
// class:       Amison3
// Description: refer to the program description
// Data Values: none
// Constant:    none 
// Globals:     input- access to keyboard input class
//              learnMe - linked list used to hold the inOut objects used by the nueral net
//              learningRate - learning Rate used in the training algorithm
//              weights - 2D double array to hold all the weights of the arcs
//*************************************************************************************************************************
//*************************************************************************************************************************


public class Amison3 {

    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LList learnMe = new LList();                        // Linked list to hold the inOut objects
    public static double learningRate = .01;                          // learning rate to be used by the training algorithm
    public static double[][] weights;                                 // weights of the neural network's arcs
//*************************************************************************************************************************
//Method:	main
//Description:	handles all of the options of the program (train, recall, exit)
//Parameters:   none
//Returns:     	none
//Calls:        new TextFileClass(), KeyboardInputClass.getInteger(),KeyboardInputClass.getDouble()
//              KeyboardInputClass.getString(),TextFileClass.getFileName(), recall(), trainer()
//              TextFileClass.getFileNamePassed(), TextFileClass.getFileContents(), outUpdate(), LList.clear(),
//              LList.add(), new inOut()
//Globals:	weights, input, learnMe, learningRate
    public static void main(String[] args) {
        weights = new double[121][121];
        for (int t = 0; t < 121; t++) {             // for loop used to load the weights with random numbers
            for (int i = 0; i < 121; i++) {         // between +- .5
                double random = Math.random() - .5;
                weights[t][i] = random;
            }
        }
        while (true) {
            int options = input.getInteger(true, 1, 1, 3, "1=Train\n2=Recall\n3=Exit");
            if (options == 1) {                                             // if user wants to train
                learnMe.clear();                
                TextFileClass textFile = new TextFileClass();
                textFile.getFileName("Specify the text file containing the list of I/O file names:");
                textFile.getFileContents();                                 // get the contents of the desired file
                int numberOfRelations = Integer.parseInt(textFile.text[0]); // get the number of relationships in the file
                for (int i = 1; i <= numberOfRelations * 2; i++) {          // used to load the learnMe list with inOut objects
                    TextFileClass newTextFileIn = new TextFileClass();      // used to get the in file
                    TextFileClass newTextFileOut = new TextFileClass();     // used to get the out file
                    newTextFileIn.getFileNamePassed(textFile.text[i]);      // see TextFileClass for getFileNamePassed()
                    newTextFileOut.getFileNamePassed(textFile.text[i + 1]);
                    newTextFileIn.getFileContents();
                    newTextFileOut.getFileContents();
                    double[] newIn = parser(newTextFileIn.text[0]);         // convert the string to a double[]
                    double[] newOut = parser(newTextFileOut.text[0]);
                    Object newObject = new inOut(textFile.text[i], newIn, newOut, "");  // create new inOut object
                    i++;
                    learnMe.add(newObject);                                // add newObject to learnMe
                }
                outUpdate();                                               // update the out portion of the new objects
                int trainingIters = input.getInteger(true, 10, 1, 999999, "How many training iterations?");
                learningRate = input.getDouble(true, learningRate, 0.000000000000000000001, Double.MAX_VALUE, "Learning rate? Default = "+learningRate);
                for (int i = 0; i < trainingIters; i++) {                 // train the neural net on each object
                    trainer();
                    System.out.println("---------------------------------------");
                    System.out.println("");
                }
            }// end train if
            if (options == 2) {                                         // if user wants to recall           
                String recallMe = input.getString("", "Specify the text file containing the input pattern data:");
                while (recallMe.equals("")) {                   // if the user entered nothing
                    System.out.println("Enter a File Name");
                    recallMe = input.getString("", "Specify the text file containing the input pattern data:");
                }
                recall(recallMe);                               // recall desired file name
            }// end if user wants to recall
            if (options == 3) {                                 // if user wants to exit
                break;
            }// end if user wants to exit
        }

    }// end main
//*************************************************************************************************************************
//Method:	trainer
//Description:	trains the weights of the nueral network
//Parameters:   none
//Returns:     	none
//Calls:        LList.getLength(), LList.getEntry(), activation(), large(), 
//              tiny(), outUpdate(), print()
//Globals:	weights, learnMe, learningRate
    public static void trainer() {
        for (int i = 1; i <= learnMe.getLength(); i++) {            //used to train the network on each image
            inOut current = (inOut) learnMe.getEntry(i);
            for (int j = 0; j < 121; j++) {                         // used to each row of weights                              
                double delta = current.desired[j] - activation(current.in, weights[j]);// compute delta
                for (int k = 0; k < weights[j].length; k++) {       // used to compute each weight
                    weights[j][k] = large(tiny(weights[j][k] + (learningRate * delta * current.in[k])));// compute the new weights
                }
            }
        }
        outUpdate();                                                    // update all the out portions in learnMe()
        for (int i = 1; i <= learnMe.getLength(); i++) {                // used to print all the images
            inOut current = (inOut) learnMe.getEntry(i);
            print(current.in, current.desired, current.out);
            System.out.println("");
        }
    }// end trainer
//*************************************************************************************************************************
//Method:	outUpdate
//Description:	updates the out portion of each inOut object in the learnMe list
//Parameters:   none
//Returns:     	none
//Calls:        LList.getLength(), LList.getEntry(), large(), tiny(), activation()
//Globals:	learnMe, weights
    public static void outUpdate() {
        for (int i = 1; i <= learnMe.getLength(); i++) {        // used to update each object
            inOut current = (inOut) learnMe.getEntry(i);
            current.out = "";                                   // clear the current out String
            for (int j = 0; j < 121; j++) {
                double activate = large(tiny(activation(current.in, weights[j]))); // compute the activation
                if (activate > 0) {
                    current.out += '\u2588';                    // add a box to the current out string
                } else {
                    current.out += 'X';                         // add an X to the current out string
                }
            }
        }
    }
//*************************************************************************************************************************
//Method:	activation
//Description:	computes the activation value of a certain neuron
//Parameters:   double[] ins - the input values of an image
//              double[] weight - the weights of the arcs of the ins
//Returns:     	activation - the activation value at a neuron
//Calls:        none
//Globals:	none
    public static double activation(double[] ins, double[] weight) {
        double activation = 0.0;
        for (int i = 0; i < 121; i++) {             // used to compute the activation for each weight and input
            activation += (ins[i] * weight[i]);     // sum the weights * arcs
        }
        return activation;                          // return the activation value
    }
//*************************************************************************************************************************
//Method:	recal
//Description:	finds a certain file in the learnMe list, then displays it's contents
//Parameters:   String name - the name of the desired file
//Returns:     	none
//Calls:        LList.getLength(), LList.getEntry(), String.equals(), print(), KeyboardInputClass.getString()
//Globals:	learnMe, input
    public static void recall(String name) {
        while (true) {                      // while the current file has not been found
            String list = "";               // used to hold all the file names in memory
            for (int i = 1; i <= learnMe.getLength(); i++) { // used to step through all the objects until desired is found
                inOut findMe = (inOut) learnMe.getEntry(i);
                if (name.equals(findMe.name)) {             // if file found
                    print(findMe.in, findMe.desired, findMe.out);
                    return;
                }
                list += findMe.name + " ";      // if the name was not found, add the current name to the overall list
            }
            System.out.println("Could Not Find File, Re-Enter The Name of The File To Recall (Case Sensative)");
            name = input.getString(name, "Files Currently in Memory: (" + list + ")"); // get a valid file from the user
        }
    }
//*************************************************************************************************************************
//Method:	parser
//Description:	converts the -1's & 1's of the original image string into a double array
//Parameters:   String passed - the passed -1's and 1's of the original image
//Returns:     	double[] returner - a double array containing the image
//Calls:        new StringTokenizer(), StringTokenizer.nextToken(), Double.parseDouble()
//Globals:	none
    public static double[] parser(String passed) {
        double[] returner = new double[121]; 
        StringTokenizer parser = new StringTokenizer(passed);       // access to string tokenizer
        for (int i = 0; i < returner.length; i++) {                 // used to parse each item in the passed string
            returner[i] = Double.parseDouble(parser.nextToken(" "));// get the next token and put it in the current index
        }
        return returner;                                            // return the new array          
    }
//*************************************************************************************************************************
//Method:	convert
//Description:	converts numbers to X's and Boxes
//Parameters:   double[] passed - passed values to be converted
//Returns:     	String returner - the String of X's and Boxes to be printed
//Calls:        none
//Globals:      none
    public static String convert(double[] passed) {
        String returner = "";                       // initialize a blank string to be returned
        for (int i = 0; i < passed.length; i++) {   // used to step through every index in the array
            double here = passed[i];
            if (here < 0) {
                returner += 'X';                    // add an X to returner
            } else {
                returner += '\u2588';               // add a box to returner
            }
        }
        return returner;                            // return the new array of X's and Boxes
    }
//*************************************************************************************************************************
//Method:	print
//Description:	prints the strings in the desired order
//Parameters:   double[] inPassed - passed image that goes into the neural net 
//              double[] desiredPassed - passed image that is desired to come out of the neural net
//              String out - actual output of the neural network
//Returns:     	none
//Calls:        convert()
//Globals:	none
    public static void print(double[] inPassed, double[] desiredPassed, String out) {
        String in = convert(inPassed);              // convert the inPassed to a String X's and Boxes 
        String desired = convert(desiredPassed);    // convert the desiredPassed to a String of X's and Boxes
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
//*************************************************************************************************************************
//Method:	tiny
//Description:	keeps numbers from becoming too small for java to handle
//Parameters:   double passed - passed number to be checked if it is too small
//Returns:     	0 if the number is in the desired range, passed if it isn't
//Calls:        none
//Globals:	none
    public static double tiny(double passed) {
        if (passed < 0.000001 && passed > -0.000001) { // if passed is less than 0.000001 & greater than -0.000001
            return 0.0;
        }
        return passed;                                  // else return the original passed number
    }
//*************************************************************************************************************************
//Method:	large
//Description:	keeps numbers from becoming too large for java to handle
//Parameters:   double passed
//Returns:     	-999999999.0 if the number is less than -999999999.0, 999999999 if the number is greater than 999999999
//              passed if neither criteria is met
//Calls:        none
//Globals:	none
    public static double large(double passed){
        if(passed<-999999999){      // if passed is less than -999999999
            return-999999999.0;
        }
        else if(passed>999999999){  // if passed is greater than 999999999
            return 999999999.0;
        }
        return passed;              // else return the original number
    }
}// end Amison3
//*************************************************************************************************************************
//*************************************************************************************************************************
// class:       inoUt
// Description: creates objects to be utilized by the nueral net
// Data Values: none
// Constant:    none 
// Globals:     String name - the name of the in file
//              double[] in - array of the binary image going in to the neural net
//              double [] desired - the array of the desired out image
//              String out - the computed image by the neural network
//*************************************************************************************************************************
//*************************************************************************************************************************
class inOut {

    public String name;         // name of the in file
    public double[] in;         // file going into the neural network
    public double[] desired;    // file desired to come out of the neural network
    public String out;          // actual output of the neural network
//*************************************************************************************************************************
//Method:	inOut
//Description:	creates objects to be utilized by the nueral net
//Parameters:   String passedName - name of the in file
//              double[] passedIn - file going into the neural network
//              double[] passedDesired - file desired to come out of the neural network
//              String outPass - the actual output of the nueral network
//Returns:     	none
//Calls:        none
//Globals:      name,in,desired,out
    public inOut(String passedName, double[] passedIn, double[] passedDesired, String outPass) {
        name = passedName;
        in = passedIn;
        desired = passedDesired;
        out = outPass;
    }
}// end inOut
