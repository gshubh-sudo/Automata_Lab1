package automata;

import java.util.*;
import java.util.Scanner;

/* TODO: Implement a grep-like tool. */

public class MyApplication {

    public static boolean mySearch(String regex, String text) throws Exception{

        EpsNFA a = REParser.parse(regex).accept(new PrettyPrinter());

        //Printing out the most initial automata
        System.out.println("Initial automata");
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);

        //converting the automata to a DFA
        System.out.println("Converted to DFA");
        a = a.convertedDFA();
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);
        
        System.out.println("Minimizing the DFA");
        a.minimize();
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);

        String str = text; 
        String substr;
        Character c;
        Set<Character> symbols = a.getSymbols();
        Integer currentstate = 0;
        Boolean found = false;

        
        found=false;
        currentstate = 0;           
        for (int j = 0; j < str.length(); j++) {
            substr = str.substring(j);
            currentstate = 0;        

            for (int i = 0; i < substr.length(); i++) {
                c = substr.charAt(i);
                if (a.getAcceptingStates().contains(currentstate)) {
                    found = true;
                    break;
                }
                if (!symbols.contains(c)) {
                    break;
                }
                else {
                    currentstate = a.actualTrans.get(currentstate).get(c).iterator().next();
                }

                if (a.getAcceptingStates().contains(currentstate)) {
                    found = true;
                    break;
                }         
            }

            if (found == true) {
                break;
            }

        }

        return found;   
        
        
    }

    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        String alphabets = s.nextLine();

        long startTime = System.nanoTime();

        EpsNFA a = REParser.parse(s.nextLine()).accept(new PrettyPrinter());

        //Printing out the most initial automata
        System.out.println("Initial automata");
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);

        //converting the automata to a DFA
        System.out.println("Converted to DFA");
        a = a.convertedDFA();
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);
        
        System.out.println("Minimizing the DFA");
        a.minimize();
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);

        long endTime = System.nanoTime();

        long timeElapsed = endTime - startTime;

        System.out.println("Execution time in nanoseconds  : " + timeElapsed);

        System.out.println("Execution time in milliseconds : " + 
                                timeElapsed / 1000000);

        String str; 
        String substr;
        Character c;
        Set<Character> symbols = a.getSymbols();
        Integer currentstate = 0;
        Boolean found = false;

        while (s.hasNextLine()){
            found=false;
            currentstate = 0;
            str=s.nextLine();
            for (int j = 0; j < str.length(); j++) {
                substr = str.substring(j);
                currentstate = 0;        

                for (int i = 0; i < substr.length(); i++) {
                    c = substr.charAt(i);
                    if (a.getAcceptingStates().contains(currentstate)) {
                        found = true;
                        break;
                    }
                    if (!symbols.contains(c)) {
                        break;
                    }
                    else {
                        currentstate = a.actualTrans.get(currentstate).get(c).iterator().next();
                    }

                    if (a.getAcceptingStates().contains(currentstate)) {
                        found = true;
                        break;
                    }         
                }

                if (found == true) {
                    break;
                }

            }

            if (found == true) {
                System.out.println(str);
            }
        }
        



    }

}
