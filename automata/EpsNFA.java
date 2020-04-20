package automata;

import java.util.*;

public class EpsNFA extends Automaton<Integer, Character> {
    
    public final static Character EPSILON = '\u03B5';
    
    
    public EpsNFA() {
        initial = 0;
    }
    
    
    public Integer getMaxState() {
        return Collections.max(getStates());
    }
    
    
    public EpsNFA shiftStates(int delta) {
        EpsNFA newNfa = new EpsNFA();
        
        for (int src : trans.keySet())
            for (int dst : trans.get(src).keySet())
                newNfa.addTransitions(src + delta, dst + delta, trans.get(src).get(dst));
        
        for (int acc : accepting)
            newNfa.addAcceptingState(acc + delta);
        
        newNfa.setInitialState(initial + delta);
        
        return newNfa;
    }
    
    
    public Set<Integer> epsClosure(Integer q) {
        List<Integer> toVisit = new ArrayList<Integer>();
        toVisit.add(q);
        
        Set<Integer> closure = new HashSet<Integer>();
        
        while (!toVisit.isEmpty()) {
            Integer p = toVisit.remove(0);
            closure.add(p);
            
            if (trans.containsKey(p))
                for (Integer dst : trans.get(p).keySet())
                    if (trans.get(p).get(dst).contains(EPSILON))
                        if (!closure.contains(dst))
                            toVisit.add(dst);
        }
        
        return closure;
    }

    public EpsNFA convertedDFA() {
        EpsNFA nfa = this;
        Map<Set<Integer>, Integer> mapped = new HashMap<Set<Integer>, Integer>();
        List<Set<Integer>> queue = new ArrayList<Set<Integer>>();
        Set<Set<Integer>> checked = new HashSet<Set<Integer>>();

        EpsNFA dfa = new EpsNFA();
        Set<Integer> acc;
        
        queue.add(nfa.epsClosure(0));
        while (!queue.isEmpty()) {
            Set<Integer> a = queue.get(0);
            queue.remove(0);
            if (!mapped.containsKey(a)) {
                mapped.put(a, mapped.size());
                acc = new HashSet<Integer>(nfa.getAcceptingStates());
                acc.retainAll(a);
                if (!acc.isEmpty()){
                    dfa.addAcceptingState(mapped.get(a));
                }
            }
            if (!checked.contains(a)) {
                checked.add(a);
                Set<Integer> states = new HashSet<Integer>();
                for (Character s : this.getSymbols()) {
                    if(s.equals(this.EPSILON)){
                        continue;
                    }
                    for (Integer i : a) {
                        try{
                        states.addAll(this.actualTrans.get(i).get(s));}
                        catch(NullPointerException e){}
                    }
                    Set<Integer> closureStates = new HashSet<Integer>();
                    for (Integer j : states) {
                        try{
                        closureStates.addAll(this.epsClosure(j));}
                        catch(NullPointerException e){}
                    }

                    // if(closureStates.isEmpty()){
                    //     continue;
                    // }

                    if (!mapped.containsKey(closureStates) ) {
                        mapped.put(closureStates, mapped.size());
                        acc = new HashSet<Integer>(nfa.getAcceptingStates());
                        acc.retainAll(closureStates);
                        if (!acc.isEmpty()){
                            dfa.addAcceptingState(mapped.get(closureStates));
                        }


                    }

                    if (!checked.contains(closureStates) && !queue.contains(closureStates)) {
                        queue.add(closureStates);
                    }


                    dfa.addTransition(mapped.get(a), mapped.get(closureStates), s);
                    // System.out.println(closureStates);
                    // System.out.println(mapped.get(closureStates));
                    // System.out.println(dfa.actualTrans);
                    states.clear();

                }
            }
        }
        return dfa;
    }

    public void minimize() {
        int max = this.getMaxState();
        System.out.println(max);
        int[][] table = new int[max+1][max+1];

        Set<Integer> acc = getAcceptingStates();
        Set<Integer> states = new HashSet<Integer>(getStates());
        Set<Character> symbols = getSymbols();

        for (Integer i : states) {
            if (!acc.contains(i)) {
                for (Integer j : acc) {
                    table[i][j] = 1;
                    table[j][i] = 1;
                }    
            }
        }

        int ii, jj;

        for ( ii = 0; ii<=max; ii++) {
            for ( jj = 0; jj <= max; jj++) {
                System.out.print(table[ii][jj]);
            } 
            System.out.println();
        }

        boolean change = true;
        int i_next;
        int j_next;

        while (change) {
            change  = false;
            for (int i = 0; i <= max; i++) {
                for (int j = 0; j < i; j++) {
                    if (table[i][j] == 0) {
                        for (Character k : symbols) {
                            i_next = actualTrans.get(i).get(k).iterator().next();
                            j_next = actualTrans.get(j).get(k).iterator().next();
                            if (table[i_next][j_next] == 1) {
                                table[i][j] = 1;
                                table[j][i] = 1;
                                change = true;
                            }
                        }
                    }
                }
            }
            System.out.println("yo");
            for ( ii = 0; ii<=max; ii++) {
                for ( jj = 0; jj <= max; jj++) {
                    System.out.print(table[ii][jj]);
                } 
                System.out.println();
            }
        }

        while (!states.isEmpty()) {
            Integer a = states.iterator().next();
            for (int l = a; l <= max; l++) {
                if (table[a][l] == 0) {
                    mergeStates(a, l);
                    states.remove(l);
                }
            }
            states.remove(a);
        }
        // return this;
    }
    
}
