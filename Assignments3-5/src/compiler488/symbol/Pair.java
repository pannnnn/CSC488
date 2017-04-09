package compiler488.symbol;

/**
  * A wrapper class stored inside SymbolTable containing symbol information.
  */
public class Pair implements Comparable {
    public Symbol symbol;
    public boolean inherited;
    public Short offset;

    public Pair(Symbol symbol, boolean inherited) {
        this.symbol = symbol;
        this.inherited = inherited;
    }

    public int compareTo(Object o) {
        return (offset - ((Pair) o).offset);
    }

    public String toString() {
        return "[" + symbol + "] " + offset + " " + inherited + "\n";
    }
}
