package automata;

import java.util.*;

//class State {
//	public int id;
//	public State(int a ) {
//		this.id = a;
//	}
//}

public class Automaton<State, Sym> {
    
    public TransMap<State, Sym> trans = new TransMap<State, Sym>();
    protected State initial;
    public Set<State> accepting = new HashSet<State>();
    public StateMap<State, Sym> actualTrans = new StateMap<State, Sym>();
    
    public Automaton() {
    }
    
    
    public Automaton(Automaton<State, Sym> a) {
        initial = a.initial;
        accepting.addAll(a.accepting);
        for (State src : a.trans.keySet())
            for (State dst : a.trans.get(src).keySet())
                addTransitions(src, dst, a.trans.get(src).get(dst));
    }
    
    
    public void addTransition(State src, State dst, Sym c) {
        addTransitions(src, dst, Collections.singleton(c));
    }
    
    
    public void addTransitions(State src, State dst, Collection<Sym> cs) {
        if (!trans.containsKey(src)){
            trans.put(src, new HashMap<State, Set<Sym>>());
            actualTrans.put(src, new HashMap<Sym, Set<State>>());}
        
        Map<State, Set<Sym>> stateTransitions = trans.get(src);
        if (!stateTransitions.containsKey(dst))
            stateTransitions.put(dst, new HashSet<Sym>());
        
        stateTransitions.get(dst).addAll(cs);

        Map<Sym, Set<State>> actualTransitions = actualTrans.get(src);
        for (Sym s : cs) {
            if (!actualTransitions.containsKey(s)) {
                actualTransitions.put(s, new HashSet<State>());
            }
            //System.out.println(actualTransitions.get(s));
            actualTransitions.get(s).addAll(Collections.singleton(dst));    
        }
        
    }
    
    
    public TransMap<State, Sym> getTransitions() {
        return trans;
    }
    
    
    public void addAcceptingState(State acc) {
        accepting.add(acc);
    }
    
    
    public void setInitialState(State initial) {
        this.initial = initial;
    }
    

    public State getInitialState() {
        return initial;
    }
    
    public Set<State> getAcceptingStates() {
        return accepting;
    }

    public State getFirstAcceptingState() {
        return accepting.iterator().next();
    }
    

    public Set<State> getSuccessors(State src, Sym sym) {
        Set<State> successors = new HashSet<State>();
        if (trans.containsKey(src))
            for (State dst : trans.get(src).keySet())
                if (trans.get(src).get(dst).contains(sym))
                    successors.add(dst);
        return successors;
    }
    
    
    public void mergeStates(State s1, State s2) {
        
        if (s1.equals(s2))
            return;
        
        // copy all s2-ingoing to s1-ingoing
        for (State src : getStates())
            if (trans.containsKey(src) && trans.get(src).containsKey(s2))
                addTransitions(src, s1, trans.get(src).get(s2));
        
        // copy all a2-outgoing to a1-outgoing
        if (trans.containsKey(s2))
            for (State dst : getStates())
                if (trans.get(s2).containsKey(dst))
                    addTransitions(s1, dst, trans.get(s2).get(dst));
        
        // remove a2.
        trans.remove(s2);
        for (State s : trans.keySet())
            trans.get(s).remove(s2);
        
        if (initial.equals(s2))
            initial = s1;
        
        if (accepting.remove(s2))
            accepting.add(s1);
    }
    
    
    
    // Returns the largest state explicitly mentioned in this automaton.
    public Set<State> getStates() {
        Set<State> states = new HashSet<State>();
        states.add(initial);
        states.addAll(accepting);
        for (State src : trans.keySet())
            for (State dst : trans.get(src).keySet())
                if (!trans.get(src).get(dst).isEmpty()) {
                    states.add(src);
                    states.add(dst);
                }
        return states;
    }

    public Set<Sym> getSymbols() {
        Set<Sym> symbols = new HashSet<Sym>();
        // states.add(initial);
        // states.addAll(accepting);
        for (State src : trans.keySet())
            for (State dst : trans.get(src).keySet())
                if (!trans.get(src).get(dst).isEmpty()) {
                    symbols.addAll(trans.get(src).get(dst));
                    // states.add(dst);
                }
        return symbols;
    }
    
    public void printGV() {
        
        String acc = "";
        for (State q : accepting)
            acc += stateStringRep(q) + " ";
        
        System.out.println("digraph finite_state_machine {");
        System.out.println("    rankdir=LR;");
        System.out.println("    size=\"10,10\"");
        System.out.println("    node [shape = doublecircle]; " + acc + ";");
        System.out.println("    node [shape = circle];");
        
        for (State src : trans.keySet()) {
            for (State dst : trans.get(src).keySet()) {
                Set<Sym> syms = trans.get(src).get(dst);
                System.out.println(
                        stateStringRep(src) + " -> " + 
                        stateStringRep(dst) + 
                        " [ label = \"" + symsStringRep(syms) + "\" ];");
            }
        }
        System.out.println("}");
    }
    
    public void emptyAcceptingStates() {
    	accepting.clear();
    }
    public void add(Automaton<State, Sym> b) {
    	this.trans.putAll(b.trans);
    }
    
    protected String stateStringRep(State s) {
        return "q" + s;
    }
    
    protected String symsStringRep(Set<Sym> syms) {
        return syms.toString().replaceAll("\\[|,|\\]", "").replace("\"", "");
    }
}


class TransMap<State, Sym> extends HashMap<State, Map<State, Set<Sym>>> {
}

class StateMap<State, Sym> extends HashMap<State, Map<Sym, Set<State>>> {
}