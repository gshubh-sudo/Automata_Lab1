package automata;

import automata.resyntax.*;
import java.util.*;

public class PrettyPrinter implements RegExpVisitor {
    
    public EpsNFA visit(Closure closure) {
        System.out.print("(");
        //Not using the resulting automata directly, deep copying it
        EpsNFA a = closure.r.accept(this);
        a = a.shiftStates(1);
        a.addTransition(0, a.getInitialState(), a.EPSILON);
        int max = a.getMaxState();
        // System.out.println(a.getAcceptingStates().iterator().next());
        // System.out.println(1);
        Iterator<Integer> i = a.getAcceptingStates().iterator();
        // System.out.println(2);
        while (i.hasNext()){
            // System.out.println(21);
            a.addTransition(i.next(), a.getInitialState(), a.EPSILON);
            // System.out.println(22);
            // a.addTransition(i.next(), max+1, a.EPSILON);
            // System.out.println(23);
        }
        Iterator<Integer> j = a.getAcceptingStates().iterator();
        // System.out.println(2);
        while (j.hasNext()){
            a.addTransition(j.next(), max+1, a.EPSILON);
            // System.out.println(23);
        }
        // System.out.println(3);
        a.addTransition(0, max+1, a.EPSILON);
        
        a.emptyAcceptingStates();
        a.addAcceptingState(max+1);
        a.setInitialState(0);
        System.out.print("*)");
        return a;
    }
    
    public EpsNFA visit(Concatenation concat) {
        System.out.print("(");
        EpsNFA a = concat.r1.accept(this);
        EpsNFA b = concat.r2.accept(this);
        // a.printGV();
        // b.printGV();
        int maxA = a.getMaxState();
        b = b.shiftStates(maxA+1);
        //System.out.println(1234);
        //System.out.println(maxA);
       // b.printGV();
        Iterator<Integer> i = a.getAcceptingStates().iterator();
        while (i.hasNext()){
            a.addTransition(i.next(), b.getInitialState(), a.EPSILON);
        }
        // a.trans.putAll(b.trans);

        for(Integer src : b.trans.keySet()){
            for (Integer dst : b.trans.get(src).keySet()){
                a.addTransitions(src, dst, b.trans.get(src).get(dst));
            }
        }
        
        a.emptyAcceptingStates();
        Iterator<Integer> j = b.getAcceptingStates().iterator();
        while (j.hasNext()){
            a.addAcceptingState(j.next());
        }

        System.out.print(")");
        return a;
    }

    public EpsNFA visit(Dot dot) {
        System.out.print(".");
        EpsNFA a = new EpsNFA();
        Set<Character> chars= new HashSet<Character>();
        for (int i = 0; i <128; i++) {
            chars.add((char)i);
        }
        a.addTransitions(0,1,chars);
        a.addAcceptingState(1);

        return a;
    }

    public EpsNFA visit(Litteral litteral) {
        System.out.print(litteral.c);
        EpsNFA a = new EpsNFA();
//        State s1 = new State(1);
        a.addAcceptingState(1);
        a.addTransition(0,1,litteral.c);
        return a;
    }

    public EpsNFA visit(OneOrMore oneOrMore) {
        System.out.print("(");
        EpsNFA a= oneOrMore.r.accept(this);
        a = a.shiftStates(1);
        a.addTransition(0,1,a.EPSILON);

        int maxA= a.getMaxState();
        Iterator<Integer> i = a.getAcceptingStates().iterator();
        while (i.hasNext()){
            a.addTransition(i.next(), maxA+1, a.EPSILON);
        }
        a.addTransition(maxA,1,a.EPSILON);
        a.emptyAcceptingStates();
        a.addAcceptingState(maxA+1);
        a.setInitialState(0);
        System.out.print("+)");
        return a;
    }

    public EpsNFA visit(Union union) {
        System.out.print("(");
        EpsNFA a= union.r1.accept(this);
        System.out.print("|");
        EpsNFA b= union.r2.accept(this);
        int maxA=a.getMaxState();

        a = a.shiftStates(1);
        b = b.shiftStates(maxA+2);
        a.addTransition(0,1,a.EPSILON);

        
        
        a.addTransition(0,maxA+2,a.EPSILON);

        a.setInitialState(0);
        // a.trans.putAll(b.trans);


        for(Integer src : b.trans.keySet()){
            for (Integer dst : b.trans.get(src).keySet()){
                a.addTransitions(src, dst, b.trans.get(src).get(dst));
            }
        }

        Iterator<Integer> j = b.getAcceptingStates().iterator();
        while (j.hasNext()){
            a.addAcceptingState(j.next());
        }

        int max = a.getMaxState();
        Iterator<Integer> i = a.getAcceptingStates().iterator();
        while (i.hasNext()) {
            a.addTransition(i.next(), max+1, a.EPSILON);
        }
        a.emptyAcceptingStates();
        a.addAcceptingState(max+1);

        System.out.print(")");
        return a;
    }

    public EpsNFA visit(ZeroOrOne zeroOrOne) {
        System.out.print("(");
        EpsNFA a= zeroOrOne.r.accept(this);
        Iterator<Integer> i = a.getAcceptingStates().iterator();
        int max = a.getMaxState();
        while (i.hasNext()){
            a.addTransition(i.next(), max+1, a.EPSILON);
            // System.out.println(23);
        }
        a.addTransition(0, max+1, a.EPSILON);

        // EpsNFA b=new EpsNFA();
        // b.accepting.addAll(a.accepting);
        // b.setInitialState(0);
        // b.trans.putAll(a.trans);

        // int maxA=a.getMaxState();

        // b = b.shiftStates(maxA+1);
        
        // Iterator<Integer> j = b.getAcceptingStates().iterator();
        // while (j.hasNext()){
        //     a.addAcceptingState(j.next());
        // }

        // a.trans.putAll(b.trans);

        // a.addTransition(maxA,maxA+1, a.EPSILON);

        System.out.print("?)");
        return a;
    }
    
}   
