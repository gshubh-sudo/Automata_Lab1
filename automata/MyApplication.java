package automata;

import java.util.*;
import java.util.Scanner;

/* TODO: Implement a grep-like tool. */

public class MyApplication {

    // Your own regex search function here:
    // Takes a regex and a text as input
    // Returns true if the regex matches any substring of the text; otherwise returns false
    public static boolean mySearch(String regex, String text) {
        return false;  // should be something that correctly implements it
    }

    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);


        // Example of using the regexp parser:
        
        // EpsNFA a = REParser.parse("abcd").accept(new PrettyPrinter());
        
        String alphabets=s.nextLine();
        EpsNFA a = REParser.parse(s.nextLine()).accept(new PrettyPrinter());
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);

        System.out.println("OOPS");
        a = a.convertedDFA();
        // a.minimize();
        a.printGV();
        System.out.println(a.getSymbols());
        System.out.println(a.actualTrans);
        System.out.println();

        a.minimize();
        a.printGV();


        // System.exit(0);

        String str = "aaaabaabaccccccc";
        String substr;
        Character c;
        Set<Character> symbols = a.getSymbols();
        Integer currentstate = 0;
        Boolean found = false;
        while(s.hasNextLine()){
            found=false;
            currentstate = 0;
            str=s.nextLine();
            for (int j = 0; j < str.length(); j++) {
                substr = str.substring(j);

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
