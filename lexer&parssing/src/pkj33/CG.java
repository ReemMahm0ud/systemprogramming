package pkj33;

import java.util.ArrayList;

public class CG {

    public static String Reg;
    public static ArrayList<String> listCount = new ArrayList<>();
    public static String destination;
    public static int locationCounter = 0;

    public static void loadRegisterA(String firstOperand) {
        if (Reg.equals(firstOperand)) {
            return;
        }
        
        Reg = firstOperand;
        System.out.println("\tLDA\t" + firstOperand);
    }
    public static void loadDestination(String destinationValue) {
        destination = destinationValue;
    }
    public static void generateStartOfProgram(String programName) {
        System.out.println(programName + "\tSTART\t0");
        System.out.println("\tEXTREF\tXWRITE, XREAD");
        System.out.println("\tSTL\tRETADR");

    }

   
    
   
    public static void generateDestination() {
        System.out.println("\tSTA\t" + destination);
    }
    public void addToLocationCounter(int value) {
        locationCounter += value;
        
    }

    
    public static void writeSubRoutine() {
        System.out.println("\t+SUB\tWRITEX");
    }

 
    public static void readSubRoutine() {
        System.out.println("\t+SUB\tREADX");
    }


    

    public static void generateIdListForMethod() {
        for (String item : listCount) {
            System.out.println("\tWORD\t" + item);
        }
        clearListCount();
    }
   
    public static void generateIdList() {
        for (String item : listCount) {
            System.out.println(item + "\tRESW\t1");
        }
        clearListCount();
    }
    public static void generateEndOfProgram() {
        System.out.println("\tLDL\tRETADR");
        System.out.println("\tRSUB");
    }

    public static void addToListCount(String id) {
        listCount.add(id);
    }

    private static void clearListCount() {
        listCount.clear();
    }


    public static void setDestination(String var) {
        destination = var;
    }

  

}